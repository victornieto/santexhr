package org.openapplicant.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.openapplicant.dao.IDomainObjectDAO;
import org.openapplicant.domain.DomainObject;
import org.openapplicant.util.Pagination;
import org.openapplicant.util.Params;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public abstract class DomainObjectDAO<T extends DomainObject> 
	extends HibernateDaoSupport
	implements IDomainObjectDAO<T> {
	
	private final Class<T> type;
	
	/**
	 * Constructs a new dao operating on instances of the given type.
	 * @param type
	 */
	public DomainObjectDAO(Class<T> type) {
		Assert.notNull(type);
		this.type = type;
	}
	
	@PostConstruct
	public void postConstruct() {
		// allow hibernateTemplate to do save/update/delete modifications 
		// when FlushMode is MANUAL.  (Default behavior throws an InvalidDataAccessApiUsageException)
		getHibernateTemplate().setCheckWriteOperations(false);
	}
	
	public void delete(Long id) {
		T entity = findOrNull(id);
		if(null != entity) {
			getHibernateTemplate().delete(entity);
		}
	}
	
	public void evict(T entity) {
		getHibernateTemplate().evict(entity);
	}

	@SuppressWarnings("unchecked")
	public T find(Long id) {
		T result = (T)getHibernateTemplate().get(type, id);
		assertRetrievalNotNull(result, "find failed. id:" + id);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public T findOrNull(Long id) {
		return (T)getHibernateTemplate().get(type, id);
	}
	
	public T findByGuid(String guid) {
		T result = findByGuidOrNull(guid);
		assertRetrievalNotNull(result, "findByGuid failed. guid: " + guid);
		return result;
	}
	
	public T findByGuidOrNull(String guid) {
		return findUniqueResult(
				"from " + type.getName() + " where entityInfo.businessGuid = :guid",
				new Params("guid", guid)
		);
	}

	public T save(T t) {
		getHibernateTemplate().saveOrUpdate(t);
		return t;
	}
	
	/**
	 * Assert that the given retrieval is not null.
	 * @param retrieval the item to test
	 * @throws DataRetreivalFailureException if retrieval is null
	 */
	protected void assertRetrievalNotNull(Object retrieval) {
		assertRetrievalNotNull(retrieval, null);
	}
	
	/**
	 * Assert that the given retrieval is not null.
	 * @param retrieval the item to test
	 * @param msg an error message to throw
	 * @throws DataRetreivalFailureException if retrieval is null
	 */
	protected void assertRetrievalNotNull(Object retrieval, String msg) {
		if(null == retrieval) {
			msg = StringUtils.defaultIfEmpty(msg, "[Assertion Failed] the retrieval was null");
			throw new DataRetrievalFailureException(msg);
		}
	}
	
	/**
	 * Helper method for paginating a criteria query.
	 * @param criteria
	 * @param pagination
	 * @return the query result
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(DetachedCriteria criteria, Pagination pagination) {
		return getHibernateTemplate().findByCriteria(criteria, pagination.getOffset(), pagination.getLimit());
	}
	
	/**
	 * Helper method for executing an hql query with named parameter binding.
	 * <p><b>example:</b></p>
	 * <pre>
	 * String hql = "from Candidate where company.id = :companyId"
	 * Map<String, Object> params  = new HashMap<String, Object>();
	 * params.put("companyId", someCompanyId);
	 * return findByQueryString(hql, params, somePagination)
	 * </pre>
	 * @param queryString the query string to execute
	 * @param params a the named parameter bindings
	 * @param pagination the pagination to apply
	 * @return the query results
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByQueryString(final String queryString, final Params params, final Pagination pagination) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createQuery(queryString)
								.setProperties(params.getAll())
								.setFirstResult(pagination.getOffset())
								.setMaxResults(pagination.getLimit())
								.list();
			}
		});
	}
	
	/**
	 * Helper method for executing an hql query with named parameter bindings
	 * that returns a unique result.
	 * @param queryString the query string to execute
	 * @param params the named parameter bindings.
	 * @return the unique result or null if non exists
	 * @throws DataAccessException if a non unique result is returned.
	 */
	@SuppressWarnings("unchecked")
	protected T findUniqueResult(final String queryString, final Params params) {
		return (T) getHibernateTemplate().execute( 
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						return session.createQuery(queryString)
								.setProperties(params.getAll())
								.uniqueResult();
					}
					
				}
		);
	}
}
