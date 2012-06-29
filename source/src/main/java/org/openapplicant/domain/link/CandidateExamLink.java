package org.openapplicant.domain.link;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Company;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;


/**
 * Models an exam link sent to a candidate.
 */
@Entity
public class CandidateExamLink extends ExamLink {
	
	private Candidate candidate;
	
	// FIXME: company param is redundant
	public CandidateExamLink(Company company, Candidate candidate, ExamsStrategy strategy) {
		super(company, strategy);
		
		Assert.notNull(candidate);
		this.candidate = candidate;
	}
	
	@NeverCall
	CandidateExamLink(){}
	
	@ManyToOne(
			cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Candidate getCandidate() {
		return candidate;
	}
	
	private void setCandidate(Candidate value) {
		candidate = value;
	}
}
