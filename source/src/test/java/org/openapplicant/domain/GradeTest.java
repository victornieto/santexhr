package org.openapplicant.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.openapplicant.domain.Grade;
import org.openapplicant.domain.Score;

public class GradeTest {
	
	@Test
	public void getScore() {
		Grade grade = new GradeBuilder()
							.withNoScores()
							.addScore("form", new Score(9))
							.addScore("useful", new Score(8))
							.build();
		
		assertEquals(new Score(8.5), grade.getScore());
	}
	
	@Test
	public void isScoresValid() {
		Grade grade = new GradeBuilder()
							.withNoScores()
							.addScore("form", new Score(101))
							.build();
		assertFalse(grade.isScoresValid());
		
		grade = new GradeBuilder()
						.withNoScores()
						.addScore("form", new Score(-1))
						.build();
		assertFalse(grade.isScoresValid());
		
		grade = new GradeBuilder()
						.withNoScores()	
						.addScore("form", new Score(100.1))
						.build();
		assertFalse(grade.isScoresValid());
		
		grade = new GradeBuilder()
						.withNoScores()
						.addScore("form", new Score(100))
						.build();
		assertTrue(grade.isScoresValid());
		
		grade = new GradeBuilder()
						.withNoScores()
						.addScore("form", new Score(0))
						.build();
		assertTrue(grade.isScoresValid());
	}
	
	@Test
	public void replaceScores() {
		Grade grade = new GradeBuilder()
								.withNoScores()
								.addScore("foo", new Score(100))
								.addScore("bar", new Score(90))
								.build();
		
		
		assertEquals(100, grade.getScores().get("foo").getValue().intValue());
		assertEquals(90, grade.getScores().get("bar").getValue().intValue());
		assertEquals(95, grade.getScore().getValue().intValue());
		
		grade.updateScores(
				new GradeBuilder()
						.withNoScores()
						.addScore("foo", new Score(50))
						.addScore("bar", new Score(60))
						.build()
		);
		assertEquals(50, grade.getScores().get("foo").getValue().intValue());
		assertEquals(60, grade.getScores().get("bar").getValue().intValue());
		assertEquals(55, grade.getScore().getValue().intValue());
	}
}
