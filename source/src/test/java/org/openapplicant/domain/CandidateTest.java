package org.openapplicant.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.PhoneNumber;
import org.openapplicant.domain.Sitting;
import org.springframework.test.util.ReflectionTestUtils;

public class CandidateTest {
	
	@Test
	public void isActive() {
		Candidate candidate = new CandidateBuilder().build();
		
		candidate.setStatus(Candidate.Status.NOT_TESTED);
		assertTrue(candidate.isActive());
		assertFalse(candidate.isArchived());
		
		candidate.setStatus(Candidate.Status.RESUME_REJECTED);
		assertFalse(candidate.isActive());
		assertTrue(candidate.isArchived());
	}
	
	@Test
	public void getPhoneNumebrs() {
		Candidate candidate = new CandidateBuilder()
									.withWorkPhoneNumber(null)
									.withHomePhoneNumber(null)
									.withCellPhoneNumber(null)
									.build();
		
		assertTrue(candidate.getWorkPhoneNumber().isBlank());
		assertTrue(candidate.getCellPhoneNumber().isBlank());
		assertTrue(candidate.getHomePhoneNumber().isBlank());
		
		candidate.setWorkPhoneNumber(new PhoneNumberBuilder().build());
		assertFalse(candidate.getWorkPhoneNumber().isBlank());
		
		candidate.setHomePhoneNumber(new PhoneNumberBuilder().build());
		assertFalse(candidate.getHomePhoneNumber().isBlank());
		
		candidate.setCellPhoneNumber(new PhoneNumberBuilder().build());
		assertFalse(candidate.getCellPhoneNumber().isBlank());
	}
	
	@Test
	public void getPhoneNumbersInternal() {
		Candidate candidate = new CandidateBuilder()
										.withCellPhoneNumber(new PhoneNumberBuilder().build())
										.build();
		int initialSize = ((Map)ReflectionTestUtils.invokeGetterMethod(candidate, "getPhoneNumbersInternal")).size();
		assertTrue(initialSize > 0);
		
		candidate.setCellPhoneNumber(PhoneNumber.createEmptyPhoneNumber());
		
		int finalSize = ((Map) ReflectionTestUtils.invokeGetterMethod(candidate, "getPhoneNumbersInternal")).size();
		assertEquals(initialSize, finalSize); // empty phone numbers should not be persisted.
	}
	
	@Test
	public void setEmail_blank() {
		Candidate candidate = new CandidateBuilder()
									.withEmail("   \r\n")
									.build();
		assertNull(candidate.getEmail());
	}
	
	@Test
	public void addSitting() {
		Candidate candidate = new CandidateBuilder().build();
		Sitting sitting1 = new SittingBuilder().build();
		Sitting sitting2 = new SittingBuilder().build();
		
		candidate.addSitting(sitting1);
		assertEquals(sitting1, candidate.getLastSitting());
		
		candidate.addSitting(sitting2);
		assertEquals(sitting2, candidate.getLastSitting());
	}
	
	@Test
	public void unarchive() {
		Candidate candidate = new CandidateBuilder().build();
		candidate.setStatus(Candidate.Status.SENT_EXAM);
		
		assertEquals(Candidate.Status.SENT_EXAM, candidate.getLastActiveStatus());
		
		candidate.setStatus(Candidate.Status.CANDIDATE_REJECTED);
		assertEquals(Candidate.Status.CANDIDATE_REJECTED, candidate.getStatus());
		assertEquals(Candidate.Status.SENT_EXAM, candidate.getLastActiveStatus());
		
		candidate.unarchive();
		
		assertEquals(Candidate.Status.SENT_EXAM, candidate.getStatus());
	}
}
