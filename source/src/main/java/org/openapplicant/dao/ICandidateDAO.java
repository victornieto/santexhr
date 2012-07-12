package org.openapplicant.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.PropertyCandidateSearch;
import org.openapplicant.domain.SimpleStringCandidateSearch;
import org.openapplicant.domain.User;
import org.openapplicant.util.Pagination;
import org.springframework.dao.DataRetrievalFailureException;


public interface ICandidateDAO extends IDomainObjectDAO<Candidate> {
	
	/**
	 * Finds a candidate with the given email address, returning null
	 * if none found.
	 * 
	 * @param email the candidate's email address
	 * @return the candidate with the given email or null
	 */
	Candidate findByEmailOrNull(String email);
	
	/**
	 * Finds a candidate with the given email address belonging to a 
	 * company with the given companyId, returning null if not found.
	 * 
	 * @param email the candidate's email address
	 * @param companyId the id of the candidate's company
	 * @return the retrieved candidate or null
	 */
	Candidate findByEmailAndCompanyIdOrNull(String email, Long companyId);
	
	/**
	 * Finds all candidates for the given company id.
	 * 
	 * @param companyId the id of the company who's candidates to find
	 * @param pagination the pagination to apply
	 * @return a list of the company's candidates
	 */
	List<Candidate> findAllByCompanyId(Long companyId, Pagination pagination);
	
	/**
	 * Finds all candidates for a given company with a given status
	 * 
	 * @param companyId
	 * @param status true if the candidate status is active.
	 * @param status the status to search for
	 * @return a list of the company's candidates
	 */
	List<Candidate> findAllByCompanyIdAndStatus(Long companyId, Candidate.Status status, Pagination pagination);
	
	/**
	 * Finds all active candidates for a given company.
	 * 
	 * @param companyId
	 * @param pagination the pagination to apply
	 * @return a list of the company's active candidates
	 */
	List<Candidate> findAllActiveCandidatesByCompanyId(long companyId, Pagination pagination);
	
	/**
	 * Finds all archived candidates for a given company.
	 * 
	 * @param companyId
	 * @param pagination the pagination to apply
	 * @return a list of the company's archived candidates
	 */
	List<Candidate> findAllArchivedCandidatesByCompanyId(long companyId, Pagination pagination);
	
	/**
	 * Finds all candidates for a company with the given id.
	 * 
	 * @param searchString the criteria to search for
	 * @param pagination the pagination to apply
	 * @return a list of the company's candidates
	 */
	List<Candidate> performSearch(SimpleStringCandidateSearch searchString, Pagination pagination);
	
	/**
	 * Finds all candidates for a company with the given id.
	 * 
	 * @param propertySearch the criteria to search for
	 * @param pagination the pagination to apply
	 * @return a list of the company's candidates
	 */
	List<Candidate> performSearch(PropertyCandidateSearch propertySearch, Pagination pagination);
	
	
	/**
	 * Finds all candidates created between the starting and ending dates for the specified company
	 * 
	 * @param company 
	 * @param startDate starting date
	 * @param endDate end date
	 * @return a list of the candidates who meet the search criteria
	 */
	List<Candidate> findByCompanyIDandDateRange(Long companyId, Calendar startDate, Calendar endDate);
	
	/**
	 * 
	 */
	Map<String, Integer> findStatusCountsByCompanyId(Long companyId);

    /**
     * Finds all candidates registered by the given user
     * @param user the user for which his candidates must be found
     * @return a list of candidates registered by the specified user
     */
    List<Candidate> findByUser(User user);
}
