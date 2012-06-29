package org.openapplicant.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openapplicant.domain.ResponseMeasurements;

public class ResponseMeasurementsTest {
	
	@Test
	public void add() {
		ResponseMeasurements r1 = new ResponseMeasurements();
		r1.setAwayTime(20L);
		r1.setEraseChars(15L);
		r1.setErasePresses(10L);
		r1.setFocusChanges(45L);
		r1.setFocusTime(15L);
		r1.setHesitationTime(20L);
		r1.setKeyChars(50L);
		r1.setKeyPresses(60L);
		r1.setLineCount(100L);
		r1.setPasteChars(100L);
		r1.setPastePresses(2L);
		r1.setReviewingTime(50L);
		r1.setTotalTime(75L);
		r1.setTypingTime(90L);
		r1.setWordCount(80L);
		
		ResponseMeasurements r2 = r1.add(r1);
		
		assertTrue(r2 != r1);
		assertEquals(2 * r1.getAwayTime(), r2.getAwayTime());
		assertEquals(2 * r1.getEraseChars(), r2.getEraseChars());
		assertEquals(2 * r1.getErasePresses(), r2.getErasePresses());
		assertEquals(2 * r1.getFocusChanges(), r2.getFocusChanges());
		assertEquals(2 * r1.getHesitationTime(), r2.getHesitationTime());
		assertEquals(2 * r1.getKeyChars(), r2.getKeyChars());
		assertEquals(2 * r1.getKeyPresses(), r2.getKeyPresses());
		assertEquals(2 * r1.getLineCount(), r2.getLineCount());
		//assertEquals(2 * r1.getLinesPerHour(), r2.getLinesPerHour(), 0);
		assertEquals(2 * r1.getPasteChars(), r2.getPasteChars());
		assertEquals(2 * r1.getPastePresses(), r2.getPastePresses());
		assertEquals(2 * r1.getReviewingTime(), r2.getReviewingTime());
		assertEquals(2 * r1.getTotalTime(), r2.getTotalTime());
		assertEquals(2 * r1.getTypingTime(), r2.getTypingTime());
		assertEquals(2 * r1.getWordCount(), r2.getWordCount());
		//assertEquals(2 * r1.getWordsPerMinute(), r2.getWordsPerMinute(), 0);
	}

}
