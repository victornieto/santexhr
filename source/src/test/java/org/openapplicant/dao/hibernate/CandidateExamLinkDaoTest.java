package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICandidateExamLinkDAO;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IExamDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.domain.link.CandidateExamLinkBuilder;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.domain.link.OpenExamLink;
import org.openapplicant.domain.link.OpenExamLinkBuilder;

public class CandidateExamLinkDaoTest extends DomainObjectDAOTest<CandidateExamLink> {
	
	@Resource
	private ICandidateExamLinkDAO candidateExamLinkDao;
	
	@Resource
	private ICompanyDAO companyDao;
	
	@Resource 
	private IExamDAO examDao;
	
	@Resource
	private ICandidateDAO candidateDao;
	
	private Company savedCompany;
	
	private Candidate savedCandidate;
	
	private Exam savedExam;
	
	@Override
	public ICandidateExamLinkDAO getDomainObjectDao() {
		return candidateExamLinkDao;
	}
	
	@Override
	public CandidateExamLink newDomainObject() {
		return getExamLinkBuilder().build();
	}
	
	protected CandidateExamLinkBuilder getExamLinkBuilder() {
		return new CandidateExamLinkBuilder();
	}
	
	@Before
	public void setUp() {
		savedCompany = new CompanyBuilder().build();
		savedCompany = companyDao.save(savedCompany);
		
		savedCandidate = new CandidateBuilder()
								.withCompany(savedCompany)
								.build();
		savedCandidate = candidateDao.save(savedCandidate);
		
		savedExam = new ExamBuilder()
							.withCompany(savedCompany)
							.build();
		savedExam = examDao.save(savedExam);
	}
	
	@Test
	public void save_exam() {
		CandidateExamLink examLink = getExamLinkBuilder()
								.withExams(savedExam)
								.build();
		candidateExamLinkDao.save(examLink);
		candidateExamLinkDao.evict(examLink);
		
		CandidateExamLink found = candidateExamLinkDao.findByGuid(examLink.getGuid());
		assertEquals(1, found.getExams().size());
	}
	
	@Test
	public void save_dynamicExamLink() {
		//assertEquals(savedCompany, savedExam.getCompany());
		
		CandidateExamLink examLink = getExamLinkBuilder()
								.withCompany(savedCompany)
								.withAllCompanyExams()
								.build();
		candidateExamLinkDao.save(examLink);
		candidateExamLinkDao.evict(examLink);
		examLink = candidateExamLinkDao.find(examLink.getId());
		
		assertEquals(1, examLink.getExams().size());
		assertEquals(savedExam, examLink.getExams().get(0));
		
		examDao.save(
				new ExamBuilder()
					.withCompany(savedCompany)
					.build()
		);
		assertEquals(2, examLink.getExams().size());
	}
	
	@Test
	public void save_2links1Exam() {
		CandidateExamLink link1 = getExamLinkBuilder()
							.withExams(savedExam)
							.build();
		
		CandidateExamLink link2 = getExamLinkBuilder()
							.withExams(savedExam)
							.build();
		
		candidateExamLinkDao.save(link1);
		candidateExamLinkDao.save(link2);
		candidateExamLinkDao.evict(link1);
		candidateExamLinkDao.evict(link2);
		
		link1 = candidateExamLinkDao.find(link1.getId());
		link2 = candidateExamLinkDao.find(link2.getId());
		
		assertEquals(1, link1.getExams().size());
		assertEquals(1, link2.getExams().size());
		assertEquals(savedExam, link1.getExams().get(0));
		assertEquals(link1.getExams().get(0), link2.getExams().get(0));
	}
	
	/*
	@Test
	public void findAllByCompanyId() {
		CandidateExamLink link1 = getExamLinkBuilder()
							.withCompany(savedCompany)
							.build();
		
		CandidateExamLink link2 = getExamLinkBuilder()
							.withCompany(savedCompany)
							.build();
		
		examLinkDao.save(link1);
		examLinkDao.save(link2);
		
		List<ExamLink> links = examLinkDao.findAllByCompanyId(savedCompany.getId());
		
		assertEquals(3, links.size());
		assertTrue(links.contains(link1));
		assertTrue(links.contains(link2));
		assertTrue(links.contains(savedCompany.getLinkToAllExams()));
	}
	*/
	
	@Test
	public void findByCandidateId() {
		Candidate candidate = new CandidateBuilder().build();
		
		CandidateExamLink examLink = new CandidateExamLinkBuilder()
								.withCandidate(candidate)
								.build();
		
		candidateExamLinkDao.save(examLink);
		candidateExamLinkDao.evict(examLink);
		
		List<CandidateExamLink> found = candidateExamLinkDao.findAllByCandidateId(candidate.getId());
		assertEquals(1,found.size());
		assertEquals(found.get(0).getId(),examLink.getId());
	}
	
	/*
	@Test
	public void findAllByCompanyId_differentTypes() {
		CandidateExamLink link1 = new CandidateExamLinkBuilder()
								.withCompany(savedCompany)
								.build();
		
		OpenExamLink link2 = new OpenExamLinkBuilder()
								.withCompany(savedCompany)
								.build();
		examLinkDao.save(link1);
		examLinkDao.save(link2);
		
		List<CandidateExamLink> links = examLinkDao.findAllByCompanyId(savedCompany.getId());
		
		assertEquals(3, links.size());
		assertTrue(links.contains(link1));
		assertTrue(links.contains(link2));
		assertTrue(links.contains(savedCompany.getLinkToAllExams()));
	}
	*/
}
