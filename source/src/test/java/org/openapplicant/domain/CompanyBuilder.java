package org.openapplicant.domain;

import java.util.UUID;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Builds test Company objects.
 */
public class CompanyBuilder {
	
	private User[] users = new User[]{};
	
	private String emailAlias = UUID.randomUUID().toString();
	
	private String hostName = "mycompany.com" + UUID.randomUUID().toString();
	
	private String proxyName = "proxy.mycompany.com" + UUID.randomUUID().toString();
	
	private String contextRoot = "/openapplicant";
	
	private String name = "Origniate Labs";
	
	private int hostPort = 80;
	
	private int proxyPort = 80;
	
	private Profile profile = new ProfileBuilder().build();

	public CompanyBuilder withUsers(User...users) {
		if(null == users) {
			users = new User[]{};
		}
		this.users = users;
		return this;
	}

	public CompanyBuilder withEmailAlias(String alias) {
		this.emailAlias = alias;
		return this;
	}
	
	public CompanyBuilder withHostName(String value) {
		hostName = value;
		return this;
	}
	
	public CompanyBuilder withProxyName(String value) {
		this.proxyName = value;
		return this;
	}
	
	public CompanyBuilder withName(String value) {
		this.name = value;
		return this;
	}
	
	public CompanyBuilder withHostPort(int value) {
		hostPort = value;
		return this;
	}
	
	public CompanyBuilder withProxyPort(int value) {
		proxyPort= value;
		return this;
	}
	
	public CompanyBuilder withContextRoot(String value) {
		contextRoot = value;
		return this;
	}
	
	public CompanyBuilder withProfile(Profile value) {
		this.profile = value;
		return this;
	}
	
	public Company build() {
		Company company = new Company();
		for(User each: users) {
			company.addUser(each);
		}
		company.setEmailAlias(emailAlias);
		company.setHostName(hostName);
		company.setProxyName(proxyName);
		company.setName(name);
		company.setHostPort(hostPort);
		company.setProxyPort(proxyPort);
		company.setContextRoot(contextRoot);
		if(profile != null) {
			ReflectionTestUtils.invokeSetterMethod(company, "profile", profile);
		}
		return company;
	}

}
