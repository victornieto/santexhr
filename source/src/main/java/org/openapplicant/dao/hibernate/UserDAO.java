package org.openapplicant.dao.hibernate;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.IUserDAO;
import org.openapplicant.domain.User;
import org.openapplicant.util.Pagination;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class UserDAO extends DomainObjectDAO<User> implements IUserDAO {

	public UserDAO() {
		super(User.class);
	}
	
	public User findByEmail(final String email) {
		User result = findByEmailOrNull(email);
		assertRetrievalNotNull(result, "email: " + email);
		return result;
	}
	
	@Transactional(readOnly=true)
	public User findByEmailOrNull(final String email) {
		return (User)getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) {
				return session.createQuery("from " + User.class.getName() + " where email = :email")
							.setParameter("email", email)
							.uniqueResult();
			}
		});
	}

	@Transactional(readOnly=true)
	public List<User> findAllActiveUsersByCompanyId(Long companyId, Pagination pagination) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.createAlias("company", "c").add(eq("c.id", companyId)).add(Restrictions.eq("enabled", true));
		
		return findByCriteria(criteria, pagination);
	}

	@Transactional(readOnly=true)
	public List<User> findAllActiveAdminsByCompanyId(Long id, Pagination pagination) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria
			.createAlias("company", "c")
				.add(eq("c.id", id))
			.add(Restrictions.eq("enabled", true))
			.add(Restrictions.eq("role", User.Role.ROLE_ADMIN));
		
		return findByCriteria(criteria, pagination);
	}

}
