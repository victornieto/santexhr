package org.openapplicant.domain.link;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.link.ExamsStrategy;
import org.openapplicant.domain.link.OpenExamLink;

public class OpenExamLinkBuilder extends ExamLinkBuilder<OpenExamLink> {
	
	@Override
	public OpenExamLink doBuild(Company company, ExamsStrategy strategy) {
		return new OpenExamLink(company, strategy);
	}

}
