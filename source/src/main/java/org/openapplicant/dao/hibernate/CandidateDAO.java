package org.openapplicant.dao.hibernate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Candidate.Status;
import org.openapplicant.domain.PropertyCandidateSearch;
import org.openapplicant.domain.SimpleStringCandidateSearch;
import org.openapplicant.domain.User;
import org.openapplicant.domain.event.CandidateCreatedByUserEvent;
import org.openapplicant.util.Pagination;
import org.openapplicant.util.Params;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

import static org.hibernate.criterion.Restrictions.*;


@Repository
public class CandidateDAO extends DomainObjectDAO<Candidate> implements ICandidateDAO {

	private static final Log logger = LogFactory.getLog(CandidateDAO.class);
	
	public CandidateDAO() {
		super(Candidate.class);
	}

	public List<Candidate> findAllByCompanyId(Long companyId, Pagination pagination) {
		return findByQueryString(
				"from " + Candidate.class.getName() + " where company.id = :id",
				new Params("id", companyId),
				pagination
		);
	}
	
	public List<Candidate> performSearch(SimpleStringCandidateSearch search, Pagination pagination) {
	
		DetachedCriteria criteria = DetachedCriteria.forClass(Candidate.class);
		
		criteria.createAlias("company", "c")
			.add(eq("c.id", search.getUser().getCompany().getId()));
		
		if(search.getSearchTerms().size() > 0) {
			Disjunction anyNameInSearchTerms = Restrictions.disjunction();
			for(String each : search.getSearchTerms()) {
				anyNameInSearchTerms.add(ilike("name.first", each, MatchMode.ANYWHERE));
				anyNameInSearchTerms.add(ilike("name.last", each, MatchMode.ANYWHERE));
				anyNameInSearchTerms.add(ilike("name.middle", each, MatchMode.ANYWHERE));
			}
			criteria.add(anyNameInSearchTerms);
		}
			
		if(search.getDateRange().isBounded()) {
			criteria.createAlias("entityInfo", "e")
			.add(
				between("e.createdDate", 
					search.getDateRange().getStartDate(), 
					search.getDateRange().getEndDate()
				)
			);
		}
		List<Candidate> returnValue = findByCriteria(criteria, pagination);
		List<Candidate> skillsMatches = searchResumesForCompanyId(search.getSearchString(),search.getUser().getCompany().getId());
		return (List<Candidate>) CollectionUtils.union(returnValue, skillsMatches);
	}
	
	private List<Candidate> searchResumesForCompanyId(String searchString, Long companyId) {
		SQLQuery query = (SQLQuery) getSession().createSQLQuery("select c.* from candidate c inner join attachment_indexable ai on c.resume=ai.attachment_id where c.company=? and match(string_content) against (? in boolean mode)")
			.addEntity(Candidate.class)
			.setLong(0, companyId)
			.setString(1, searchString);
		List<Candidate> returnValue = (List<Candidate>) query.list();
		logger.debug("searchResumes for "+searchString+" returning list of "+returnValue.size()+" users");
		return returnValue;
	}
	
	public List<Candidate> performSearch(PropertyCandidateSearch search, Pagination pagination) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Candidate.class);
		
		criteria.createAlias("company", "c")
			.add(eq("c.id", search.getUser().getCompany().getId()));
		
		if(search.getName().isNotBlank()) {
			Disjunction anyNameInSearchTerms = Restrictions.disjunction();
			for(String token : search.getName().getFullName().split("\\s+")) {
				anyNameInSearchTerms.add(ilike("name.first", token, MatchMode.ANYWHERE));
				anyNameInSearchTerms.add(ilike("name.middle", token, MatchMode.ANYWHERE));
				anyNameInSearchTerms.add(ilike("name.last", token, MatchMode.ANYWHERE));
			}
			criteria.add(anyNameInSearchTerms);
		}
		
		/*
		 * 
		 this chunk of code works just fine
		 but it won't scale real well.
		if (search.getSkills().size() > 0) {
			logger.debug("Searching by skill!");
			criteria.createCriteria("resume")
				.add(Restrictions.sqlRestriction("{alias}.string_content like '%"+search.getSkillsString()+"%'"));
		}
		
		*/
		
		if(search.getDateRange().isBounded()) {
			criteria.createAlias("entityInfo", "e")
					.add(
						between("e.createdDate",
							search.getDateRange().getStartDate(),
							search.getDateRange().getEndDate()
						)
					);
		}
		
		List<Candidate> returnValue = findByCriteria(criteria, pagination);
		

		// here's how this works -- if there already are candidates, this is a narrowing thing
		// if not, it's expansive.
		if (search.getSkills().size() > 0) {
			List<Candidate> skillsMatches = searchResumesForCompanyId(search.getSkillsString(),search.getUser().getCompany().getId());
			if (returnValue.size() > 0)
				returnValue = (List<Candidate>) CollectionUtils.intersection(skillsMatches, returnValue);
			else 
				returnValue = skillsMatches;
		}
		
		return returnValue;
	}

	public List<Candidate> findAllByCompanyIdAndStatus(
			Long companyId, Candidate.Status status, Pagination pagination) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Candidate.class)
										.createAlias("company", "c")
										.add(eq("c.id", companyId))
										.add(eq("statusInternal", status));
		return findByCriteria(criteria, pagination);
	}
	
	public List<Candidate> findAllActiveCandidatesByCompanyId(long companyId,
			Pagination pagination) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Candidate.class)
										.createAlias("company", "c")
										.add(eq("c.id", companyId))
										.add(in("statusInternal", Candidate.Status.getActiveStatuses()));
		return findByCriteria(criteria, pagination);
	}
	
	public List<Candidate> findAllArchivedCandidatesByCompanyId(long companyId,
			Pagination pagination) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Candidate.class)
										.createAlias("company", "c")
										.add(eq("c.id", companyId))
										.add(in("statusInternal", Candidate.Status.getArchivedStatuses()));
		
		return findByCriteria(criteria, pagination);
	}

	public Candidate findByEmailOrNull(final String email) {
		return findUniqueResult(
				"from " + Candidate.class.getName() + " where email = :email",
				new Params("email", email)
		);
	}

	public Candidate findByEmailAndCompanyIdOrNull(String email, Long companyId) {
		return findUniqueResult(
				"from " + Candidate.class.getName() + " where email = :email and company.id = :companyId",
				new Params("email", email)
					.add("companyId", companyId)
		);
	}

	public List<Candidate> findByCompanyIDandDateRange(Long companyId,
			Calendar startDate, Calendar endDate) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Candidate.class);
		
		criteria.createAlias("company", "c").add(eq("c.id", companyId));
		criteria.createAlias("entityInfo","e").add(between("e.createdDate",startDate,endDate));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	// TODO:  whip up a unit test or two for this method
	public Map<String, Integer> findStatusCountsByCompanyId(final Long companyId) {
		final String sql = "select status,count(*) as c from candidate where company=? group by status";
		List results =  getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery(sql)
									.addScalar("status")
									.addScalar("c", Hibernate.INTEGER);
				query.setLong(0, companyId);
				return query.list();
			}
		});
		Map<String,Integer> returnValue = new HashMap<String, Integer>();
		// sigh.
		for(Status s : Candidate.Status.values()) {
			returnValue.put(s.name(),0);
		}
		for(Object rs : results) {
			Object[] rsx = (Object[])rs;
			returnValue.put((String)rsx[0],(Integer)rsx[1]);
		}
		return returnValue;
	}

    public List<Candidate> findByUser(final User user) {
        return (List<Candidate>) getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                List<CandidateCreatedByUserEvent> events = session.createCriteria(CandidateCreatedByUserEvent.class)
                        .add(Restrictions.eq("user", user))
                        .list();
                List<Candidate> candidates = new ArrayList<Candidate>();
                for (CandidateCreatedByUserEvent event : events) {
                    candidates.add(event.getCandidate());
                }
                return candidates;
            }
        });
    }


}
