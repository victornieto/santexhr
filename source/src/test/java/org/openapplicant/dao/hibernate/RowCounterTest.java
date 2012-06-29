package org.openapplicant.dao.hibernate;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IRowCounter;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


@ContextConfiguration(locations="/applicationContext-test.xml")
public class RowCounterTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Resource
	private ICandidateDAO candidateDao;
	
	@Resource
	private ICompanyDAO companyDao;
	
	@Resource
	private IRowCounter rowCounter;
	
	@Resource
	private SessionFactory sessionFactory;
	
	private Company savedCompany;
	
	@Before
	public void setUp() {
		savedCompany = new CompanyBuilder().build();
		companyDao.save(savedCompany);
	}
	
	@Test
	public void isUnique_notSaved() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.build();
		assertTrue(rowCounter.isUnique(candidate, Arrays.asList("email","company.id")));
	}
	
	@Test
	public void isUnique_saved() {
		Candidate candidate = new CandidateBuilder()
									.withCompany(savedCompany)
									.build();
		candidateDao.save(candidate);
	
		sessionFactory.getCurrentSession().evict(candidate);
		candidate = candidateDao.find(candidate.getId());
		assertTrue(rowCounter.isUnique(candidate, Arrays.asList("email","company.id")));
	}
	
	@Test
	public void isUnique_notUnique() {
		Candidate candidate1 = new CandidateBuilder()
									.withCompany(savedCompany)
									.build();
		Candidate candidate2 = new CandidateBuilder()
									.withCompany(savedCompany)
									.build();
		candidateDao.save(candidate1);
		candidateDao.save(candidate2);
		
		sessionFactory.getCurrentSession().evict(candidate1);
		candidate1 = candidateDao.find(candidate1.getId());
		candidate1.setEmail(candidate2.getEmail());
		assertFalse(rowCounter.isUnique(candidate1, Arrays.asList("email", "company.id")));
	}

}
