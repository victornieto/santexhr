package org.openapplicant.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Company;
import org.openapplicant.util.TestUtils;
import org.openapplicant.validation.UniqueWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.validation.Errors;


@ContextConfiguration(locations="/applicationContext-test.xml")
public class CandidateValidatorTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Resource
	private ICandidateDAO candidateDao;
	
	@Resource
	private ICompanyDAO companyDao;
	
	@Test
	public void email_basicUniqueness() {
		Company company = new CompanyBuilder().build();
		companyDao.save(company);
		
		Candidate candidate1 = new CandidateBuilder()
										.withEmail(TestUtils.uniqueEmail())
										.withCompany(company)
										.build();
		
		assertFalse(candidate1.validate().hasErrors());
		
		candidateDao.save(candidate1);
		
		Candidate candidate2 = new CandidateBuilder()
										.withEmail(candidate1.getEmail())
										.withCompany(company)
										.build();
		
		Errors errors = candidate2.validate();
		assertTrue(errors.hasErrors());
		assertEmailNotUnique(errors);
		assertFalse(candidate1.validate().hasErrors());
	}
	
	@Test
	public void email_uniquePerCompany() {
		Company company1 = new CompanyBuilder().build();
		Company company2 = new CompanyBuilder().build();
		companyDao.save(company1);
		companyDao.save(company2);
		
		Candidate candidate1 = new CandidateBuilder()
										.withEmail(TestUtils.uniqueEmail())
										.withCompany(company1)
										.build();
		candidateDao.save(candidate1);
		
		Candidate candidate2 = new CandidateBuilder()
										.withEmail(candidate1.getEmail())
										.withCompany(company2)
										.build();
		
		assertFalse(candidate2.validate().hasErrors());
	}
	
	@Test
	public void email_ignoreNull() {
		Candidate candidate1 = new CandidateBuilder()
									.withEmail(null)
									.build();

		candidateDao.save(candidate1);
		
		int count = simpleJdbcTemplate.queryForInt(
				"select count(*) from candidate where email is null"
		);
		assertTrue(count >= 1);
		
		Candidate candidate2 = new CandidateBuilder()
				.withEmail(null)
				.build();
		
		assertFalse(candidate2.validate().hasErrors());
	}
	
	private void assertEmailNotUnique(Errors errors) {
		UniqueWith annotation = TestUtils.getAnnotation(Candidate.class, "getEmail", UniqueWith.class);
		assertEquals(annotation.message(), errors.getFieldError("email").getDefaultMessage());
	}
}
