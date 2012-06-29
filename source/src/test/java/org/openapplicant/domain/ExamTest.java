package org.openapplicant.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.question.CodeQuestionBuilder;
import org.openapplicant.domain.question.EssayQuestionBuilder;
import org.openapplicant.domain.question.Question;


public class ExamTest {
	
	@Test
	public void addQuestion_basic() {
		Exam exam = new ExamBuilder()
							.withQuestions(
									new CodeQuestionBuilder().build(), 
									new EssayQuestionBuilder().build()
							)
							.build();
		
		assertEquals(2, exam.getQuestions().size());
	}
	
	@Test(expected=IllegalStateException.class)
	public void addQuestion_duplicateArtifactId() {
		Question question1 = new CodeQuestionBuilder().build();
		
		Exam exam = new ExamBuilder()
							.withQuestions(question1)
							.build();
		
		Question question2 = new CodeQuestionBuilder()
									.withArtifactId(question1.getArtifactId())
									.build();
		
		exam.addQuestion(question2);
	}
	
	@Test
	public void getQuestion_basic() {
		Question question = new EssayQuestionBuilder()
								.withId(Long.valueOf(1))
								.build();
			
		Exam exam = new ExamBuilder()
							.withQuestions(question)
							.build();
			
		
		Question found = exam.getQuestionById(question.getId());
		assertEquals(found, question);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getQuestion_notFound() {
		Exam exam = new ExamBuilder()
						.withQuestions(new Question[]{})
						.build();
		
		exam.getQuestionById(Long.valueOf(1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getQuestion_null() {
		Exam exam = new ExamBuilder()
							.withQuestions(new Question[]{})
							.build();
		
		exam.getQuestionById((Long)null);
	}
	
	@Test
	public void getQuestionByArtifactId() {
		Question question = new CodeQuestionBuilder().build();
		
		Exam exam = new ExamBuilder()
							.withQuestions(question)
							.build();
		
		assertEquals(question, exam.getQuestionByArtifactId(question.getArtifactId()));
	}
	
	@Test
	public void getQuestionByArtifactId_notFound() {
		Exam exam = new ExamBuilder()
							.withQuestions(new Question[]{})
							.build();
		assertNull(exam.getQuestionByArtifactId("foo"));
	}
	
	@Test
	public void updateQuestion() {
		Question question = new CodeQuestionBuilder()
									.withPrompt("foo")
									.build();
		
		Exam exam = new ExamBuilder()
							.withQuestions(question)
							.build();
		
		Question updatedValue = new CodeQuestionBuilder()
									.withArtifactId(question.getArtifactId())
									.withPrompt("bar")
									.build();
		
		exam.updateQuestion(updatedValue);
		
		assertTrue(question == exam.getQuestions().get(0));
		assertEquals("bar", exam.getQuestions().get(0).getPrompt());
	}
	
	@Test
	public void updateQuestion_frozen() {
		Question question = new CodeQuestionBuilder()
									.withPrompt("foo")
									.build();
		question.freeze();
		Exam exam = new ExamBuilder()
							.withQuestions(question)
							.build();
		Question updatedValue = new CodeQuestionBuilder()
										.withArtifactId(question.getArtifactId())
										.withPrompt("bar")
										.build();
		
		exam.updateQuestion(updatedValue);
		
		assertTrue(question != exam.getQuestions().get(0));
		assertEquals("bar", exam.getQuestions().get(0).getPrompt());
		assertEquals(question.getArtifactId(), exam.getQuestions().get(0).getArtifactId());
		assertFalse(exam.getQuestions().get(0).isFrozen());
		assertTrue(exam.getQuestions().get(0).isSnapshot());
	}
	
	@Test
	public void freeze() {
		Exam exam = new ExamBuilder()
							.withQuestions(
									new CodeQuestionBuilder().build(),
									new EssayQuestionBuilder().build()
							)
							.build();
		
		//assertFalse(exam.isFrozen());
		
		//exam.freeze();
		
		//assertTrue(exam.isFrozen());
		for(Question each : exam.getQuestions()) {
			assertTrue(each.isFrozen());
		}
	}
	
	@Test
	public void createSnapshot() {
		Exam exam = new ExamBuilder()
							.withCompany(new CompanyBuilder().build())
							.withName("Foo")
							.withDescription("foo")
							.withGenre("bar")
							.withActive(true)
							.withQuestions(
									new CodeQuestionBuilder().build(),
									new EssayQuestionBuilder().build()
							)
							.build();
		
		Exam snapshot = null; // exam.createSnapshot();
		
		//assertTrue(snapshot.isSnapshot());
		//assertTrue(snapshot.isLatestVersion());
		//assertTrue(snapshot.isNotFrozen());
		
		//assertFalse(exam.isSnapshot());
		//assertFalse(exam.isLatestVersion());
		//assertTrue(exam.isFrozen());
		
		//assertEquals(snapshot, exam.getNextVersion());
		//assertEquals(exam, snapshot.getPreviousVersion());
		
		assertEquals(exam.getArtifactId(), snapshot.getArtifactId());
		//assertEquals(exam.getCompany(), snapshot.getCompany());
		assertEquals(exam.getName(), snapshot.getName());
		assertEquals(exam.getDescription(), snapshot.getDescription());
		assertEquals(exam.getGenre(), snapshot.getGenre());
		assertEquals(exam.getQuestions(), snapshot.getQuestions());
		
		for(Question each : exam.getQuestions()) {
			assertTrue(each.isFrozen());
		}
		for(Question each : snapshot.getQuestions()) {
			assertTrue(each.isFrozen());
		}
	}
	
	@Test
	public void createSnapshot_activeStatus() {
		// snapshot should inherit the active status of the last version
		Exam exam = new ExamBuilder()
							.withActive(false)
							.build();
		
		Exam snapshot = null; // exam.createSnapshot();
		
		//assertFalse(snapshot.isActive());
		//assertFalse(exam.isActive());

		exam = new ExamBuilder()
						.withActive(true)
						.build();
		snapshot = null; // exam.createSnapshot();
		
		//assertTrue(snapshot.isActive());
		//assertTrue(exam.isActive());
	}
}
