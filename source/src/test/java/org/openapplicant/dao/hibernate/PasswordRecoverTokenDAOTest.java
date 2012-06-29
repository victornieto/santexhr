package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertNull;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.openapplicant.dao.IDomainObjectDAO;
import org.openapplicant.dao.IPasswordRecoveryTokenDAO;
import org.openapplicant.dao.IUserDAO;
import org.openapplicant.domain.PasswordRecoveryToken;
import org.openapplicant.domain.PasswordRecoveryTokenBuilder;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;


public class PasswordRecoverTokenDAOTest 
	extends DomainObjectDAOTest<PasswordRecoveryToken> {

	@Resource 
	private IPasswordRecoveryTokenDAO passwordRecoveryTokenDao;
	
	@Resource
	private IUserDAO userDao;
	
	@Override
	protected IDomainObjectDAO<PasswordRecoveryToken> getDomainObjectDao() {
		return passwordRecoveryTokenDao;
	}

	@Override
	protected PasswordRecoveryToken newDomainObject() {
		return new PasswordRecoveryTokenBuilder().build();
	}
	
	@Test
	public void deleteAllByUserId_oneToken() {
		User user = new UserBuilder().build();
		user = userDao.save(user);
		
		PasswordRecoveryToken token = new PasswordRecoveryToken(user);
		token = passwordRecoveryTokenDao.save(token);
		
		passwordRecoveryTokenDao.deleteAllByUserId(user.getId());
		
		assertNull(passwordRecoveryTokenDao.findOrNull(token.getId()));
	}
	
	@Test
	public void deleteAllByUserId_multipleTokens() {
		User user = new UserBuilder().build();
		user = userDao.save(user);
		
		PasswordRecoveryToken token1 = new PasswordRecoveryToken(user);
		PasswordRecoveryToken token2 = new PasswordRecoveryToken(user);
		
		passwordRecoveryTokenDao.save(token1);
		passwordRecoveryTokenDao.save(token2);
		
		passwordRecoveryTokenDao.deleteAllByUserId(user.getId());
		
		assertNull(passwordRecoveryTokenDao.findOrNull(token1.getId()));
		assertNull(passwordRecoveryTokenDao.findOrNull(token2.getId()));
	}
	

}
