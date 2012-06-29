package org.openapplicant.domain.link;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.domain.link.ExamsStrategy;


public class CandidateExamLinkBuilder extends ExamLinkBuilder<CandidateExamLink> {
	
	private Candidate candidate = new CandidateBuilder().build();
	
	public CandidateExamLinkBuilder withCandidate(Candidate value) {
		candidate = value;
		return this;
	}
	
	@Override
	protected CandidateExamLink doBuild(Company company, ExamsStrategy strategy) {
		return new CandidateExamLink(company, candidate, strategy);
	}
}
