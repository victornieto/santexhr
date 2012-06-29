package org.openapplicant.dao;

import org.openapplicant.domain.PasswordRecoveryToken;

public interface IPasswordRecoveryTokenDAO 
	extends IDomainObjectDAO<PasswordRecoveryToken> {
	
	/**
	 * Deletes all tokens associated with the given suer id.
	 * @param userId
	 */
	void deleteAllByUserId(long userId);
	
	/**
	 * Deletes all tokens associated with the given user id.  Does not 
	 * raise an exception if the operation fails.
	 * @param userId
	 */
	void deleteAllQuietlyByUserId(long userId);

}
