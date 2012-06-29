package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.openapplicant.dao.IUserDAO;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.util.TestUtils;
import org.springframework.dao.DataRetrievalFailureException;


public class UserDAOTest extends DomainObjectDAOTest<User> {
	
	@Resource
	protected IUserDAO userDao;
	
	@Override
	public User newDomainObject(){
		return new UserBuilder().build();
	}
	
	@Override 
	public IUserDAO getDomainObjectDao() {
		return userDao;
	}
	
	@Test
	public void findByEmail() {
		String email = TestUtils.uniqueEmail();
		
		User user = new UserBuilder()
							.withEmail(email)
							.build();
		
		user = userDao.save(user);
		
		User found = userDao.findByEmail(email);
		
		assertEquals(found.getId(), user.getId());
	}
	
	@Test(expected=DataRetrievalFailureException.class)
	public void findByEmail_failed() {
		userDao.findByEmail(TestUtils.uniqueEmail());
	}
	
	@Test
	public void findByEmailOrNull() {
		assertNull(userDao.findByEmailOrNull(TestUtils.uniqueEmail()));
	}
}
