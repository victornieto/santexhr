package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IExamDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.domain.question.CodeQuestion;
import org.openapplicant.domain.question.CodeQuestionBuilder;
import org.openapplicant.domain.question.EssayQuestion;
import org.openapplicant.domain.question.EssayQuestionBuilder;
import org.openapplicant.domain.question.MultipleChoiceQuestion;
import org.openapplicant.domain.question.MultipleChoiceQuestionBuilder;
import org.openapplicant.domain.question.Question;
import org.openapplicant.domain.question.QuestionVisitorAdapter;


public class ExamDAOTest extends DomainObjectDAOTest<Exam> {
	
	@Resource
	private IExamDAO examDao;
	
	@Resource 
	private ICompanyDAO companyDao;
	
	private Company savedCompany;
	
	@Override
	public Exam newDomainObject() {
		return new ExamBuilder().build();
	}
	
	@Override
	public IExamDAO getDomainObjectDao() {
		return examDao;
	}
	
	@Before
	public void Setup() {
		savedCompany = new CompanyBuilder()
							.withUsers(new UserBuilder().build())
							.build();
		
		savedCompany = companyDao.save(savedCompany);
	}
	
	@Test
	public void save_addQuestions() {
		Exam exam = new ExamBuilder()
				.withQuestions(
						new CodeQuestionBuilder().build(),
						new EssayQuestionBuilder().build(),
						new MultipleChoiceQuestionBuilder().build()
				)
				.build();
		examDao.save(exam);
		
		Exam found = examDao.findByGuid(exam.getGuid());
		assertEquals(3, found.getQuestions().size());
		
		Question code = found.getQuestions().get(0);
		assertTrue(new QuestionKindHelper(code).isCode());
	
		Question essay = found.getQuestions().get(1);
		assertTrue(new QuestionKindHelper(essay).isEssay());
		
		Question multipleChoice = found.getQuestions().get(2);
		assertTrue(new QuestionKindHelper(multipleChoice).isMultipleChoice());
	}
	
	private static class QuestionKindHelper extends QuestionVisitorAdapter {
		private boolean isCode = false;
		private boolean isEssay = false;
		private boolean isMultipleChoice = false;

		public QuestionKindHelper(Question question) {
			question.accept(this);
		}
		
		public boolean isCode() {
			return isCode;
		}
		
		public boolean isEssay() {
			return isEssay;
		}
		
		public boolean isMultipleChoice() {
			return isMultipleChoice;
		}
		
		public void visit(CodeQuestion question) {
			isCode = true;
		}

		public void visit(EssayQuestion question) {
			isEssay = true;
		}

		public void visit(MultipleChoiceQuestion question) {
			isMultipleChoice = true;
		}
	}
	
	/**
	 * Testing the update of all the fields that are common
	 * to all of the question types.
	 */
	@Test
	public void update_question() {
		Exam exam = new ExamBuilder()
				.withQuestions(
						new CodeQuestionBuilder().build()
				)
				.build();
		examDao.save(exam);
		
		Question question = exam.getQuestions().get(0);
	
		String artifactId = question.getArtifactId();
		
		String updatedPrompt = "foo? ... final answer?";
		question.setPrompt(updatedPrompt);
		
		Integer updatedTimeAllowed = 120;
		question.setTimeAllowed(updatedTimeAllowed);
		
		String updatedName = "barQuestion";
		question.setName(updatedName);
		
		exam.updateQuestion(question);
		examDao.save(exam);
		examDao.evict(exam);

		Exam found = examDao.findByGuid(exam.getGuid());
		question = (CodeQuestion) found.getQuestions().get(0);
		
		assertEquals(artifactId, question.getArtifactId());
		assertEquals(updatedPrompt, question.getPrompt());
		assertEquals(updatedTimeAllowed, question.getTimeAllowed());
		assertEquals(updatedName, question.getName());
	}
	
	@Test
	public void update_codeQuestion() {
		Exam exam = new ExamBuilder()
				.withQuestions(
						new CodeQuestionBuilder().build()
				)
				.build();
		examDao.save(exam);
		
		CodeQuestion question = (CodeQuestion) exam.getQuestions().get(0);
	
		String artifactId = question.getArtifactId();
		
		String updatedAnswer = "bar!";
		question.setAnswer(updatedAnswer);
		
		exam.updateQuestion(question);
		examDao.save(exam);
		examDao.evict(exam);

		Exam found = examDao.findByGuid(exam.getGuid());
		question = (CodeQuestion) found.getQuestions().get(0);
		
		assertEquals(artifactId, question.getArtifactId());
		assertEquals(updatedAnswer, question.getAnswer());
		
	}
	
	@Test
	public void update_essayQuestion() {
		Exam exam = new ExamBuilder()
				.withQuestions(
						new EssayQuestionBuilder().build()
				)
				.build();
		examDao.save(exam);
		
		EssayQuestion question = (EssayQuestion) exam.getQuestions().get(0);
	
		String artifactId = question.getArtifactId();
		
		String updatedAnswer = "bar!";
		question.setAnswer(updatedAnswer);
		
		exam.updateQuestion(question);
		examDao.save(exam);
		examDao.evict(exam);

		Exam found = examDao.findByGuid(exam.getGuid());
		question = (EssayQuestion) found.getQuestions().get(0);
		
		assertEquals(artifactId, question.getArtifactId());
		assertEquals(updatedAnswer, question.getAnswer());
		
	}
	
	@Test
	public void update_multipleChoiceQuestion() {
				
		Exam exam = new ExamBuilder()
				.withQuestions(
						new MultipleChoiceQuestionBuilder().build()
				)
				.build();
		examDao.save(exam);
		
		MultipleChoiceQuestion question = (MultipleChoiceQuestion) exam.getQuestions().get(0);
	
		String artifactId = question.getArtifactId();
		
		List<String> updatedChoices = Arrays.asList("W","X","Y","Z");
		question.setChoicesText(updatedChoices);
		
		Integer updatedAnswerIndex = 1;
		question.setAnswerIndex(updatedAnswerIndex);
		
		exam.updateQuestion(question);
		examDao.save(exam);
		examDao.evict(exam);

		Exam found = examDao.findByGuid(exam.getGuid());
		question = (MultipleChoiceQuestion) found.getQuestions().get(0);
		
		assertEquals(artifactId, question.getArtifactId());
		assertEquals(updatedChoices, question.getChoices());
		assertEquals(updatedAnswerIndex, question.getAnswerIndex());
		
	}
	
	@Test
	public void save_snapshotToPrevious() {
		Exam exam = new ExamBuilder().build();
		Exam snapshot = null; // exam.createSnapshot();
		
		examDao.save(snapshot);
		
		// assert original was cascaded
		assertNotNull(examDao.findByGuid(exam.getGuid()));
		assertNotNull(examDao.findByGuid(snapshot.getGuid()));
	}
	
	@Test
	public void save_previousToSnapshot() {
		Exam exam = new ExamBuilder().build();
		Exam snapshot = null; // exam.createSnapshot();
		
		examDao.save(exam);
		
		// assert snapshot was cascaded
		assertNotNull(examDao.findByGuid(exam.getGuid()));
		assertNotNull(examDao.findByGuid(snapshot.getGuid()));
	}
	
	@Test
	public void findLatestVersionsByCompanyId() {
		Exam exam1 = new ExamBuilder()
							.withCompany(savedCompany)
							.build();
		examDao.save(exam1);
		
		Exam snapshot1 = null; // exam1.createSnapshot();
		examDao.save(snapshot1);
		
		Exam exam2 = new ExamBuilder()
							.withCompany(savedCompany)
							.build();
		examDao.save(exam2);
		
		List<Exam> latestVersions = null; // examDao.findLatestVersionsByCompanyId(savedCompany.getId());
		
		assertEquals(2, latestVersions.size());
		for(Exam each : latestVersions) {
			//assertTrue(each.isLatestVersion());
		}
		
		assertFalse(latestVersions.contains(exam1));
		assertTrue(latestVersions.contains(snapshot1));
		assertTrue(latestVersions.contains(exam2));
	}
	
	@Test
	public void findLatestActiveVersionsByCompanyId() {
		Exam exam1 = new ExamBuilder()
							.withCompany(savedCompany)
							.withActive(true)
							.build();
		examDao.save(exam1);
		
		Exam exam2 = new ExamBuilder()
							.withCompany(savedCompany)
							.withActive(false)
							.build();
		examDao.save(exam2);
		
		List<Exam> exams = null; // examDao.findLatestActiveVersionsByCompanyId(savedCompany.getId());
		
		assertEquals(1, exams.size());
		assertTrue(exams.contains(exam1));
		
		Exam snapshot1 = null; // exam1.createSnapshot();
		// assertTrue(snapshot1.isActive());
		examDao.save(snapshot1);
		examDao.save(exam1);
		
		// exams = examDao.findLatestActiveVersionsByCompanyId(savedCompany.getId());
		assertEquals(1, exams.size());
		assertTrue(exams.contains(snapshot1));
	}
	
	@Test
	public void findLatestVersionByArtifactId() {
		Exam exam1 = new ExamBuilder().build();
		examDao.save(exam1);
		
		//assertEquals(exam1, examDao.findLatestVersionByArtifactId(exam1.getArtifactId()));
		
		Exam exam2 = null; // exam1.createSnapshot();
		examDao.save(exam2);
		
		//assertEquals(exam2, examDao.findLatestVersionByArtifactId(exam1.getArtifactId()));
	}
}
