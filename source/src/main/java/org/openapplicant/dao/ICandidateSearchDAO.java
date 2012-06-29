package org.openapplicant.dao;

import java.util.List;

import org.openapplicant.domain.CandidateSearch;
import org.openapplicant.util.Pagination;


public interface ICandidateSearchDAO extends IDomainObjectDAO<CandidateSearch> {
	
	/**
	 * Retrieves the most recently entered searches for the given user
	 * 
	 * @param userId the id of the user who's searches to find
	 * @param pagination the pagination to apply
	 * @return a list of recent searches
	 */
	List<CandidateSearch> findAllByUserId(Long userId, Pagination pagination);

}
