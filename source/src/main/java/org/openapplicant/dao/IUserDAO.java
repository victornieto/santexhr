package org.openapplicant.dao;

import java.util.List;

import org.openapplicant.domain.User;
import org.openapplicant.util.Pagination;
import org.springframework.dao.DataRetrievalFailureException;


public interface IUserDAO extends IDomainObjectDAO<User> {
	
	/**
	 * Finds a user with the given email
	 * 
	 * @param email the email of the user to find
	 * @return the retrieved user
	 * @throws DataRetrievalFailureException if no user has the given email
	 */
	User findByEmail(String email);
	
	/**
	 * Finds a user with the given email, returning null if not found.
	 * 
	 * @param email the email of the user to find
	 * @return the retrieved user or null if not found
	 */
	User findByEmailOrNull(String email);
	
	/**
	 * Find all active users with a given company id
	 * @param companyId
	 * @return the retrieved list of users
	 */
	List<User> findAllActiveUsersByCompanyId(Long companyId, Pagination pagination);

	List<User> findAllActiveAdminsByCompanyId(Long id, Pagination pagination);
}
