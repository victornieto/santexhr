package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IUserDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.ProfileBuilder;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.util.TestUtils;
import org.springframework.dao.DataRetrievalFailureException;


public class CompanyDAOTest extends DomainObjectDAOTest<Company>{
	
	@Resource
	private ICompanyDAO companyDao;
	
	@Resource
	private IUserDAO userDao;
	
	@Override
	public Company newDomainObject() {
		return new CompanyBuilder().build();
	}
	
	@Override 
	public ICompanyDAO getDomainObjectDao() {
		return companyDao;
	}
	
	@Test
	public void save_addUser() {
		Company company = new CompanyBuilder()
								.withUsers(new UserBuilder().build())
								.build();
			
		company = companyDao.save(company);
		
		// company -> user
		company = companyDao.findByGuid(company.getGuid());
		assertEquals(1, company.getUsers().size());
		
		// user -> company
		String userGuid = company.getUsers().iterator().next().getGuid();
		User user = userDao.findByGuid(userGuid);
		assertEquals(company.getId(), user.getCompany().getId());
	}
	
	@Test
	public void save_removeUser() {
		Company company = new CompanyBuilder()
								.withUsers(new UserBuilder().build())	
								.build();
		
		company = companyDao.save(company);
		
		User user = company.getUsers().iterator().next();
		company.removeUser(user.getId());
		
		userDao.delete(user.getId());
		companyDao.save(company);
		
		// company -> user
		Company found = companyDao.findByGuid(company.getGuid());
		assertEquals(0, found.getUsers().size());
	}
	
	@Test
	public void save_profile() {
		Company company = new CompanyBuilder()
									.withProfile(new ProfileBuilder().build())
									.build();
		company = companyDao.save(company);
		
		String email = TestUtils.uniqueEmail();
		
		company.getProfile().setCandidateEmailsRecipient(email);
		company = companyDao.save(company);
		
		company = companyDao.findByGuid(company.getGuid());
		Profile profile = company.getProfile();
		assertEquals(email, profile.getCandidateEmailsRecipient());
	}
	
	@Test
	public void findByHostname() {
		Company company = new CompanyBuilder()
								.withHostName("fooHost")
								.build();
			
		company = companyDao.save(company);
		Company found = companyDao.findByHostname("fooHost");
		
		assertEquals(found.getId(), company.getId());
	}
	
	@Test(expected=DataRetrievalFailureException.class)
	public void findByHostname_failed() {
		companyDao.findByHostname(UUID.randomUUID().toString());
	}
	
	@Test
	public void findByProxyname() {
		Company company = new CompanyBuilder()
								.withProxyName("foo")
								.build();
			
		company = companyDao.save(company);
		Company found = companyDao.findByProxyname("foo");
		
		assertEquals(found.getId(), company.getId());
	}
	
	@Test(expected=DataRetrievalFailureException.class)
	public void findByProxyname_failed() {
		companyDao.findByProxyname(UUID.randomUUID().toString());
	}
	
	@Test
	public void findByEmailAlias() {
		Company company = new CompanyBuilder().build();
		company = companyDao.save(company);
		
		assertNotNull(company.getEmailAlias());
		
		Company found = companyDao.findByEmailAlias(company.getEmailAlias());
		assertEquals(found.getId(), company.getId());
	}
	
	@Test(expected=DataRetrievalFailureException.class)
	public void findByEmaiAlias_failed() {
		companyDao.findByEmailAlias(UUID.randomUUID().toString());
	}
	
	@Test
	public void findByEmailAliasOrNull() {
		Company company = new CompanyBuilder().build();
		company = companyDao.save(company);
		
		Company found = companyDao.findByEmailAliasOrNull(company.getEmailAlias());
		assertEquals(found.getId(), company.getId());
	}
	
	@Test
	public void findByEmailAliasOrNull_notFound() {
		assertNull(companyDao.findByEmailAliasOrNull(UUID.randomUUID().toString()));
	}

}
