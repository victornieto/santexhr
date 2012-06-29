package org.openapplicant.domain.event;

import javax.persistence.Entity;

import org.openapplicant.domain.Candidate;


/**
 * Event capturing on candidate creation.
 */
@Entity
public class CandidateCreatedEvent extends CandidateWorkFlowEvent{
	
	/**
	 * Constructs a new event
	 * @param candidate the candidate that was created.
	 */
	public CandidateCreatedEvent(Candidate candidate) {
		super(candidate);
	}
	
	private CandidateCreatedEvent(){}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}

}
