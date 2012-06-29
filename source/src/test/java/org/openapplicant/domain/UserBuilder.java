package org.openapplicant.domain;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.User;
import org.openapplicant.util.TestUtils;


/**
 * Builds test User objects
 */
public class UserBuilder {
	
	private String email = TestUtils.uniqueEmail();
	
	private String password = "pa55w0rd";
	
	private Name name = new NameBuilder().build();
	
	private Company company = new CompanyBuilder().build();
	
	private User.Role role = User.Role.ROLE_ADMIN;
	
	public UserBuilder withEmail(String value) {
		email = value;
		return this;
	}
	
	public UserBuilder withPassword(String value) {
		password = value;
		return this;
	}
	
	public UserBuilder withName(Name value) {
		name = value;
		return this;
	}
	
	public UserBuilder withCompany(Company company) {
		this.company = company;
		return this;
	}
	
	public UserBuilder withRole(User.Role role) {
		this.role = role;
		return this;
	}

	public User build() {
		User result = new User();
		result.setEmail(email);
		result.setPassword(password);
		result.setName(name);
		if(company != null) {
			company.addUser(result);
		}
		result.setRole(role);
		return result;
	}
}
