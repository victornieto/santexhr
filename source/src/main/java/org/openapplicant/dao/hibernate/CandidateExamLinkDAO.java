package org.openapplicant.dao.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.ICandidateExamLinkDAO;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.domain.link.ExamLink;
import org.springframework.orm.hibernate3.HibernateCallback;


public class CandidateExamLinkDAO extends DomainObjectDAO<CandidateExamLink> 
	implements ICandidateExamLinkDAO {
	
	public CandidateExamLinkDAO() {
		super(CandidateExamLink.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<CandidateExamLink> findAllByCandidateId(final Long candidateId) {
		return (List<CandidateExamLink>) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session s) {
				return s.createCriteria(ExamLink.class)
					.createCriteria("candidate")
					.add(Restrictions.eq("id",candidateId))
					.list();
			}
		});
	}
}
