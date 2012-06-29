package org.openapplicant.domain;

import org.openapplicant.domain.Grade;
import org.openapplicant.domain.Response;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Builds Response test objects.
 */
public class ResponseBuilder {
	
	private Long id = null;
	
	private long lineCount = 20L;
	
	private Grade grade = new GradeBuilder().build();
	
	private String content = "foo foo foo";
	
	private String keypressEvents = "foo foo foo";
	
	private String focusEvents = "foo";
	
	private long loadTimeStamp = System.currentTimeMillis();
	
	public ResponseBuilder withLineCount(long value) {
		lineCount = value;
		return this;
	}
	
	public ResponseBuilder withId(Long value) {
		id = value;
		return this;
	}
	
	public ResponseBuilder withGrade(Grade value) {
		grade = value;
		return this;
	}
	
	public ResponseBuilder withGradeOfZero() {
		grade = new GradeBuilder().withNoScores().build();
		return this;
	}
	
	public ResponseBuilder withContent(String value) {
		content = value;
		return this;
	}
	
	public ResponseBuilder withKeypressEvents(String value) {
		keypressEvents = value;
		return this;
	}
	
	public ResponseBuilder withFocusEvents(String value) {
		focusEvents = value;
		return this;
	}
	
	public ResponseBuilder withLoadTimeStamp(long value) {
		loadTimeStamp = value;
		return this;
	}
	
	public Response build() {
		Response result = new Response();
		ReflectionTestUtils.invokeSetterMethod(result, "id", id);
		result.setLineCount(lineCount);
		result.getGrade().updateScores(grade);
		result.setContent(content);
		result.setKeypressEvents(keypressEvents);
		result.setFocusEvents(focusEvents);
		result.setLoadTimestamp(loadTimeStamp);
		//result.setQuestion(question);
		return result;
	}

}
