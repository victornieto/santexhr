package org.openapplicant.domain.event;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Score;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.User;
import org.springframework.util.Assert;


/**
 * Event fired when a user grades a candidate's exam.
 */
@Entity
public class SittingGradedEvent extends CandidateWorkFlowEvent {
	
	private Sitting sitting;
	
	private User user;
	
	private Score sittingScore;
	
	/**
	 * Constructs a new event
	 * @param user the user who graded the exam
	 * @param sitting the sitting that was graded
	 */
	public SittingGradedEvent(User user, Sitting sitting) {
		super(sitting.getCandidate());
		
		Assert.notNull(user);
		Assert.notNull(sitting.getScore());
		
		this.sitting = sitting;
		this.user = user;
		this.sittingScore = sitting.getScore();
	}
	
	private SittingGradedEvent(){}
	
	/**
	 * @return the exam that was graded.
	 */
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Sitting getSitting() {
		return sitting;
	}
	
	private void setSitting(Sitting sitting) {
		this.sitting = sitting;
	}
	
	/**
	 * @return the user who graded the exam
	 */
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public User getUser() {
		return user;
	}
	
	private void setUser(User value) {
		user = value;
	}
	
	@Embedded
	@AttributeOverride(name="value", column=@Column(nullable=true, name="sitting_score"))
	public Score getSittingScore() {
		return sittingScore;
	}
	
	private void setSittingScore(Score value) {
		sittingScore = value;
	}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}

}
