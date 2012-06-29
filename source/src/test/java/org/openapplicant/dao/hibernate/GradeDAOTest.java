package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Test;
import org.openapplicant.dao.IGradeDAO;
import org.openapplicant.domain.Grade;
import org.openapplicant.domain.GradeBuilder;
import org.openapplicant.domain.Score;


public class GradeDAOTest extends DomainObjectDAOTest<Grade> {
	
	@Resource
	protected IGradeDAO gradeDao;
	
	@Override 
	public Grade newDomainObject(){
		return new GradeBuilder().build();
	}
	
	@Override 
	public IGradeDAO getDomainObjectDao() {
		return gradeDao;
	}
	
	@Test
	public void save_score() {
		Grade grade = new GradeBuilder()
							.withNoScores()
							.addScore("form", new Score(9))
							.addScore("useful", new Score(8))
							.build();
		
		grade = gradeDao.save(grade);
		
		Grade found = gradeDao.findByGuid(grade.getGuid());
		
		assertEquals(2, found.getScores().size());
	}
	
	/*
	@Test
	public void testScore() {
		Grade g = new Grade();
		g.putScore("useful",new BigDecimal(8.0));
		g.putScore("form",new BigDecimal(10.0));
		logger.debug("Set scores; new aggregate score is "+g.computeScore());
		assertEquals(new Double(9.0),(Double)g.getScore().doubleValue());
	}
	
	@Test
	public void save_withscore() {
		Grade g = new Grade();
		g.putScore("useful",new BigDecimal(8.0));
		g.putScore("form",new BigDecimal(10.0));
		g.setSitting(sitting);
		gradeDao.save(g);
		assertNotNull(g.getId());
	}
	*/
	
}
