package org.openapplicant.domain.question;

import org.openapplicant.domain.question.CodeQuestion;

public class CodeQuestionBuilder extends QuestionBuilder<CodeQuestion> {

	private String answer = "foo!";
	
	public CodeQuestionBuilder withAnswer(String value) {
		answer = value;
		return this;
	}
	
	public CodeQuestion doBuild() {
		CodeQuestion result = new CodeQuestion();
		result.setAnswer(answer);
		return result;
	}
	
}
