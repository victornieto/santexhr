package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.openapplicant.dao.ISittingDAO;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.GradeBuilder;
import org.openapplicant.domain.Response;
import org.openapplicant.domain.ResponseBuilder;
import org.openapplicant.domain.Score;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.SittingBuilder;
import org.openapplicant.domain.question.CodeQuestionBuilder;
import org.openapplicant.domain.question.Question;


public class SittingDAOTest extends DomainObjectDAOTest<Sitting> {
	
	@Resource
	private ISittingDAO sittingDao;
	
	@Override
	public Sitting newDomainObject() {
		return new SittingBuilder().build();
	}
	
	@Override
	public ISittingDAO getDomainObjectDao() {
		return sittingDao;
	}
	
	@Override
	@Test(expected=UnsupportedOperationException.class)
	public void delete() {
		Sitting sitting = new SittingBuilder().build();
		sitting = sittingDao.save(sitting);
		sittingDao.delete(sitting.getId());
	}
	
	@Test
	public void save_exam() {
		Sitting sitting = new SittingBuilder().build();
		assertNotNull(sitting.getExam());
		assertTrue(sitting.getExam().getQuestions().size() > 0);
		
		sitting = sittingDao.save(sitting);
		Long examId = sitting.getExam().getId();
		
		Sitting found = sittingDao.findByGuid(sitting.getGuid());
		
		assertNotNull(examId);
		assertEquals(examId, found.getExam().getId());
		assertEquals(found.getExam().getQuestions().size(), sitting.getQuestionsAndResponses().size());
	}
	
	@Test
	public void advanceQuestion() {
		Question q1 = new CodeQuestionBuilder().build();
		Question q2 = new CodeQuestionBuilder().build();
		
		Sitting sitting = new SittingBuilder()
								.withExam(
										new ExamBuilder()
												.withQuestions(q1, q2)
												.build()
								)
								.build();
		
		sittingDao.save(sitting);
		
		assertEquals(q1, sitting.advanceToNextQuestion());
		assertTrue(sitting.hasNextQuestion());
		
		sitting = sittingDao.save(sitting);
		
		assertEquals(q2, sitting.advanceToNextQuestion());
		assertFalse(sitting.hasNextQuestion());
	}
	
	@Test
	public void gradeResponse() {
		Sitting sitting = new SittingBuilder()
								.withExam(
										new ExamBuilder()
												.withQuestions(new CodeQuestionBuilder().build())
												.build()
								)
								.build();
		sitting = sittingDao.save(sitting);
		
		Question q = sitting.advanceToNextQuestion();
		Response response = new ResponseBuilder().build();
		sitting.assignResponse(q.getId(), response);
		sitting = sittingDao.save(sitting);
			
		sitting.gradeResponse(response.getId(), new GradeBuilder().build());
		
		Score score = sitting.getScore();
		
		sitting = sittingDao.save(sitting);
		
		assertEquals(score, sitting.getScore());
	}
}
