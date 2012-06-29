package org.openapplicant.domain.event;

import javax.persistence.Entity;

import org.openapplicant.domain.Candidate;


/**
 * Event fired when the facilitator solicits a candidate to supply
 * their missing resume.
 */
@Entity
public class FacilitatorRequestedResumeEvent extends CandidateWorkFlowEvent {

	/**
	 * Constructs an new event
	 * @param candidate the candidate who's resume was requested.
	 */
	public FacilitatorRequestedResumeEvent(Candidate candidate) {
		super(candidate);
	}
	
	private FacilitatorRequestedResumeEvent(){}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}
}
