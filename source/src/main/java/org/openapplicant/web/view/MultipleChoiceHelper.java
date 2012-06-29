package org.openapplicant.web.view;

import java.util.List;

import org.openapplicant.domain.question.IQuestionVisitor;
import org.openapplicant.domain.question.MultipleChoiceQuestion;
import org.openapplicant.domain.question.Question;
import org.openapplicant.domain.question.QuestionVisitorAdapter;


public class MultipleChoiceHelper extends QuestionVisitorAdapter {
	
	/**
	 * This helper seems so incredibly unnecessary.  We just need
	 * strong types when we're sending things to the view.
	 */
	
	private MultipleChoiceQuestion question;
	
	public MultipleChoiceHelper(Question question) {
		question.accept(this);
	}
	
	public void visit(MultipleChoiceQuestion question) {
		this.question = question;
	}
	
	public List<String> getChoices() {
		return question.getChoices();
	}
	
	public Integer getAnswerIndex() {
		return question.getAnswerIndex();
	}

}