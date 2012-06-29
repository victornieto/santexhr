package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.DomainObject;
import org.springframework.util.Assert;


/**
 * Superclass for all candidate work flow related events.
 */
@Entity
@Table(name="candidate_work_flow_event")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
public abstract class CandidateWorkFlowEvent extends DomainObject {
	
	private Candidate candidate;
	
	/**
	 * Constructs a new CandidateWorkFlowEvent
	 * @param candidate the work flow candidate
	 */
	protected CandidateWorkFlowEvent(Candidate candidate) {
		Assert.notNull(candidate);
		this.candidate = candidate;
	}
	
	/**
	 * for orm tools only.  Subclasses should use the single argument
	 * constructor.
	 */
	protected CandidateWorkFlowEvent(){}
	
	/**
	 * @return the work flow candidate
	 */
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Candidate getCandidate() {
		return candidate;
	}
	
	private void setCandidate(Candidate value) {
		candidate = value;
	}
	
	/**
	 * Accepts the given visitor
	 * @param visitor the visitor to accept
	 */
	public abstract void accept(ICandidateWorkFlowEventVisitor visitor);

}
