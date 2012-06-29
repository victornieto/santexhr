package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Sitting;


/**
 * Event fired when a candidate begins a sitting
 */
@Entity
public class SittingCreatedEvent extends CandidateWorkFlowEvent {
	
	private Sitting sitting;
	
	/**
	 * Constructs a new event
	 * @param sitting the exam sitting.
	 */
	public SittingCreatedEvent(Sitting sitting) {
		super(sitting.getCandidate());
		this.sitting = sitting;
	}
	
	private SittingCreatedEvent(){}
	
	/**
	 * @return the candidate's sitting
	 */
	@OneToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)	
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Sitting getSitting() {
		return sitting;
	}
	
	private void setSitting(Sitting sitting) {
		this.sitting = sitting;
	}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}
}
