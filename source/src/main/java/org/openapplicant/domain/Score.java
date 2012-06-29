package org.openapplicant.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.NotNull;

/**
 * Immutable value object that encapsulates scale and rounding mode for
 * grade scores.
 */
@Embeddable
public class Score implements Comparable<Score> {
	
	private static final int SCALE = 1;
	
	private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
	
	public static final Score ZERO = new Score(new BigDecimal("0"));
	
	private BigDecimal value;
	
	public Score(int value) {
		this(new BigDecimal(value));
	}
	
	public Score(double value) {
		this(new BigDecimal(value));
	}
	
	public Score(Double value) {
	    	this(new BigDecimal(value));
	}
	
	public Score(BigDecimal value) {
		this.value = value.setScale(SCALE, ROUNDING_MODE);
	}
	
	private Score() {} 
	
	public static Score average(Collection<Score> scores) {
		if(scores.size() == 0) {
			return Score.ZERO;
		}
		Score total = Score.ZERO;
		for(Score each : scores) {
			total = total.add(each);
		}
		return total.divide(scores.size());
	}
	
	@Column(name="score", nullable=false)
	@NotNull
	public BigDecimal getValue() {
		return value;
	}
	
	private void setValue(BigDecimal value) {
		this.value = value.setScale(SCALE, ROUNDING_MODE);
	}
	
	public Score add(Score augend) {
		return new Score(value.add(augend.value));
	}
	
	public Score divide(int divisor) {
		return new Score(value.divide(new BigDecimal(divisor), SCALE, ROUNDING_MODE));
	}
	
	public int compareTo(Score other) {
		return value.compareTo(other.value);
	}
	
	public boolean greaterThan(Score other) {
		return compareTo(other) > 0;
	}
	
	public boolean lessThan(Score other) {
		return compareTo(other) < 0;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Score)) {
			return false;
		}
		return value.equals(((Score)other).value);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
