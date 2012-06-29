package org.openapplicant.dao;

import java.util.List;

import org.openapplicant.domain.event.CandidateWorkFlowEvent;


public interface ICandidateWorkFlowEventDAO extends IDomainObjectDAO<CandidateWorkFlowEvent> {
	
	/**
	 * Retrieves all candidate work flow events for the candidate with the given id.
	 * 
	 * @param candidateId the id of the candidate who's events to retrieve.
	 * @return the work flow events
	 */
	List<CandidateWorkFlowEvent> findAllByCandidateId(Long candidateId);

}
