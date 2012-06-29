package org.openapplicant.domain;

import org.openapplicant.domain.PasswordRecoveryToken;
import org.openapplicant.domain.User;

public class PasswordRecoveryTokenBuilder {
	
	private User user = new UserBuilder().build();
	
	public PasswordRecoveryTokenBuilder withUser(User value) {
		user = value;
		return this;
	}
	
	public PasswordRecoveryToken build() {
		return new PasswordRecoveryToken(user);
	}
}
