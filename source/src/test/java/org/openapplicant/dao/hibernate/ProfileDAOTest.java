package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.validator.AssertFalse;
import org.junit.Test;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IDomainObjectDAO;
import org.openapplicant.dao.IProfileDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.ProfileBuilder;


public class ProfileDAOTest extends DomainObjectDAOTest<Profile> {

	@Resource
	private IProfileDAO profileDao;
	
	@Resource
	private ICompanyDAO companyDao;
	
	@Override
	protected IDomainObjectDAO<Profile> getDomainObjectDao() {
		return profileDao;
	}

	@Override
	protected Profile newDomainObject() {
		return new ProfileBuilder().build();
	}
	
	@Override
	@Test(expected=UnsupportedOperationException.class)
	public void delete() {
		Profile profile = new ProfileBuilder().build();
		profile = profileDao.save(profile);
		profileDao.delete(profile.getId());
	}
	
	@Test
	public void save_basic() {
		Profile profile = new ProfileBuilder()
								.withDailyReports(true)
								.withMaxRejectScore(20)
								.withMinInviteScore(80)
								.build();
		
		profile = profileDao.save(profile);
		
		profile = profileDao.findByGuid(profile.getGuid());
		
		assertEquals(true, profile.isForwardDailyReports());
		assertEquals((Integer)20, profile.getMaxRejectScore());
		assertEquals((Integer)80, profile.getMinInviteScore());
	}
	
	
	@Test
	public void testFindNightlyReportCompanies() {

		Profile profile1 = new ProfileBuilder()
								.withDailyReports(true)
								.build();
		Profile profile2 = new ProfileBuilder()
			.withDailyReports(false)
			.build();
		profileDao.save(profile1);
		profileDao.save(profile2);		
		
		Company company1 = new CompanyBuilder()
									.withName("company1")
									.withProfile(profile1)
									.build();
		Company company2 = new CompanyBuilder()
									.withName("company2")
									.withProfile(profile2)
									.build();
		companyDao.save(company1);
		companyDao.save(company2);
		
		List<Company> companies = profileDao.findNightlyReportCompanies();
		for(Company c : companies) {
			if (c.getId().equals(company1.getId())) 
				return;
			if (c.getId().equals(company2.getId()))
				fail();
		}
		fail();
	}
}
