package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.User;
import org.openapplicant.domain.link.CandidateExamLink;
import org.springframework.util.Assert;


/**
 * Event fired when a User sends an exam link to a candidate.
 */
@Entity
public class UserSentExamLinkEvent extends CandidateWorkFlowEvent {

	private CandidateExamLink examLink;
	
	private User user;
	
	/**
	 * Constructs a new event
	 * @param user the user who sent the exam link
	 * @param examLink the link that was sent
	 */
	public UserSentExamLinkEvent(User user, CandidateExamLink examLink) {
		super(examLink.getCandidate());
		
		Assert.notNull(user);
		
		this.examLink = examLink;
		this.user = user;
	}
	
	private UserSentExamLinkEvent(){}
	
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
	
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public CandidateExamLink getExamLink() {
		return examLink;
	}
	
	private void setExamLink(CandidateExamLink value) {
		examLink = value;
	}

	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}
}
