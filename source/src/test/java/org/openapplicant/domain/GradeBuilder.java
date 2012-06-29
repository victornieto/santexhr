package org.openapplicant.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openapplicant.domain.Grade;
import org.openapplicant.domain.Score;

/**
 * Builds test Grade objects.
 */
public class GradeBuilder {
	
	private Map<String, Score> scores = new HashMap<String,Score>();
	
	public GradeBuilder() {
		scores.put("useful", new Score(90));
		scores.put("function", new Score(100));
	}
	
	public GradeBuilder withNoScores() {
		scores = new HashMap<String, Score>();
		return this;
	}
	
	public GradeBuilder addScore(String category, Score value) {
		scores.put(category, value);
		return this;
	}
	
	public Grade build() {
		Grade result = new Grade();
		for(Entry<String, Score> each : scores.entrySet()) {
			result.putScore(each.getKey(), each.getValue());
		}
		return result;
	}
}
