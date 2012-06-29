package org.openapplicant.domain.question;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openapplicant.domain.question.Question;


public class QuestionTest {
	
	@Test
	public void merge_basic() {
		Question question1 = new CodeQuestionBuilder()
									.withId(Long.valueOf(1))
									.build();
			
		Question question2 = new CodeQuestionBuilder()
									.withId(Long.valueOf(2))
									.withPrompt("foo")
									.withTimeAllowed(60)
									.withName("question2")
									.build();
			
		question1.merge(question2);
		
		assertMerged(question1, question2);
		assertFalse(question2.getId().equals(question1.getId()));
		assertFalse(question2.getArtifactId().equals(question1.getArtifactId()));
	}
	
	@Test
	public void createSnapshot() {
		Question question = new CodeQuestionBuilder().build();
		Question snapshot = question.createSnapshot();
		
		assertTrue(snapshot.isSnapshot());
		assertTrue(snapshot.isLatestVersion());
		assertTrue(snapshot.isNotFrozen());
		
		assertFalse(question.isSnapshot());
		assertFalse(question.isLatestVersion());
		assertTrue(question.isFrozen());
		
		assertEquals(snapshot, question.getNextVersion());
		assertEquals(question, snapshot.getPreviousVersion());
		assertEquals(snapshot.getArtifactId(), question.getArtifactId());
		
		assertMerged(question, snapshot);
	}
	
	private void assertMerged(Question question1, Question question2) {
		assertEquals(question2.getPrompt(), question1.getPrompt());
		assertEquals(question2.getTimeAllowed(), question1.getTimeAllowed());
		assertEquals(question2.getName(), question1.getName());
	}
}
