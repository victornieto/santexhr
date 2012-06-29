package org.openapplicant.domain.event;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CoverLetter;
import org.openapplicant.domain.Resume;


/**
 * Event fired when the facilitator receives an email from a candidate.
 */
@Entity
public class FacilitatorReceivedEmailEvent extends CandidateWorkFlowEvent {
	
	private Resume resume;
	
	private CoverLetter coverLetter;
	
	/**
	 * Constructs a new event
	 * @param candidate the candidate who sent the email
	 */
	public FacilitatorReceivedEmailEvent(Candidate candidate, Resume resume, CoverLetter coverLetter) {
		super(candidate);
		setResume(resume);
		setCoverLetter(coverLetter);
	}
	
	private FacilitatorReceivedEmailEvent(){}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	public Resume getResume() {
		return resume;
	}
	
	public void setResume(Resume resume) {
		this.resume = resume;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	public CoverLetter getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(CoverLetter coverLetter) {
		this.coverLetter = coverLetter;
	}
	
	
	
	
}
