package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openapplicant.util.TestUtils.assertMarch;
import static org.openapplicant.util.TestUtils.collectIds;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICandidateSearchDAO;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.domain.CandidateSearch;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.PropertyCandidateSearch;
import org.openapplicant.domain.SimpleStringCandidateSearch;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.util.Pagination;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


@ContextConfiguration(locations="/applicationContext-test.xml")
public class CandidateSearchDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	//========================================================================
	// MEMBERS
	//========================================================================
	@Resource 
	private ICandidateSearchDAO candidateSearchDao;
	
	@Resource
	private ICompanyDAO companyDao;
	
	private User savedUser;
	
	private Company savedCompany;
	
	//------------------------------------------------------------------------
	// SET UP
	//------------------------------------------------------------------------
	@Before
	public void setUp() {
		savedCompany = new CompanyBuilder()
								.withUsers(new UserBuilder().build())
								.build();
			
		savedCompany = companyDao.save(savedCompany);
		savedUser = savedCompany.getUsers().iterator().next();
	}
	
	//========================================================================
	// TESTS
	//========================================================================
	
	//------------------------------------------------------------------------
	// SAVE (SIMPLE STRING CANDIDATE SEARCH)
	//------------------------------------------------------------------------
	@Test
	public void save_simpleStringCandidateSearch() {
		CandidateSearch search = 
			new SimpleStringCandidateSearch("john doe doe java San Francisco, CA 90210 3/4/2006 - 3-5-2007", savedUser);
		
		List<String> searchTerms = Arrays.asList("john","doe","java","San","Francisco","CA","90210");
		
		search = candidateSearchDao.save(search);
		
		search = candidateSearchDao.findByGuid(search.getGuid());
		
		SimpleStringCandidateSearch simpleSearch = (SimpleStringCandidateSearch) search;
		
		assertMarch(4, 2006, simpleSearch.getDateRange().getStartDate());
		assertMarch(5, 2007, simpleSearch.getDateRange().getEndDate());
		assertEquals(savedUser.getId(), simpleSearch.getUser().getId());
		assertEquals(savedCompany.getId(), simpleSearch.getUser().getCompany().getId());
		
		for(String each : simpleSearch.getSearchTerms()) {
			assertTrue(each, searchTerms.contains(each));
		}
		assertEquals(
				StringUtils.join(simpleSearch.getSearchTerms(), " "),
				searchTerms.size(), simpleSearch.getSearchTerms().size()
		);
	}
	
	//------------------------------------------------------------------------
	// SAVE (PROPERTY PARSING CANDIDATE SEARCH)
	//------------------------------------------------------------------------
	@Test
	public void save_propertyParsingCandidateSearch() {
		CandidateSearch search = new 
			PropertyCandidateSearch.Builder(savedUser)
						.name("joe bob briggs")
						.skills("java javascript ruby")
						.dateRange("3/6/2001 3/7/2001")
						.build();
		
		search = candidateSearchDao.save(search);
		search = candidateSearchDao.findByGuid(search.getGuid());
		
		PropertyCandidateSearch propSearch = (PropertyCandidateSearch) search;
		
		assertEquals("joe", propSearch.getName().getFirst());
		assertEquals("bob", propSearch.getName().getMiddle());
		assertEquals("briggs", propSearch.getName().getLast());
		assertEquals("java", propSearch.getSkills().get(0));
		assertEquals("javascript", propSearch.getSkills().get(1));
		assertEquals("ruby", propSearch.getSkills().get(2));
		assertMarch(6, 2001, propSearch.getDateRange().getStartDate());
		assertMarch(7, 2001, propSearch.getDateRange().getEndDate());
		assertEquals(savedUser.getId(), propSearch.getUser().getId());
		assertEquals(savedCompany.getId(), propSearch.getUser().getCompany().getId());
	}
	
	//------------------------------------------------------------------------
	// FIND ALL BY USER ID
	//------------------------------------------------------------------------
	@Test
	public void findAllByUserId() throws Exception {
		CandidateSearch simpleSearch = new SimpleStringCandidateSearch("", savedUser);
		CandidateSearch propSearch = new PropertyCandidateSearch.Builder(savedUser).build();
		
		propSearch = candidateSearchDao.save(propSearch);
		simpleSearch = candidateSearchDao.save(simpleSearch);
		
		List<CandidateSearch> searches = candidateSearchDao.findAllByUserId(
				savedUser.getId(), 
				Pagination.oneBased().perPage(50).forPage(1)
		);
		
		List<Long> searchIds = collectIds(searches);
		
		// testing the actual order of the search results leads to inconsistent tests.
		assertEquals(2, searchIds.size());
		assertTrue(searchIds.contains(propSearch.getId()));
		assertTrue(searchIds.contains(simpleSearch.getId()));
	}
}
