package org.openapplicant.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICandidateWorkFlowEventDAO;
import org.openapplicant.dao.IExamDAO;
import org.openapplicant.dao.ISittingDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.SittingBuilder;
import org.openapplicant.domain.event.SittingCompletedEvent;
import org.openapplicant.domain.event.SittingCreatedEvent;
import org.openapplicant.domain.question.EssayQuestionBuilder;
import org.openapplicant.domain.question.Question;
import org.openapplicant.service.QuizService;


public class QuizServiceTest {
	
	private QuizService quizService;
	
	private IExamDAO mockExamDao;
	
	private ICandidateDAO mockCandidateDao;
	
	private ICandidateWorkFlowEventDAO mockCandidateWorkFlowEventDao;
	
	private ISittingDAO mockSittingDao;
	
	private Exam exam;
	
	private Candidate candidate;
	
	private Object[] allMocks;
	
	@Before
	public void setUp() {
		quizService = new QuizService();
		mockExamDao = createMock(IExamDAO.class);
		mockCandidateDao = createMock(ICandidateDAO.class);
		mockCandidateWorkFlowEventDao = createMock(ICandidateWorkFlowEventDAO.class);
		mockSittingDao = createMock(ISittingDAO.class);
		
		quizService.setExamDao(mockExamDao);
		quizService.setCandidateDao(mockCandidateDao);
		quizService.setCandidateWorkFlowEventDao(mockCandidateWorkFlowEventDao);
		quizService.setSittingDao(mockSittingDao);
		
		candidate = new CandidateBuilder().build();
		exam = new ExamBuilder()
						.withCompany(candidate.getCompany())
						.build();
		
		allMocks = new Object[]{
				mockCandidateDao, 
				mockExamDao, 
				mockCandidateWorkFlowEventDao,
				mockSittingDao
		};
	}
	
	@Test
	public void createSitting() {
		//expect(mockExamDao.findLatestVersionByArtifactId(exam.getArtifactId()))
			//.andReturn(exam);
		
		expect(mockExamDao.save(exam))
			.andReturn(exam);
		
		expect(mockCandidateDao.save(candidate))
			.andReturn(candidate);
		
		expect(mockCandidateWorkFlowEventDao.save(isA(SittingCreatedEvent.class)))
			.andReturn(new SittingCreatedEvent(new SittingBuilder().build()));
		
		replay(allMocks);
		
		Sitting sitting = quizService.createSitting(candidate, exam.getArtifactId());
		
		verify(allMocks);
		
		//assertTrue(sitting.getExam().isFrozen());
		assertEquals(Candidate.Status.EXAM_STARTED, candidate.getStatus());
		assertEquals(1, candidate.getSittings().size());
		assertEquals(sitting, candidate.getSittings().iterator().next());
	}
	
	@Test
	public void createSitting_retakeExam() {
		//expect(mockExamDao.findLatestVersionByArtifactId(exam.getArtifactId()))
		//	.andReturn(exam);
		
		replay(allMocks);
		
		Sitting alreadyTaken = new SittingBuilder()
									.withExam(exam)
									.withCandidate(candidate)
									.build();
		//assertTrue(exam.isFrozen());
		
		candidate.addSitting(alreadyTaken);
		
		Sitting sitting = quizService.createSitting(candidate, exam.getArtifactId());
		
		verify(allMocks);
		
		assertEquals(sitting, alreadyTaken);
	}
	
	@Test
	public void nextQuestion() {
		candidate.setStatus(Candidate.Status.EXAM_STARTED);
		Sitting sitting = new SittingBuilder()
								.withId(100L)
								.withCandidate(candidate)
								.withExam(
										new ExamBuilder()
												.withQuestions(
														new EssayQuestionBuilder()
																.withPrompt("foo")
																.build()
												)
												.build()
								)
								.build();
		
		assertTrue(sitting.hasNextQuestion());

		expect(mockCandidateDao.save(sitting.getCandidate()))
			.andReturn(sitting.getCandidate());
		expect(mockCandidateWorkFlowEventDao.save(isA(SittingCompletedEvent.class)))
			.andReturn(new SittingCompletedEvent(sitting));		
		expect(mockSittingDao.save(sitting))
			.andReturn(sitting);
		
		replay(allMocks);
		
		Question question = quizService.nextQuestion(sitting);
		
		verify(allMocks);
		
		assertEquals("foo", question.getPrompt());
		assertEquals(Candidate.Status.READY_FOR_GRADING, sitting.getCandidate().getStatus());
		assertTrue(sitting.isFinished());
	}
}
