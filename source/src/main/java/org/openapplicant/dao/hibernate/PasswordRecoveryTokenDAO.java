package org.openapplicant.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.dao.IPasswordRecoveryTokenDAO;
import org.openapplicant.domain.PasswordRecoveryToken;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;


@Repository
public class PasswordRecoveryTokenDAO extends DomainObjectDAO<PasswordRecoveryToken> 
	implements IPasswordRecoveryTokenDAO {
	
	private static final Log log = LogFactory.getLog(PasswordRecoveryTokenDAO.class);
	
	public PasswordRecoveryTokenDAO() {
		super(PasswordRecoveryToken.class);
	}
	
	public void deleteAllByUserId(long userId) {
		List<PasswordRecoveryToken> tokens = findAllByUserId(userId);
		for(PasswordRecoveryToken each : tokens) {
			getHibernateTemplate().delete(each);
		}
	}

	public void deleteAllQuietlyByUserId(long userId) {
		try {
			deleteAllByUserId(userId);
		} catch(DataAccessException e) {
			log.warn("Unable to delete token for userId " + userId, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<PasswordRecoveryToken> findAllByUserId(long userId) {
		return getHibernateTemplate().find(
				"from " + PasswordRecoveryToken.class.getName() + " where user.id = ?", 
				userId
		);
	}
}
