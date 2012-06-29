package org.openapplicant.dao.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.IExamLinkDAO;
import org.openapplicant.domain.link.ExamLink;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;


@Repository
public class ExamLinkDAO extends DomainObjectDAO<ExamLink> implements IExamLinkDAO {
	
	public ExamLinkDAO() {
		super(ExamLink.class);
	}

	@SuppressWarnings("unchecked")
	public List<ExamLink> findAllByCompanyId(final Long companyId) {
		return (List<ExamLink>)getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(Session s) {
				return s.createCriteria(ExamLink.class)
							.createCriteria("company")
							.add(Restrictions.eq("id", companyId))
							.list();
			}
		});
	}
}
