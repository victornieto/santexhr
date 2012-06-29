package org.openapplicant.dao;

import java.util.List;

import org.openapplicant.domain.DomainObject;


public interface IRowCounter {
	
	/**
	 * Check if a domain object with the given properties is unique.
	 * @param entity the domain object to check
	 * @param uniquePropertyNames a list of property names used to create a 
	 * unique conjunction query.  
	 * @return true if entity is unique.
	 */
	boolean isUnique(DomainObject entity, List<String> uniquePropertyNames);

}
