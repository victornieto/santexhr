package org.openapplicant.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.StringContains.containsString;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IUserDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.ProfileBuilder;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;



@ContextConfiguration(locations="/applicationContext-test.xml")
public class ReportServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource
	private ReportService reportService;
	
	@Resource
	private IUserDAO userDao;
	
	@Resource 
	private ICompanyDAO companyDao;
	
	@Test
	public void testDeltaReport1() {
		// need to add a company
		// and add four candidates
		
		Company company = new CompanyBuilder().build();
		
		Candidate candidate1 = new CandidateBuilder()
			.withName(new Name("Candidate1"))
			.withCompany(company)
			.build();
		Candidate candidate2 = new CandidateBuilder()
			.withName(new Name("Candidate2"))
			.withCompany(company)
			.build();
		Candidate candidate3 = new CandidateBuilder()
			.withName(new Name("Candidate3"))
			.withCompany(company)
			.build();

		reportService.getCompanyDao().save(company);
		reportService.getCandidateDao().save(candidate1);
		reportService.getCandidateDao().save(candidate2);
		reportService.getCandidateDao().save(candidate3);
		
		GregorianCalendar start = new GregorianCalendar();
		start.add(Calendar.HOUR, -2);
		GregorianCalendar end   = new GregorianCalendar();
		
		String report = reportService.getDeltaReport(company.getId(), start, end);
		assertNotNull(report);
		assertThat(report,containsString("Candidate1"));
		
		logger.info("Here's the report:\n"+report+"\n");
	}
	
	@Test
	public void testRecipientList() {
		
		User user1 = new UserBuilder()
							.withEmail("mrw@mail.com")
							.withRole(User.Role.ROLE_ADMIN)
							.build();
		User user2 = new UserBuilder()
							.withEmail("mrw+test1@mail.com")
							.withRole(User.Role.ROLE_HR)
							.build();
		
		userDao.save(user1);
		userDao.save(user2);
		
		Profile profile = new ProfileBuilder()
								.withDailyReportRecipient(null)
								.build();
		Company company = new CompanyBuilder()
								.withUsers(user1,user2)
								.withProfile(profile)
								.build();
		companyDao.save(company);
		
		
		List<String> results = reportService.findDailyReportsRecipient(company);
		assertEquals(1,results.size());
		assertEquals("mrw@mail.com",results.get(0));
	}
	
}
