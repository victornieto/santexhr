package org.openapplicant.dao;

import org.openapplicant.domain.DomainObject;
import org.springframework.dao.DataRetrievalFailureException;


/**
 * Base interface for for performing CRUD operations on a domain object.
 */
public interface IDomainObjectDAO<T extends DomainObject> {
	
	/**
	 * Updates a domain object or creates it if new.
	 * 
	 * @param t the domain object to save
	 * @return the saved domainObject
	 */
	T save(T t);
	
	/**
	 * Finds a domain object with the given id.
	 * 
	 * @param id the id of the domain object to find
	 * @return the domain object
	 * @throws DataRetrievalFailureException if no domain object exists 
	 * for the given id.
	 */
	T find(Long id);
	
	/**
	 * Finds a domain object with the given id, returning null if not found.
	 * 
	 * @param id the id of the domain object to find.
	 * @return the domain object or null if none found.
	 */
	T findOrNull(Long id);
	
	/**
	 * Finds a domain object using it's guid identifier.  For better performance,
	 * findById should be favored over this method whenever possible.
	 * 
	 * @param guid the guid of the domain object to find.
	 * @return the domain object
	 * @throws DataRetrievalFailureException if no domain object exists 
	 * for the given guid.
	 */
	T findByGuid(String guid);
	
	/**
	 * Finds a domain object using it's guid identifier, returning null if 
	 * not found.  For better performance, findByIdOrNull should be favored 
	 * over this method whenever possible.
	 * 
	 * @param guid the guid of the domain object to find
	 * @return the domain object or null if none exists.
	 */
	T findByGuidOrNull(String guid);
	
	/**
	 * Deletes a domain object with the given id.
	 * 
	 * @param id the id of the domain object to delete.
	 */
	void delete(Long id);
	
	/**
	 * Evicts a domain object from the session cache
	 * 
	 * @param entity the domain object to evict.
	 */
	void evict(T entity);
	
}
