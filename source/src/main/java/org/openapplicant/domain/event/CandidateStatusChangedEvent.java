package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.User;
import org.openapplicant.domain.Candidate.Status;
import org.springframework.util.Assert;


/**
 * Event raised when a candidate's status has changed.
 */
@Entity
public class CandidateStatusChangedEvent extends CandidateWorkFlowEvent {
	
	private Candidate.Status status;
	
	private User user;
	
	/**
	 * Constructs a new event
	 * @param candidate the candidate who's status has changed.
	 * @param user the user who changed the candidate's status
	 * @throws IllegalArgumentException if user is null
	 */
	public CandidateStatusChangedEvent(Candidate candidate, User user) {
		super(candidate);
		Assert.notNull(user);
		
		this.status = candidate.getStatus();
		this.user = user;
	}
	
	private CandidateStatusChangedEvent(){}
	
	/**
	 * @return the candidate's status at the time of the event.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return status;
	}
	
	private void setStatus(Candidate.Status status) {
		this.status = status;
	}
	
	/**
	 * @return the user who archived the candidate
	 */
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public User getUser() {
		return user;
	}
	private void setUser(User value) {
		user = value;
	}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}

}
