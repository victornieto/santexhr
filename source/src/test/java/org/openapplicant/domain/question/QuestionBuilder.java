package org.openapplicant.domain.question;

import java.util.UUID;

import org.openapplicant.domain.question.CodeQuestion;
import org.openapplicant.domain.question.Question;
import org.springframework.test.util.ReflectionTestUtils;


/**
 * Builds test Question objects
 */
public abstract class QuestionBuilder<T extends Question> {
	
	private Long id;
	
	private String prompt = "foo?";
		
	private Integer timeAllowed = 60;
	
	private String name = "fooQuestion";
	
	private String artifactId = UUID.randomUUID().toString();
	
	public QuestionBuilder<T> withId(Long value) {
		id = value;
		return this;
	}
	
	public QuestionBuilder<T> withPrompt(String value) {
		prompt = value;
		return this;
	}
	
	public QuestionBuilder<T> withTimeAllowed(Integer value) {
		timeAllowed = value;
		return this;
	}
	
	public QuestionBuilder<T> withName(String value) {
		name = value;
		return this;
	}
	
	public QuestionBuilder<T> withArtifactId(String value) {
		artifactId = value;
		return this;
	}

	public T build() {
		T result = doBuild();
		ReflectionTestUtils.invokeSetterMethod(result, "id", id);
		ReflectionTestUtils.invokeSetterMethod(result, "artifactId", artifactId);
		result.setPrompt(prompt);
		result.setTimeAllowed(timeAllowed);
		result.setName(name);
		return result;
	}
	
	protected abstract T doBuild();
}
