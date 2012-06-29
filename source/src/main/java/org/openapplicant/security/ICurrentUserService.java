package org.openapplicant.security;

import org.openapplicant.domain.User;

public interface ICurrentUserService {
	
	/**
	 * @return the current user
	 */
	User getCurrentUser();

}
