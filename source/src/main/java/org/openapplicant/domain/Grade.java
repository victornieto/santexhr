package org.openapplicant.domain;

import static org.openapplicant.domain.Score.ZERO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.AssertTrue;
import org.hibernate.validator.Valid;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;


@Entity
public class Grade extends DomainObject {
	
	private Score score;
	
	private Map<String,Score> scores = new HashMap<String, Score>();
	
	@Valid
	@Embedded
	@AttributeOverride(name="value", column=@Column(name="score", nullable=true))
	public Score getScore() {
		return score;
	}

	@NeverCall
	public void setScore(Score score) {
		this.score = score;
	}
	
	@Transient
	public boolean isBlank() {
		return scores.size() == 0;
	}

	/**
	 * @return an unmodifiable map of scores with each score category as a key.
	 */
	@Transient
	public Map<String, Score> getScores() {
		return Collections.unmodifiableMap(scores);
	}
	
	/**
	 * For form validation
	 * @return
	 */
	@AssertTrue
	@Transient
	public boolean isScoresValid() {
		Score oneHundred = new Score(100);
		for(Score each : scores.values()) {
			if(each.greaterThan(oneHundred) || each.lessThan(ZERO)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Update the scores of this grade with scores from other
	 * @param other the grade who's scores to copy
	 */
	public void updateScores(Grade other) {
		Assert.notNull(other);
		for(Map.Entry<String, Score> each : other.getScores().entrySet()) {
			putScore(each.getKey(), each.getValue());
		}
	}
	
	/**
	 * Sets the score for a given category.
	 * @param category the grading category 
	 * @param value the score to assign
	 */
	public void putScore(String category, Score value) {
		Assert.notNull(category);
		Assert.notNull(value);
		
		scores.put(category, value);
		score = Score.average(scores.values());
	}
	
	@CollectionOfElements
	@JoinTable
	private Map<String, Score> getScoresInternal() {
		return scores;
	}
	
	private void setScoresInternal(Map<String, Score> scores) {
		if(scores == null) {
			scores = new HashMap<String, Score>();
		}
		this.scores = scores;
	}
}
