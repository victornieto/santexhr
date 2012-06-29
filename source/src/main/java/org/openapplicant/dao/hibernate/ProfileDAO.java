package org.openapplicant.dao.hibernate;

import static org.hibernate.criterion.Restrictions.between;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.IProfileDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.Candidate.Status;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;


@Repository
public class ProfileDAO extends DomainObjectDAO<Profile> implements IProfileDAO {
	
	public ProfileDAO() {
		super(Profile.class);
	}
	
	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException("Profiles must not be deleted");
	}

	public List<Company> findNightlyReportCompanies() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Company.class);
		
		criteria.createAlias("profile", "p").add(eq("p.forwardDailyReports", true));
		return getHibernateTemplate().findByCriteria(criteria);
	}

}
