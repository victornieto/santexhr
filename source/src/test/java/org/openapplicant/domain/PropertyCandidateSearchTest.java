package org.openapplicant.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openapplicant.util.TestUtils.assertMarch;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.domain.PropertyCandidateSearch;
import org.openapplicant.domain.User;

public class PropertyCandidateSearchTest {
	
	private User user;
	
	//------------------------------------------------------------------------
	// SET UP
	//------------------------------------------------------------------------
	@Before
	public void setUp() {
		user = new UserBuilder().build();
	}
	
	//========================================================================
	// TESTS
	//========================================================================
	
	//------------------------------------------------------------------------
	// NAME
	//------------------------------------------------------------------------
	@Test
	public void name() {
		PropertyCandidateSearch search = 
			new PropertyCandidateSearch.Builder(user)
										.name(" john doe ")
										.build();
		assertEquals("john", search.getName().getFirst());
		assertEquals("", search.getName().getMiddle());
		assertEquals("doe", search.getName().getLast());
		
		search = new PropertyCandidateSearch.Builder(user)
											.name("joe bob briggs")
											.build();
		
		assertEquals("joe", search.getName().getFirst());
		assertEquals("bob", search.getName().getMiddle());
		assertEquals("briggs", search.getName().getLast());
	}
	
	//------------------------------------------------------------------------
	// SKILLS
	//------------------------------------------------------------------------
	@Test
	public void skills() {
		PropertyCandidateSearch search = 
				new PropertyCandidateSearch.Builder(user)
											.skills("java javascript ruby")
											.build();
		assertEquals(3, search.getSkills().size());
		assertTrue(search.getSkills().contains("java"));
		assertTrue(search.getSkills().contains("javascript"));
		assertTrue(search.getSkills().contains("ruby"));
		
		search = new PropertyCandidateSearch.Builder(user)
											.skills("java,javascript,ruby")
											.build();
		assertEquals(3, search.getSkills().size());
		assertTrue(search.getSkills().contains("java"));
		assertTrue(search.getSkills().contains("javascript"));
		assertTrue(search.getSkills().contains("ruby"));
		
		search = new PropertyCandidateSearch.Builder(user)
											.skills("java, ruby on rails , c++,")
											.build();
		assertEquals(3, search.getSkills().size());
		assertTrue(search.getSkills().contains("java"));
		assertTrue(search.getSkills().contains("ruby on rails"));
		assertTrue(search.getSkills().contains("c++"));
	}
	
	//------------------------------------------------------------------------
	// DATE RANGE
	//------------------------------------------------------------------------
	@Test
	public void dateRange() {
		PropertyCandidateSearch search = new PropertyCandidateSearch.Builder(user)
										.dateRange("3/6/2001 3/7/2001")
										.build();
		assertMarch(6, 2001, search.getDateRange().getStartDate());
		assertMarch(7, 2001, search.getDateRange().getEndDate());
	}

}
