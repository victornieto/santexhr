package org.openapplicant.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.openapplicant.domain.Score;

public class ScoreTest {
	
	@Test
	public void add() {
		Score score = new Score(10);
		score = score.add(new Score(20.56));
		
		assertEquals(new BigDecimal("30.6"), score.getValue());
	}
	
	@Test
	public void add_intConstructor() {
		Score score = new Score(10);
		score = score.add(new Score(20));
		
		assertEquals(new BigDecimal("30.0"), score.getValue());
		
		score = score.add(new Score(10.55));
		
		assertEquals(new BigDecimal("40.6"), score.getValue());
	}
	
	@Test
	public void divide() {
		Score score = new Score(10);
		score = score.divide(3);
		
		assertEquals(new BigDecimal("3.3"), score.getValue());
	}
	
	@Test
	public void average() {
		Collection<Score> scores = new ArrayList<Score>(0);
		assertEquals(Score.ZERO, Score.average(scores));
		
		scores = Arrays.asList(new Score(10), Score.ZERO);
		assertEquals(new Score(5), Score.average(scores));
	}
	
	@Test
	public void compareTo() {
		Score ten = new Score(10);
		
		assertEquals(0, ten.compareTo(new Score(10)));
		assertEquals(0, ten.compareTo(new Score(10.00)));
		assertEquals(0, ten.compareTo(new Score(9.99)));
		assertEquals(1, ten.compareTo(new Score(9.9)));
		assertEquals(-1, ten.compareTo(new Score(10.09)));
	}
	
	@Test
	public void greaterThan() {
		Score ten = new Score(10);
		
		assertTrue(ten.greaterThan(new Score(9.9)));
		assertFalse(ten.greaterThan(new Score(10.1)));
	}
	
	@Test
	public void lessThan() {
		Score ten = new Score(10);
		
		assertTrue(ten.lessThan(new Score(10.1)));
		assertFalse(ten.lessThan(new Score(9.9)));
	}
}
