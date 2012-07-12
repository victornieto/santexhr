package org.openapplicant.domain.event;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.DomainObject;
import org.springframework.util.Assert;

import java.util.Date;


/**
 * Superclass for all candidate work flow related events.
 */
@Entity
@Table(name="candidate_work_flow_event")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
public abstract class CandidateWorkFlowEvent extends DomainObject {
	
	private Candidate candidate;
    private Date date;
	
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

    @Column(nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
	
	/**
	 * Accepts the given visitor
	 * @param visitor the visitor to accept
	 */
	public abstract void accept(ICandidateWorkFlowEventVisitor visitor);

    @Override
    public void beforeSave() {
        super.beforeSave();
        date = new Date();
    }
}
