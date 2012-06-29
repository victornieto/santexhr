package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openapplicant.util.CalendarUtils.december;
import static org.openapplicant.util.TestUtils.collectIds;
import static org.openapplicant.util.TestUtils.setCreatedDate;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.NameBuilder;
import org.openapplicant.domain.PropertyCandidateSearch;
import org.openapplicant.domain.SimpleStringCandidateSearch;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.util.Pagination;
import org.openapplicant.util.TestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Tests the search functions of CandidateDAO
 */
@ContextConfiguration(locations="/applicationContext-test.xml")
public class CandidateDAOSearchTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Resource
	private ICompanyDAO companyDao;
	
	@Resource
	private ICandidateDAO candidateDao;
	
	private Candidate janeDoe;
	
	private Candidate janeSmith;
	
	private Candidate johnMatrix;
	
	private Candidate franklinKirby;
	
	private User savedUser;
	
	private Pagination page1With50PerPage;
	
	@Before
	public void setUp() {		
		
		Company company = new CompanyBuilder()
								.withUsers(new UserBuilder().build())
								.build();
		
		company.addUser(savedUser);
		
		companyDao.save(company);
		savedUser = company.getUsers().iterator().next();
		
		janeDoe = new CandidateBuilder()
							.withCompany(company)
							.withName(new NameBuilder("jane","doe").build())
							.build();
		
		janeDoe = candidateDao.save(janeDoe);
		
		janeSmith = new CandidateBuilder()
							.withCompany(company)
							.withName(new NameBuilder("jane","smith").build())
							.build();

		janeSmith = candidateDao.save(janeSmith);
		
		johnMatrix = new CandidateBuilder()
							.withCompany(company)
							.withName(new NameBuilder("john","matrix").build())
							.build();

		johnMatrix = candidateDao.save(johnMatrix);
		
		franklinKirby = new CandidateBuilder()
							.withCompany(company)
							.withName(new NameBuilder("franklin","kirby").build())
							.build();
		
		franklinKirby = candidateDao.save(franklinKirby);
		
		setCreatedDate(janeDoe, december(2, 2007));
		setCreatedDate(johnMatrix, december(3, 2007));
		setCreatedDate(franklinKirby, december(5, 2007));
		setCreatedDate(janeSmith, december(6, 2007));
		
		janeDoe = candidateDao.save(janeDoe);
		johnMatrix = candidateDao.save(johnMatrix);
		franklinKirby = candidateDao.save(franklinKirby);
		janeSmith = candidateDao.save(janeSmith);
		
		page1With50PerPage = Pagination.oneBased().perPage(50).forPage(1);
	}

	
	@Test
	public void performSearch_simpleStringSearch() {
		List<Candidate> result = candidateDao.performSearch(
				new SimpleStringCandidateSearch("doe matrix kirby 12/1/2007 12/4/2007", savedUser),
				page1With50PerPage
		);
		List<Long> resultIds = TestUtils.collectIds(result);
		
		assertTrue(resultIds.contains(janeDoe.getId()));
		assertTrue(resultIds.contains(johnMatrix.getId()));
		// this person's date is not in the specified date range
		assertFalse(resultIds.contains(franklinKirby.getId())); 
	}
	
	@Test
	public void performSearch_propertyCandidateSearch() {
		List<Candidate> result = candidateDao.performSearch(
				new PropertyCandidateSearch.Builder(savedUser)
						.name("Jane DOE")
						.build(),
				page1With50PerPage
		);
		// search now returns *any* Jane, so both Jane Doe and Jane Smith will be returned
		List<Long> resultIds = TestUtils.collectIds(result);
		assertTrue(resultIds.contains(janeDoe.getId()));
		assertTrue(resultIds.contains(janeSmith.getId()));
		
		result = candidateDao.performSearch(
				new PropertyCandidateSearch.Builder(savedUser)
						.name("jane")
						.build(),
				page1With50PerPage
		);
		assertTrue(collectIds(result).contains(janeDoe.getId()));
		assertTrue(collectIds(result).contains(janeSmith.getId()));
		
		result = candidateDao.performSearch(
				new PropertyCandidateSearch.Builder(savedUser)
						.name("jane")
						.dateRange("12/1/2007 12/5/2007")
						.build(),
				page1With50PerPage
		);
		resultIds = TestUtils.collectIds(result);
		assertTrue(resultIds.contains(janeDoe.getId()));
		assertFalse(resultIds.contains(janeSmith.getId()));
	}

}
