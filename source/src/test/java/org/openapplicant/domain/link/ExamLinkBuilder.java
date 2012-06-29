package org.openapplicant.domain.link;

import java.util.Arrays;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.link.DynamicExamsStrategy;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.domain.link.ExamsStrategy;
import org.openapplicant.domain.link.StaticExamsStrategy;


/**
 * Builds test ExamLink objects.
 */
public abstract class ExamLinkBuilder<T extends ExamLink> {
	
	private Company company = new CompanyBuilder().build();
	
	private String description ="foo";
	
	private ExamsStrategy strategy = new StaticExamsStrategy(Arrays.asList(new ExamBuilder().build()));
	
	public ExamLinkBuilder<T> withCompany(Company value) {
		company = value;
		return this;
	}
	
	public ExamLinkBuilder<T> withDescription(String value) {
		description = value;
		return this;
	}
	
	public ExamLinkBuilder<T> withExams(Exam...exams) {
		if(null == exams){
			exams = new Exam[]{};
		}
		strategy = new StaticExamsStrategy(Arrays.asList(exams));
		return this;
	}
	
	public ExamLinkBuilder<T> withAllCompanyExams() {
		strategy = new DynamicExamsStrategy();
		return this;
	}
	
	public T build() {
		T result = doBuild(company, strategy);
		result.setDescription(description);
		return result;
	}
	
	protected abstract T doBuild(Company company, ExamsStrategy strategy);

}
