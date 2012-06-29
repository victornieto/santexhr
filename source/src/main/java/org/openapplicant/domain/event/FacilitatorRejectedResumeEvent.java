package org.openapplicant.domain.event;

import javax.persistence.Entity;

import org.openapplicant.domain.Candidate;


/**
 * Event fired when a user's resume is rejected by the facilitator.
 */
@Entity
public class FacilitatorRejectedResumeEvent extends CandidateWorkFlowEvent {
	
	/**
	 * Constructs a new event
	 * @param candidate the candidate who's resume was rejected.
	 */
	public FacilitatorRejectedResumeEvent(Candidate candidate) {
		super(candidate);
	}
	
	private FacilitatorRejectedResumeEvent(){}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}

}
