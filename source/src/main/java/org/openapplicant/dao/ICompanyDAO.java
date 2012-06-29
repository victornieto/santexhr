package org.openapplicant.dao;

import org.openapplicant.domain.Company;
import org.springframework.dao.DataRetrievalFailureException;

public interface ICompanyDAO extends IDomainObjectDAO<Company> {
	
	/**
	 * Finds a company with the given host name.
	 * 
	 * @param hostname the company's hostname
	 * @return the company with the given hostname
	 * @throws DataRetrievalFailureException if no company exists with
	 * hostname.
	 */
	Company findByHostname(String hostname);
	
	/**
	 * Finds a company with the given proxy name.
	 * 
	 * @param proxyname the company's proxyname
	 * @return the company with the given proxyname
	 * @throws DataRetrievalFailureException if no company exists with
	 * proxyname
	 */
	Company findByProxyname(String proxyname);
	
	/**
	 * Finds a company with the given email alias.
	 * 
	 * @param alias the company's email alias
	 * @return the company with the given email alias
	 * @throws DataRetrievalFailureException if no company exists with 
	 * alias
	 */
	Company findByEmailAlias(String alias);
	
	/**
	 * Finds a company with the given email alias, returning null if not found.
	 * @param alias the company's email alias
	 * @return the company with the given email alias or null
	 */
	Company findByEmailAliasOrNull(String alias);
}
