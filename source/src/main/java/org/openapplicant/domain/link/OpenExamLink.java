package org.openapplicant.domain.link;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.openapplicant.domain.Company;
import org.openapplicant.policy.NeverCall;


/**
 * Models an exam link sent without a specific candidate 
 * (eg. a link posted on a job board)
 */
@Entity
public class OpenExamLink extends ExamLink {
	
	public OpenExamLink(Company company, ExamsStrategy strategy) {
		super(company, strategy);
	}
	
	@NeverCall
	OpenExamLink(){}
	
	@Override
	@Transient
	public boolean isMultiUse() {
		return true;
	}
	
	@Override
	@Transient
	public boolean isUsed() {
		return false;
	}
}
