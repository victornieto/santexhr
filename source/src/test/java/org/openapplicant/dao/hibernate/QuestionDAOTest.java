package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.openapplicant.dao.IGradeDAO;
import org.openapplicant.dao.IQuestionDAO;
import org.openapplicant.dao.IResponseDAO;
import org.openapplicant.dao.ISittingDAO;
import org.openapplicant.dao.hibernate.QuestionDAO;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.Grade;
import org.openapplicant.domain.GradeBuilder;
import org.openapplicant.domain.QuestionStatistics;
import org.openapplicant.domain.Response;
import org.openapplicant.domain.ResponseBuilder;
import org.openapplicant.domain.Score;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.SittingBuilder;
import org.openapplicant.domain.question.CodeQuestionBuilder;
import org.openapplicant.domain.question.Question;


public class QuestionDAOTest extends DomainObjectDAOTest<Question> {

	@Resource
	protected IQuestionDAO questionDao;

	@Resource
	private ISittingDAO sittingDao;
	
	@Resource
	private IGradeDAO gradeDao;
	
	@Resource
	private IResponseDAO responseDao;

	@Override
	public Question newDomainObject() {
		return new CodeQuestionBuilder().build();
	}

	@Override
	public IQuestionDAO getDomainObjectDao() {
		return questionDao;
	}
	

	@Test
	public void test_questionStatistics() {
		Question question = new CodeQuestionBuilder().build();
		Exam exam = new ExamBuilder().withQuestions(question).build();
		
		
		Sitting sitting1 = new SittingBuilder()
			.withExam(exam)
			.build();

		Sitting sitting2 = new SittingBuilder()
			.withExam(exam)
			.build();


		sittingDao.save(sitting1);
		sittingDao.save(sitting2);
		
		Grade grade1 = new GradeBuilder().withNoScores()
			.addScore("function", new Score(80L))
			.addScore("form", new Score(40L))
			.build();
		logger.info("Grade 1 score is "+grade1.getScore().getValue().intValue());
	
		Grade grade2 = new GradeBuilder().withNoScores()
			.addScore("function", new Score(70L))
			.addScore("form", new Score(50L))
			.build();
		logger.info("Grade 2 score is "+grade2.getScore().getValue().intValue());

		
		Response response1 = new ResponseBuilder()
			.withGrade(grade1)
			.withLineCount(20L).build();
				
		Response response2 = new ResponseBuilder()
			.withGrade(grade2)
		     .withLineCount(10L).build();

		responseDao.save(response1);
		responseDao.save(response2);
		
		
		sitting1.assignResponse(question.getId(),response1);
		sitting2.assignResponse(question.getId(), response2);

		
		sittingDao.save(sitting1);
		sittingDao.save(sitting2);
		
		
		QuestionStatistics qStat = ((QuestionDAO) questionDao)
				.findQuestionStatisticsByIdAndColumn(question.getId(), "line_count");
		assertEquals(qStat.getActiveMean().intValue(),qStat.getMean().intValue());
		assertEquals(7,qStat.getStddev().intValue());
		assertEquals(15,qStat.getMean().intValue());

		QuestionStatistics qStat_overall = ((QuestionDAO) questionDao)
			.findQuestionStatisticsByIdAndColumn(question.getId(), "score");
		assertEquals(60,qStat_overall.getMean().intValue());
	}

	@Test
	public void save_nullTime() {
		Question q = new CodeQuestionBuilder().withTimeAllowed(null).build();
		q = questionDao.save(q);
		q = questionDao.findByGuid(q.getGuid());

		assertNull(q.getTimeAllowed());
	}


	@Test
	public void getCorrectnessStatistics() {
		Question question = new CodeQuestionBuilder().build();
		Response response = new ResponseBuilder().build();

		Sitting sitting = new SittingBuilder().withExam(
				new ExamBuilder().withQuestions(question).build()).build();

		sittingDao.save(sitting);
		sitting.assignResponse(question.getId(), response);
		sittingDao.save(sitting);

		sitting.gradeResponse(response.getId(), new GradeBuilder()
				.withNoScores().addScore("function", new Score(10)).build());
		sittingDao.save(sitting);

		QuestionStatistics stats = questionDao.getCorrectnessStatistics(question);
		assertEquals(10, stats.getMean().intValue());
		assertEquals(0, stats.getStddev().intValue());
	}

	@Test
	public void getStyleStatistics() {
		Question question = new CodeQuestionBuilder().build();
		Response response = new ResponseBuilder().build();

		Sitting sitting = new SittingBuilder().withExam(
				new ExamBuilder().withQuestions(question).build()).build();
		sittingDao.save(sitting);
		sitting.assignResponse(question.getId(), response);
		sittingDao.save(sitting);

		sitting.gradeResponse(response.getId(), new GradeBuilder()
				.withNoScores().addScore("form", new Score(100)).build());
		sittingDao.save(sitting);

		QuestionStatistics stats = questionDao.getStyleStatistics(question);
		assertEquals(100, stats.getMean().intValue());
		assertEquals(0, stats.getStddev().intValue());
	}
}
