package org.openapplicant.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.NoteBuilder;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.ProfileBuilder;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.domain.event.AddNoteToCandidateEvent;
import org.openapplicant.domain.event.CandidateCreatedByUserEvent;
import org.openapplicant.domain.event.CandidateStatusChangedEvent;
import org.openapplicant.domain.event.CandidateWorkFlowEvent;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.domain.question.QuestionBuilder;
import org.openapplicant.service.AdminService;
import org.openapplicant.util.TestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


//============================================================================
// ADMIN MANAGER TEST
//============================================================================
@ContextConfiguration(locations="/applicationContext-test.xml")
public class AdminServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Resource 
	private AdminService adminService;

	@Test
	public void foo() {
		
	}
/*	
	@Test
	public void testComputeMatchScore() {
//		adminService.getCompanyDao().find(new Long(17));
		Candidate candidate = adminService.getCandidateDao().find(new Long(505));
		String examGuid = "8a5a54a5-0a51-4995-9d6f-0ced524da659";
	
		adminService.computeMatchScore(candidate);
		logger.info("score computed:  "+candidate.getMatchScore());
	}
	*/
	
	/*
	
	@Resource
	private ICompanyDAO companyDao;
	
	@Resource
	private ICandidateDAO candidateDao;

	private Company savedCompany;
	
	private User savedUser;
	
	private Candidate savedCandidate;
	
	@Before
	public void setUp() {
		savedCompany = companyDao.save(new CompanyBuilder().build());
		savedUser = new UserBuilder()
							.withPassword("foo")
							.build();
			
		savedUser = adminService.createUser(savedCompany.getId(), savedUser);
		
		savedCandidate = candidateDao.save(
				new CandidateBuilder()
						.withCompany(savedCompany)
						.build()
		);
	}
	
	@Test
	public void createUser() {
		// user -> company
		assertNotNull(savedUser.getId());
		assertEquals(savedUser.getCompany().getId(), savedCompany.getId());
		
		// company -> user
		savedCompany = companyDao.findByGuid(savedCompany.getGuid());
		assertEquals(1, savedCompany.getUsers().size());
		assertEquals(savedUser.getId(), savedCompany.getUsers().iterator().next().getId());
	}
	
	@Test
	public void createExamForUserCompany() {
		Exam exam = adminService.createExam(savedUser.getId());
		
		// exam -> company
		assertNotNull(exam.getId());
		assertEquals(exam.getCompany().getId(), savedCompany.getId());
		
		// company -> exam
		savedCompany = companyDao.findByGuid(savedCompany.getGuid());
		assertEquals(1, savedCompany.getUsers().size());
		assertEquals(exam.getId(), savedCompany.getExams().iterator().next().getId());
	}
	
	@Test
	public void createExamLink() {
		Exam exam = adminService.createExam(savedUser.getId());
		
		ExamLink link = adminService.createExamLink(
				savedUser.getId(), 
				"foo@gmail.com", 
				Arrays.asList(exam.getId())
		);
		
		assertNotNull(link.getEmailLocalPart());
		assertNotNull(link.getGuid());
		assertEquals(savedUser.getCompany().getId(), link.getCompany().getId());
		assertEquals(exam.getId(), link.getExams().get(0).getId());
	}
	
	@Test
	public void addQuestionToExam() {
		Exam exam = adminService.createExam(savedUser.getId());
		exam = adminService.addQuestionToExam(exam.getId(), new QuestionBuilder().build());
		
		assertEquals(1, exam.getQuestions().size());
		assertNotNull(exam.getQuestionByIndex(0).getId());
	}
	
	@Test
	public void removeQuestionFromExam() {
		Exam exam = adminService.createExam(savedUser.getId());
		exam = adminService.addQuestionToExam(exam.getId(), new QuestionBuilder().build());
		
		exam = adminService.removeQuestionFromExam(exam.getId(), exam.getQuestionByIndex(0).getId());
		
		assertEquals(0, exam.getQuestions().size());
	}
	
	@Test
	public void updateExamQuestionOrder() {
		Exam exam = adminService.createExam(savedUser.getId());
		exam =adminService.addQuestionToExam(exam.getId(), new QuestionBuilder().build());
		exam = adminService.addQuestionToExam(exam.getId(), new QuestionBuilder().build());
		
		Long q1 = exam.getQuestionByIndex(1).getId();
		Long q0 = exam.getQuestionByIndex(0).getId();
		List<Long> newQuestionOrder = Arrays.asList(q1, q0);
		
		exam = adminService.updateExamQuestionOrder(exam.getId(), newQuestionOrder);
		
		assertEquals(q1, exam.getQuestionByIndex(0).getId());
		assertEquals(q0, exam.getQuestionByIndex(1).getId());
	}
	
	@Test
	public void createCandidate() {
		Candidate candidate = adminService.createCandidate(savedUser.getId());
		assertEquals(candidate.getCompany().getId(), savedUser.getCompany().getId());
		
		List<CandidateWorkFlowEvent> events = adminService.findAllCandidateWorkFlowEventsByCandidateId(candidate.getId());
		assertTrue(events.get(0) instanceof CandidateCreatedByUserEvent);
	}
	
	@Test
	public void addNoteToCandidate() {
		savedCandidate = adminService.addNoteToCandidate(
				savedCandidate.getId(),
				new NoteBuilder()
						.withUser(savedUser)
						.withBody("foo")
						.build()
		);
		assertEquals("foo", savedCandidate.getNotes().iterator().next().getBody());
		
		List<CandidateWorkFlowEvent> events = adminService.findAllCandidateWorkFlowEventsByCandidateId(savedCandidate.getId());
		
		assertEquals(1, events.size());
		
		AddNoteToCandidateEvent event = (AddNoteToCandidateEvent) events.get(0);
		
		assertEquals("foo", event.getNote().getBody());
		assertEquals(savedUser.getId(), event.getNote().getAuthor().getId());
	}
	
	@Test
	public void updateCandidateStatus() {
		assertFalse(savedCandidate.getStatus().equals(Candidate.Status.BENCHMARKED));
		
		savedCandidate = adminService.updateCandidateStatus(savedCandidate.getId(), Candidate.Status.BENCHMARKED, savedUser.getId());
		assertEquals(Candidate.Status.BENCHMARKED, savedCandidate.getStatus());
		
		List<CandidateWorkFlowEvent> events = adminService.findAllCandidateWorkFlowEventsByCandidateId(savedCandidate.getId());
		assertEquals(1, events.size());
		assertTrue(events.get(0) instanceof CandidateArchivedEvent);
	}
	*/
}
