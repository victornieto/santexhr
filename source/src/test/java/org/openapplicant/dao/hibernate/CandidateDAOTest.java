package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.openapplicant.util.TestUtils.collectIds;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IFileAttachmentDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.CoverLetter;
import org.openapplicant.domain.CoverLetterBuilder;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.NameBuilder;
import org.openapplicant.domain.NoteBuilder;
import org.openapplicant.domain.PhoneNumber;
import org.openapplicant.domain.PhoneNumberBuilder;
import org.openapplicant.domain.Resume;
import org.openapplicant.domain.ResumeBuilder;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.util.Pagination;
import org.openapplicant.util.TestUtils;
import org.springframework.dao.DataRetrievalFailureException;


public class CandidateDAOTest extends DomainObjectDAOTest<Candidate> {
	
	@Resource
	private ICandidateDAO candidateDao;
	
	@Resource 
	private ICompanyDAO companyDao;
	
	@Resource
	private IFileAttachmentDAO fileAttachmentDao;
	
	private Company savedCompany;
	
	private User savedUser;
	
	private Pagination page1With50PerPage = Pagination.oneBased().perPage(50).forPage(1);
	
	@Override 
	public Candidate newDomainObject() {
		return new CandidateBuilder().build();
	}
	
	@Override
	public ICandidateDAO getDomainObjectDao() {
		return candidateDao;
	}
	
	@Before
	public void setUp() {
		savedCompany = new CompanyBuilder()
							.withUsers(new UserBuilder().build())
							.build();
		
		savedCompany = companyDao.save(savedCompany);
		savedUser = savedCompany.getUsers().iterator().next();
	}
	
	@Test
	public void save_name() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.withName(new NameBuilder().build())
									.build();
		
		candidate = candidateDao.save(candidate);
		
		Candidate found = candidateDao.findByGuid(candidate.getGuid());
		assertEquals(found.getName(), candidate.getName());
	}
	
	@Test
	public void save_addNote() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.withNotes(
											new NoteBuilder()
													.withUser(savedUser)
													.build()
									)
									.build();
		
		candidate = candidateDao.save(candidate);

		candidate = candidateDao.findByGuid(candidate.getGuid());
		assertEquals(1, candidate.getNotes().size());
	}
	
	@Test
	public void save_phoneNumber() {
		PhoneNumber phoneNumber = new PhoneNumberBuilder().build();
		Candidate candidate = new CandidateBuilder()
									.withWorkPhoneNumber(phoneNumber)
									.build();
			
		candidate = candidateDao.save(candidate);
		candidate = candidateDao.findByGuid(candidate.getGuid());
		
		assertEquals(phoneNumber, candidate.getWorkPhoneNumber());
	}
	
	@Test
	public void save_blankPhoneNumber() {
		Candidate candidate = new CandidateBuilder()
										.withCellPhoneNumber(null)
										.build();
		candidate = candidateDao.save(candidate);
		candidate = candidateDao.findByGuid(candidate.getGuid());
		
		assertTrue(candidate.getCellPhoneNumber().isBlank());
	}
	
	@Test
	public void save_addResume() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.withResume(new ResumeBuilder().build())
									.build();
		
		candidate = candidateDao.save(candidate);
		Resume resume = candidate.getResume();
		
		assertNotNull(resume.getId());
		
		candidate = candidateDao.findByGuid(candidate.getGuid());
		assertEquals(resume.getId(),candidate.getResume().getId());
	}
	
	@Test
	public void save_removeResume() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.withResume(new ResumeBuilder().build())
									.build();
		candidate = candidateDao.save(candidate);
		Resume resume = candidate.getResume();
		
		candidate.setResume(null);
		candidateDao.save(candidate);
		
		assertNotNull(fileAttachmentDao.findByGuid(resume.getGuid()));
		
		candidate = candidateDao.findByGuid(candidate.getGuid());
		assertNull(candidate.getResume());
	}
	
	@Test
	public void save_addCoverLetter() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.withCoverLetter(new CoverLetterBuilder().build())
									.build();
		
		candidate = candidateDao.save(candidate);
		CoverLetter coverLetter = candidate.getCoverLetter();
		
		assertNotNull(coverLetter.getId());
		
		candidate = candidateDao.findByGuid(candidate.getGuid());
		assertEquals(coverLetter.getId(), candidate.getCoverLetter().getId());
	}
	
	@Test
	public void save_removeCoverLetter() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.withCoverLetter(new CoverLetterBuilder().build())
									.build();
		
		candidate = candidateDao.save(candidate);
		CoverLetter coverLetter = candidate.getCoverLetter();
		
		candidate.setCoverLetter(null);
		candidateDao.save(candidate);
		
		assertNotNull(fileAttachmentDao.findByGuid(coverLetter.getGuid()));
		
		candidate = candidateDao.findByGuid(candidate.getGuid());
		assertNull(candidate.getCoverLetter());
	}
	
	@Test
	public void findAllByCompanyId() {
		Candidate candidate1 = new CandidateBuilder()
										.withCompany(savedCompany)
										.build();
	
		Candidate candidate2 = new CandidateBuilder()
										.withCompany(savedCompany)
										.build();
		
		candidateDao.save(candidate1);
		candidateDao.save(candidate2);
		
		Long companyId = savedCompany.getId();
		
		List<Candidate> found = candidateDao.findAllByCompanyId(companyId, page1With50PerPage);
		assertEquals(2, found.size());
	}
	
	@Test
	public void findAllByCompanyIdAndStatus() {
		
		Candidate notTestedCandidate = new CandidateBuilder()
											.withCompany(savedCompany)
											.withStatus(Candidate.Status.NOT_TESTED)
											.build();
		
		Candidate hiredCandidate = new CandidateBuilder()
											.withCompany(savedCompany)
											.withStatus(Candidate.Status.HIRED)
											.build();
		
		candidateDao.save(notTestedCandidate);
		candidateDao.save(hiredCandidate);
		
		List<Candidate> result = candidateDao.findAllByCompanyIdAndStatus(
				savedCompany.getId(), 
				Candidate.Status.NOT_TESTED,
				page1With50PerPage
		);
		
		assertEquals(1, result.size());
		assertEquals(notTestedCandidate.getId(), result.get(0).getId());
		
		result = candidateDao.findAllByCompanyIdAndStatus(
				savedCompany.getId(),
				Candidate.Status.HIRED,
				page1With50PerPage
		);
		assertEquals(1, result.size());
		assertEquals(hiredCandidate.getId(), result.get(0).getId());
	}
	
	@Test
	public void findAllActiveCandidatesByCompanyId() {
		Candidate notTestedCandidate = new CandidateBuilder()
											.withCompany(savedCompany)
											.withStatus(Candidate.Status.NOT_TESTED)
											.build();
		Candidate sentExamCandidate = new CandidateBuilder()
											.withCompany(savedCompany)
											.withStatus(Candidate.Status.SENT_EXAM)
											.build();
		Candidate archivedCandidate = new CandidateBuilder()
											.withCompany(savedCompany)
											.withStatus(Candidate.Status.HIRED)
											.build();
		candidateDao.save(notTestedCandidate);
		candidateDao.save(sentExamCandidate);
		candidateDao.save(archivedCandidate);
		
		List<Candidate> result = 
			candidateDao.findAllActiveCandidatesByCompanyId(savedCompany.getId(), page1With50PerPage);
		
		assertEquals(2, result.size());
		assertTrue(result.contains(notTestedCandidate));
		assertTrue(result.contains(sentExamCandidate));
		assertFalse(result.contains(archivedCandidate));
	}
	
	@Test
	public void findAllArchivedCandidatesByCompanyid() {
		Candidate archivedCandidate1 = new CandidateBuilder()
											.withStatus(Candidate.Status.HIRED)
											.withCompany(savedCompany)
											.build();
		Candidate archivedCandidate2 = new CandidateBuilder()
											.withStatus(Candidate.Status.BENCHMARKED)
											.withCompany(savedCompany)
											.build();
		Candidate activeCandidate = new CandidateBuilder()
											.withStatus(Candidate.Status.READY_FOR_GRADING)
											.withCompany(savedCompany)
											.build();
		candidateDao.save(archivedCandidate1);
		candidateDao.save(archivedCandidate2);
		candidateDao.save(activeCandidate);
		
		List<Candidate> result = 
			candidateDao.findAllArchivedCandidatesByCompanyId(savedCompany.getId(), page1With50PerPage);
	
		assertEquals(2, result.size());
		assertTrue(result.contains(archivedCandidate1));
		assertTrue(result.contains(archivedCandidate2));
		assertFalse(result.contains(activeCandidate));
	}
	
	@Test
	public void findByEmailOrNull() {
		String email = TestUtils.uniqueEmail();
		
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.withEmail(email)
									.build();
			
		candidate = candidateDao.save(candidate);
		
		Candidate found = candidateDao.findByEmailOrNull(email);
		
		assertEquals(found.getId(), candidate.getId());
	}
	
	@Test
	public void findByEmailOrNull_failed() {
		Candidate result = candidateDao.findByEmailOrNull(TestUtils.uniqueEmail());
		assertNull(result);
	}
	
	@Test
	public void findByEmailAndCompanyIdOrNull() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.build();
			
		candidate = candidateDao.save(candidate);
		
		Candidate found = candidateDao.findByEmailAndCompanyIdOrNull(
				candidate.getEmail(), 
				savedCompany.getId()
		);
		assertEquals(candidate.getId(), found.getId());
		
		assertNull(candidateDao.findByEmailAndCompanyIdOrNull(TestUtils.uniqueEmail(), savedCompany.getId()));
	}
	
	@Test
	public void beforeSave_completeFirstName() {
		Candidate candidate = new CandidateBuilder()
										.withStatus(Candidate.Status.INCOMPLETE)
										.withName(
												new NameBuilder()
													.withFirst("")
													.withMiddle("")
													.withLast("")
													.build()
										)
										.build();
		candidate = candidateDao.save(candidate);
		
		assertEquals(Candidate.Status.INCOMPLETE, candidate.getStatus());
		
		candidate.getName().setFirst("Madonna");
		candidate = candidateDao.save(candidate);
		
		assertTrue(Candidate.Status.INCOMPLETE != candidate.getStatus());
	}
	
	@Test
	public void beforeSave_completeLastName() {
		Candidate candidate = new CandidateBuilder()
									.withStatus(Candidate.Status.INCOMPLETE)
									.withName(
											new NameBuilder()
													.withFirst("")
													.withMiddle("")
													.withLast("")
													.build()
									)
									.build();
		candidate = candidateDao.save(candidate);
		
		assertEquals(Candidate.Status.INCOMPLETE, candidate.getStatus());
		
		candidate.getName().setLast("Prince");
		candidate = candidateDao.save(candidate);
		
		assertTrue(Candidate.Status.INCOMPLETE != candidate.getStatus());
	}
	
	
	@Test
	public void testFindByDate() {
		

		Company company = new CompanyBuilder().build();
		
		Candidate candidate1 = new CandidateBuilder()
			.withName(new Name("Candidate1"))
			.withEmail("test@mail.com")
			.withCompany(company)
			.build();
		Candidate candidate2 = new CandidateBuilder()
			.withName(new Name("Candidate2"))
			.withCompany(company)
			.build();
		Candidate candidate3 = new CandidateBuilder()
			.withName(new Name("Candidate3"))
			.withCompany(company)
			.build();

		
		companyDao.save(company);
		candidateDao.save(candidate1);
		candidateDao.save(candidate2);
		candidateDao.save(candidate3);
		
		
		GregorianCalendar start = new GregorianCalendar();
		start.add(Calendar.HOUR_OF_DAY, -24);
		GregorianCalendar end   = new GregorianCalendar();
		
		List<Candidate> results = candidateDao.findByCompanyIDandDateRange(company.getId(), start, end);
		assertEquals(3,results.size());
		
		
	}
	
}