package org.openapplicant.domain;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.question.CodeQuestionBuilder;
import org.openapplicant.domain.question.EssayQuestionBuilder;
import org.openapplicant.domain.question.MultipleChoiceQuestionBuilder;
import org.openapplicant.domain.question.Question;


/**
 * Builds test Exam objects.
 */
public class ExamBuilder {
	
	private String name = "Java";
	
	private String description = "java test";
	
	private String genre = "java";
	
	private Company company = new CompanyBuilder().build();
	
	private boolean active = true;
	
	private Question[] questions = new Question[]{
			new EssayQuestionBuilder().build(),
			new CodeQuestionBuilder().build(),
			new MultipleChoiceQuestionBuilder().build()
	};
	
	public ExamBuilder withName(String value){ 
		name = value;
		return this;
	}
	
	public ExamBuilder withDescription(String value) {
		description = value;
		return this;
	}
	
	public ExamBuilder withGenre(String value) {
		genre =  value;
		return this;
	}
	
	public ExamBuilder withQuestions(Question... questions) {
		if(null == questions) {
			questions = new Question[]{};
		}
		this.questions = questions;
		return this;
	}
	
	public ExamBuilder withCompany(Company value) {
		company = value;
		return this;
	}
	
	public ExamBuilder withActive(boolean value) {
		active = value;
		return this;
	}

	public Exam build() {
		Exam exam = new Exam();
		exam.setName(name);
		exam.setDescription(description);
		exam.setGenre(genre);
		for(Question each : questions) {
			exam.addQuestion(each);
		}
		//exam.setActive(active);
		//exam.setCompany(company);
		return exam;
	}
}
