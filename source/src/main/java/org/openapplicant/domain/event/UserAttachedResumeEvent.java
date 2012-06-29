package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Resume;
import org.openapplicant.domain.User;
import org.springframework.util.Assert;


/**
 * Event raised when a user attaches a resume to a candidate.
 */
@Entity
public class UserAttachedResumeEvent extends CandidateWorkFlowEvent {
	
	private Resume resume;
	
	private User user;
	
	/**
	 * @param user the use who attached the resume
	 * @param candidate the candidate who's resume was attached
	 * @param resume the attached resume
	 * @throws IllegalArgumentException if candidate or resume is null
	 */
	public UserAttachedResumeEvent(User user, Candidate candidate, Resume resume) {
		super(candidate);
		
		Assert.notNull(resume);
		Assert.notNull(user);
		
		this.resume = resume;
		this.user = user;
	}
	
	private UserAttachedResumeEvent(){}
	
	/**
	 * @return the attached resume
	 */
	@OneToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Resume getResume() {
		return resume;
	}
	
	private void setResume(Resume value) {
		resume = value;
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
