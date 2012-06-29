package org.openapplicant.domain.question;

import org.openapplicant.domain.question.EssayQuestion;

public class EssayQuestionBuilder extends QuestionBuilder<EssayQuestion> {

	private String answer = "foo!";
	
	public EssayQuestionBuilder withAnswer(String value) {
		answer = value;
		return this;
	}
	
	public EssayQuestion doBuild() {
		EssayQuestion result = new EssayQuestion();
		result.setAnswer(answer);
		return result;
	}
	
}
