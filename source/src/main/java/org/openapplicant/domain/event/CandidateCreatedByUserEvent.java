package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.User;
import org.springframework.util.Assert;


/**
 * Event capturing candidate creation by a user
 */
@Entity
public class CandidateCreatedByUserEvent extends CandidateWorkFlowEvent {
	
	private User user;
	
	/**
	 * Constructs a new event
	 * @param candidate the candidate that was created 
	 * @param user the user who created the candidate.
	 */
	public CandidateCreatedByUserEvent(Candidate candidate, User user) {
		super(candidate);
		Assert.notNull(user);
		this.user =  user;
	}
	
	private CandidateCreatedByUserEvent() {}
	
	/**
	 * @return the user who created the candidate.  
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
