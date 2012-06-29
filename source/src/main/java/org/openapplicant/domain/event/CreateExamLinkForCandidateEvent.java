package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.User;
import org.openapplicant.domain.link.ExamLink;
import org.springframework.util.Assert;


/**
 * Event raised when a user create's an exam link for a candidate.
 */
@Entity
public class CreateExamLinkForCandidateEvent extends CandidateWorkFlowEvent {

	private ExamLink examLink;
	
	private User user;
	
	/**
	 * Creates a new event
	 * @param candidate the candidate that an exam link was created for
	 * @param examLink the created exam link
	 * @param user the user who created the exam link
	 */
	public CreateExamLinkForCandidateEvent(Candidate candidate, ExamLink examLink, User user) {
		super(candidate);
		Assert.notNull(examLink);
		Assert.notNull(user);
		
		this.examLink = examLink;
		this.user = user;
	}
	
	private CreateExamLinkForCandidateEvent() {}
	
	@OneToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public ExamLink getExamLink() {
		return examLink;
	}
	
	private void setExamLink(ExamLink examLink) {
		this.examLink = examLink;
	}
	
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public User getUser() {
		return user;
	}
	
	private void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}
}
