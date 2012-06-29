package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Sitting;


/**
 * Event fired when a candidate has completed a sitting.
 */
@Entity
public class SittingCompletedEvent extends CandidateWorkFlowEvent {
	
	private Sitting sitting;
	
	public SittingCompletedEvent(Sitting sitting) {
		super(sitting.getCandidate());
		this.sitting = sitting;
	}
	
	private SittingCompletedEvent(){}
	
	@OneToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Sitting getSitting() {
		return sitting;
	}
	
	private void setSitting(Sitting value) {
		sitting = value;
	}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}

}
