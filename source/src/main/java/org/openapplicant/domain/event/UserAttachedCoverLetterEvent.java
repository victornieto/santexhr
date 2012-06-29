package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CoverLetter;
import org.openapplicant.domain.User;
import org.springframework.util.Assert;


/**
 * Event raised when a user attaches a cover letter to a candidate.
 */
@Entity
public class UserAttachedCoverLetterEvent extends CandidateWorkFlowEvent {

	private CoverLetter coverLetter;
	
	private User user;
	
	/**
	 * @param user the user who attached the cover letter
	 * @param candidate the candidate who's cover letter was attached
	 * @param coverLetter the attached coverLetter
	 * @throws IllegalArgumentException if candidate or cover letter is null
	 */
	public UserAttachedCoverLetterEvent(User user, Candidate candidate, CoverLetter coverLetter) {
		super(candidate);
		Assert.notNull(coverLetter);
		Assert.notNull(user);
		
		this.coverLetter = coverLetter;
		this.user = user;
	}
	
	private UserAttachedCoverLetterEvent() {}
	
	/**
	 * @return the attached cover letter
	 */
	@OneToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public CoverLetter getCoverLetter() {
		return coverLetter;
	}
	
	private void setCoverLetter(CoverLetter value) {
		coverLetter = value;
	}
	
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
