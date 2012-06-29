package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.link.ExamLink;
import org.springframework.util.Assert;


/**
 * Event fired when the facilitator automatically sends an exam link to a 
 * candidate based on their resume screening score.  
 */
@Entity
public class FacilitatorSentExamLinkEvent extends CandidateWorkFlowEvent {
	
	private ExamLink examLink;
	
	/**
	 * Constructs a new event
	 * 
	 * @param candidate the candidate who was sent the exam.
	 * @param examLink the sent exam
	 */
	public FacilitatorSentExamLinkEvent(Candidate candidate, ExamLink examLink) {
		super(candidate);
		Assert.notNull(examLink);
		this.examLink = examLink;
	}
	
	private FacilitatorSentExamLinkEvent(){}
	
	/**
	 * @return the sent exam
	 */
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
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}

}
