package org.openapplicant.dao.hibernate;

import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ne;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.openapplicant.dao.IRowCounter;
import org.openapplicant.domain.DomainObject;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class RowCounter extends HibernateDaoSupport implements IRowCounter {

	public boolean isUnique(final DomainObject entity, final List<String> uniquePropertyNames) {
		Assert.notEmpty(uniquePropertyNames);
		
		return 0 == (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Criteria criteria = session.createCriteria(entity.getClass());
				for(String each : uniquePropertyNames) {
					criteria.add(eq(each, getProperty(entity, each)));
				}
				if(!entity.isNew()) {
					criteria.add(ne("id", entity.getId()));
				}
				criteria.setProjection(rowCount());
				criteria.setFlushMode(FlushMode.MANUAL);
				return criteria.uniqueResult();
			}
		});
	}
	
	private Object getProperty(Object bean, String propertyName) {
		try {
			return PropertyUtils.getProperty(bean, propertyName);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
