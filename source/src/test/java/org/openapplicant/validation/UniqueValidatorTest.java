package org.openapplicant.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.hibernate.validator.InvalidStateException;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.IUserDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.util.TestUtils;
import org.openapplicant.validation.Unique;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.validation.Errors;


@ContextConfiguration(locations="/applicationContext-test.xml")
public class UniqueValidatorTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource 
	private IUserDAO userDao;
	
	@Resource
	private ICandidateDAO candidateDao;
	
	@Test
	public void unique_basic() {
		User user1 = new UserBuilder().build();
		assertNotNull(user1.getEmail());
		assertFalse(user1.validate().hasErrors());
		
		userDao.save(user1);
		
		User user2 = new UserBuilder()
							// email should be validated against other users for uniqueness
							.withEmail(user1.getEmail()) 
							.build();
		
		Errors errors = user2.validate();
		assertEquals(1, errors.getErrorCount());
		assertEquals(
				getUniqueConstraintMessage(User.class, "getEmail"),
				errors.getFieldError("email").getDefaultMessage()
		);
	}
	
	@Test
	public void unique_updateOwner() {
		User user = new UserBuilder().build();
		user = userDao.save(user);
		
		// simulate updating a unique property with the same value.
		// an email should be considered non-unique iff it belongs to 
		// any OTHER user.
		user.setEmail(user.getEmail());
		assertFalse(user.validate().hasErrors());
	}
	
	@Test
	public void unique_validationSkippedOnSave() {
		// Test that UniqueValidator does not perform a "select count(*) from ..."
		// for each unique property on each object in the session cache.
		// We can verify this by testing for the absence of an InvalidStateException.
		// We can expect, however, that the database should throw a DataIntegrityViolationException
		// since a schema unique constraint has been violated.
		User user1 = new UserBuilder().build();
		userDao.save(user1);
		
		User user2 = new UserBuilder()
							.withEmail(user1.getEmail())
							.build();
		try {
			userDao.save(user2);
		} catch(InvalidStateException e) {
			fail("uniquness validation should be skipped");
		} catch(DataIntegrityViolationException e) {
			return;
		}
		fail("DataIntegrityViolationException was not thrown");
	}
	
	private String getUniqueConstraintMessage(Class clazz, String method) {
		Unique annotation = TestUtils.getAnnotation(clazz, method, Unique.class);
		return annotation.message();
	}
}
