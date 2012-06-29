package org.openapplicant.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openapplicant.domain.User;

public class UserTest {
	
	@Test
	public void passwordMatches() {
		String password = "manager";
		
		User user = new UserBuilder()
							.withPassword(password)
							.build();
		
		assertFalse(user.getPassword().equals(password));
		assertTrue(user.passwordMatches(password));
	}
	
	@Test
	public void passwordMatches_whitespace() {
		String password = "   manager   ";
		
		User user = new UserBuilder()
							.withPassword(password)
							.build();
		
		assertTrue(user.passwordMatches(password.trim()));
		assertTrue(user.passwordMatches(password));
	}

}
