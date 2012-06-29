package org.openapplicant.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.domain.Company;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAO extends DomainObjectDAO<Company> implements ICompanyDAO {

	public CompanyDAO() {
		super(Company.class);
	}
	
	public Company findByHostname(final String hostname) {
		Company result = findByProperty("hostName",hostname);
		assertRetrievalNotNull(result, "hostname: " + hostname);
		return result;

	}
	
	public Company findByEmailAlias(final String alias) {
		Company result = findByEmailAliasOrNull(alias);
		assertRetrievalNotNull(result);
		return result;
	}
	
	public Company findByEmailAliasOrNull(String alias) {
		return findByProperty("emailAlias", alias);
		
	}

	public Company findByProxyname(String proxyname) {
		Company result = findByProperty("proxyName",proxyname);
		assertRetrievalNotNull(result, "proxyname: " + proxyname);
		return result;
	}
	
	protected Company findByProperty(final String property, final String value) {
		return (Company)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session s) {
				return s.createCriteria(Company.class)
						.add(Restrictions.eq(property, value))
						.uniqueResult();
			}
		});
	}

}
