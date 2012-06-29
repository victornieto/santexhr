package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICandidateWorkFlowEventDAO;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IExamDAO;
import org.openapplicant.dao.ISittingDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.CoverLetterBuilder;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.GradeBuilder;
import org.openapplicant.domain.NoteBuilder;
import org.openapplicant.domain.Response;
import org.openapplicant.domain.ResponseBuilder;
import org.openapplicant.domain.ResumeBuilder;
import org.openapplicant.domain.Score;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.SittingBuilder;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.domain.event.AddNoteToCandidateEvent;
import org.openapplicant.domain.event.CandidateCreatedByUserEvent;
import org.openapplicant.domain.event.CandidateCreatedEvent;
import org.openapplicant.domain.event.CandidateStatusChangedEvent;
import org.openapplicant.domain.event.CandidateWorkFlowEvent;
import org.openapplicant.domain.event.CreateExamLinkForCandidateEvent;
import org.openapplicant.domain.event.FacilitatorReceivedEmailEvent;
import org.openapplicant.domain.event.FacilitatorRejectedResumeEvent;
import org.openapplicant.domain.event.FacilitatorRequestedResumeEvent;
import org.openapplicant.domain.event.FacilitatorSentExamLinkEvent;
import org.openapplicant.domain.event.SittingCompletedEvent;
import org.openapplicant.domain.event.SittingCreatedEvent;
import org.openapplicant.domain.event.SittingGradedEvent;
import org.openapplicant.domain.event.UserAttachedCoverLetterEvent;
import org.openapplicant.domain.event.UserAttachedResumeEvent;
import org.openapplicant.domain.event.UserSentExamLinkEvent;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.domain.link.CandidateExamLinkBuilder;
import org.openapplicant.domain.question.Question;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


@ContextConfiguration(locations="/applicationContext-test.xml")
public class CandidateWorkFlowEventDAOTest 
	extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Resource
	private ICandidateWorkFlowEventDAO candidateWorkFlowEventDao;
	
	@Resource
	private ICandidateDAO candidateDao;
	
	@Resource
	private ICompanyDAO companyDao;
	
	@Resource
	private IExamDAO examDao;
	
	@Resource 
	private ISittingDAO sittingDao;
	
	private Candidate savedCandidate;
	
	private Company savedCompany;
	
	private User savedUser;
	
	private Exam savedExam;
	
	private CandidateExamLink unsavedExamLink;
	
	@Before
	public void setUp() {
		savedUser = new UserBuilder().build();
		
		savedCompany = new CompanyBuilder()
							.withUsers(savedUser)
							.build();
		
		savedCompany = companyDao.save(savedCompany);
		
		savedExam = new ExamBuilder()
							.withCompany(savedCompany)
							.build();
		
		savedExam = examDao.save(savedExam);
		
		savedCandidate = new CandidateBuilder()
								.withCompany(savedCompany)
								.build();
		
		savedCandidate = candidateDao.save(savedCandidate);
		
		unsavedExamLink = new CandidateExamLinkBuilder()
								.withCandidate(savedCandidate)
								.withCompany(savedCandidate.getCompany())
								.withExams(savedExam)
								.build();
	}
	
	@Test
	public void save_candidateCreatedEvent() {
		CandidateWorkFlowEvent event = new CandidateCreatedEvent(savedCandidate);
		event = candidateWorkFlowEventDao.save(event);
		
		CandidateCreatedEvent found = (CandidateCreatedEvent)candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(),found.getId());
		assertEquals(savedCandidate.getId(), found.getCandidate().getId());
	}
	
	@Test
	public void save_candidateCreatedEventUser() {
		CandidateWorkFlowEvent event = new CandidateCreatedByUserEvent(savedCandidate, savedUser);
		event = candidateWorkFlowEventDao.save(event);
		
		CandidateCreatedByUserEvent found = (CandidateCreatedByUserEvent) candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(savedCandidate.getId(), found.getCandidate().getId());
		assertEquals(savedUser.getId(), found.getUser().getId());
	}
	
	@Test
	public void save_userAttachedResumeEvent() {
		CandidateWorkFlowEvent event = new UserAttachedResumeEvent(
				savedUser,
				savedCandidate,
				new ResumeBuilder().build()
		);
		event = candidateWorkFlowEventDao.save(event);
		
		UserAttachedResumeEvent found = (UserAttachedResumeEvent) candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertNotNull(found.getResume());
		assertEquals(savedCandidate, found.getCandidate());
		assertEquals(savedUser, found.getUser());
	}
	
	@Test
	public void save_userAttachedCoverLetterEvent() {
		CandidateWorkFlowEvent event = new UserAttachedCoverLetterEvent(
				savedUser,
				savedCandidate,
				new CoverLetterBuilder().build()
		);
		event = candidateWorkFlowEventDao.save(event);
		
		UserAttachedCoverLetterEvent found = (UserAttachedCoverLetterEvent) candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(found.getId(), event.getId());
		assertNotNull(found.getCoverLetter());
		assertEquals(savedCandidate, found.getCandidate());
		assertEquals(savedUser, found.getUser());
	}
	
	@Test
	public void save_addNoteToCandidateEvent() {
		CandidateWorkFlowEvent event = new AddNoteToCandidateEvent(
				savedCandidate, 
				new NoteBuilder()
						.withUser(savedUser)
						.build()
		);
		event = candidateWorkFlowEventDao.save(event);
		
		AddNoteToCandidateEvent found = (AddNoteToCandidateEvent)candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(savedUser.getId(), found.getNote().getAuthor().getId());
		assertNotNull(found.getNote().getId());
	}
	
	@Test
	public void save_candidateStatusChangedEvent() {
		savedCandidate.setStatus(Candidate.Status.BENCHMARKED);
		CandidateWorkFlowEvent event = new CandidateStatusChangedEvent(savedCandidate, savedUser);
		event = candidateWorkFlowEventDao.save(event);
		
		CandidateStatusChangedEvent found = (CandidateStatusChangedEvent)candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(Candidate.Status.BENCHMARKED, found.getStatus());
		assertEquals(savedUser.getId(), found.getUser().getId());
	}
	
	@Test
	public void save_createExamLinkForCandidateEvent() {
		
		CandidateWorkFlowEvent event = 
			new CreateExamLinkForCandidateEvent(savedCandidate, unsavedExamLink, savedUser);
		
		event = candidateWorkFlowEventDao.save(event);
		
		CreateExamLinkForCandidateEvent found = 
			(CreateExamLinkForCandidateEvent)candidateWorkFlowEventDao.findByGuid(event.getGuid());
		
		assertEquals(event.getId(), found.getId());
		assertEquals(savedCandidate, found.getCandidate());
		assertEquals(unsavedExamLink.getGuid(), found.getExamLink().getGuid());
		assertEquals(savedExam, unsavedExamLink.getExams().get(0));
		assertEquals(savedUser, found.getUser());
	}
	
	@Test
	public void save_facilitatorSentExamEvent() {
		CandidateWorkFlowEvent event = new FacilitatorSentExamLinkEvent(savedCandidate, unsavedExamLink);
		event = candidateWorkFlowEventDao.save(event);
		
		FacilitatorSentExamLinkEvent found = (FacilitatorSentExamLinkEvent)candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(unsavedExamLink.getGuid(), found.getExamLink().getGuid());
	}
	
	@Test
	public void save_facilitatorReceivedEmailEvent() {
		CandidateWorkFlowEvent event = new FacilitatorReceivedEmailEvent(savedCandidate,null,null);
		event = candidateWorkFlowEventDao.save(event);
		
		FacilitatorReceivedEmailEvent found = (FacilitatorReceivedEmailEvent) candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(savedCandidate.getId(), found.getCandidate().getId());
	}
	
	@Test
	public void save_sittingGradedEvent() {
		Sitting sitting = new SittingBuilder()
								.withCandidate(savedCandidate)
								.withExam(savedExam)
								.build();
		
		Question question = sitting.advanceToNextQuestion();
		Response response = new ResponseBuilder().build();
		sitting.assignResponse(question.getId(), response);
		
		sitting = sittingDao.save(sitting);
		sitting.gradeResponse(
				response.getId(), 
				new GradeBuilder()
					.withNoScores()
					.addScore("form", new Score(100))
					.build()
		);
		
		Score score = sitting.getScore();
		
		CandidateWorkFlowEvent event = new SittingGradedEvent(savedUser, sitting);
		event = candidateWorkFlowEventDao.save(event);
		
		SittingGradedEvent found = (SittingGradedEvent) candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(savedExam.getId(), found.getSitting().getExam().getId());
		assertEquals(savedUser.getId(), found.getUser().getId());
		assertEquals(sitting.getId(), found.getSitting().getId());
		assertEquals(score, found.getSittingScore());
		
		// assert getSittingScore() is not aliased to sitting
		found.getSitting().gradeResponse(
				response.getId(), 
				new GradeBuilder()
						.withNoScores()
						.addScore("form", score.add(new Score(50)))
						.build()
		);
		assertEquals(score, found.getSittingScore());
	}
	
	@Test
	public void save_sittingCreatedEvent() {
		Sitting sitting = new SittingBuilder()
								.withCandidate(savedCandidate)
								.withExam(savedExam)
								.build();
		CandidateWorkFlowEvent event = new SittingCreatedEvent(sitting);
		event = candidateWorkFlowEventDao.save(event);
		
		SittingCreatedEvent found = (SittingCreatedEvent)candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(savedExam.getId(), found.getSitting().getExam().getId());
		assertEquals(savedCandidate.getId(), found.getCandidate().getId());
	}
	
	@Test
	public void save_sittingCompletedEvent() {
		Sitting sitting = new SittingBuilder()
								.withCandidate(savedCandidate)
								.withExam(savedExam)
								.build();
		SittingCompletedEvent event = new SittingCompletedEvent(sitting);
		candidateWorkFlowEventDao.save(event);
		candidateWorkFlowEventDao.evict(event);
		
		SittingCompletedEvent found = (SittingCompletedEvent)candidateWorkFlowEventDao.find(event.getId());
		assertEquals(event.getId(), found.getId());
		assertEquals(sitting.getId(), found.getSitting().getId());
		assertEquals(savedExam.getId(), found.getSitting().getExam().getId());
		assertEquals(savedCandidate.getId(), found.getCandidate().getId());
	}
	
	@Test
	public void save_facilitatorRejectedResumeEvent() {
		CandidateWorkFlowEvent event = new FacilitatorRejectedResumeEvent(savedCandidate);
		event = candidateWorkFlowEventDao.save(event);
		
		FacilitatorRejectedResumeEvent found = (FacilitatorRejectedResumeEvent) candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(savedCandidate.getId(), found.getCandidate().getId());
	}
	
	@Test
	public void save_facilitatorRequestedResumeEvent() {
		CandidateWorkFlowEvent event = new FacilitatorRequestedResumeEvent(savedCandidate);
		event = candidateWorkFlowEventDao.save(event);
		
		FacilitatorRequestedResumeEvent found = (FacilitatorRequestedResumeEvent) candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(savedCandidate.getId(), found.getCandidate().getId());
	}
	
	@Test
	public void save_userSentExamLinkEvent() {
		CandidateWorkFlowEvent event = new UserSentExamLinkEvent(savedUser, unsavedExamLink);
		event = candidateWorkFlowEventDao.save(event);
		
		UserSentExamLinkEvent found = (UserSentExamLinkEvent) candidateWorkFlowEventDao.findByGuid(event.getGuid());
		assertEquals(event.getId(), found.getId());
		assertEquals(unsavedExamLink.getCandidate().getId(), found.getCandidate().getId());
		assertEquals(savedUser.getId(), found.getUser().getId());
		assertEquals(unsavedExamLink.getGuid(), found.getExamLink().getGuid());
	}
	
	@Test
	public void findAllByCandidateId() {
		candidateWorkFlowEventDao.save(new CandidateCreatedEvent(savedCandidate));
		candidateWorkFlowEventDao.save(new FacilitatorRequestedResumeEvent(savedCandidate));
	
		List<CandidateWorkFlowEvent> events = candidateWorkFlowEventDao.findAllByCandidateId(savedCandidate.getId());
		assertEquals(2, events.size());
	}
}
