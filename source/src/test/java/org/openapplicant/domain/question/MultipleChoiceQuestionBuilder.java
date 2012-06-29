package org.openapplicant.domain.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.openapplicant.domain.question.MultipleChoiceQuestion;

public class MultipleChoiceQuestionBuilder extends QuestionBuilder<MultipleChoiceQuestion> {

	private List<String> choices = Arrays.asList("foo1", "foo2", "foo3", "foo4");
	
	private Integer answerIndex = 2;
	
	public MultipleChoiceQuestionBuilder withChoices(List<String> value) {
		choices = value;
		return this;
	}
	
	public MultipleChoiceQuestionBuilder withAnswerIndex(Integer value) {
		answerIndex = value;
		return this;
	}
	
	public MultipleChoiceQuestion doBuild() {
		MultipleChoiceQuestion result = new MultipleChoiceQuestion();
		result.setChoicesText(choices);
		result.setAnswerIndex(answerIndex);
		return result;
	}
	
}
