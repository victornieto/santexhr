package org.openapplicant.dao.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.IQuestionDAO;
import org.openapplicant.domain.QuestionStatistics;
import org.openapplicant.domain.question.Question;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.List;


@Repository
public class QuestionDAO extends DomainObjectDAO<Question> 
	implements IQuestionDAO {
	
	public QuestionDAO() {
		super(Question.class);
	}

	public QuestionStatistics getTotalTimeStatistics(Question question) {
	    	return getStatisticsForColumn(question, "total_time");
	}
	
	public QuestionStatistics getWordsPerMinuteStatistics(Question question) {
	    	return getStatisticsForColumn(question, "words_per_minute");
	}
	
	public QuestionStatistics getKeyCharsStatistics(Question question) {
	    	return getStatisticsForColumn(question, "key_chars");
	}
	
	public QuestionStatistics getCorrectnessStatistics(Question question) {
	    	return getStatisticForGrade(question, "function");
	}
	
	public QuestionStatistics getStyleStatistics(Question question) {
	    	return getStatisticForGrade(question, "form");
	}
	
	private QuestionStatistics getStatisticsForColumn(Question question, String column) {
		
		
		final String sql = 
			"select min(r."+column+") as MIN," +
			" avg(r."+column+") as MEAN," + 
			" max(r."+column+") as MAX," +
			" stddev_samp(r."+column+") as STDDEV from response as r" +
			" inner join question_and_response qr on r.id = qr.response" + 
			" where qr.question = " + question.getId();
		
		Object[] result = (Object[]) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return session.createSQLQuery(sql)
						.addScalar("MIN", Hibernate.BIG_DECIMAL)
						.addScalar("MEAN", Hibernate.BIG_DECIMAL)
						.addScalar("MAX", Hibernate.BIG_DECIMAL)
						.addScalar("STDDEV", Hibernate.BIG_DECIMAL)
						.uniqueResult();
			}
		});
	
		return new QuestionStatistics(result[0],result[1],result[2],result[3]);
	}
	
	/**
	 * 
	 * @param question
	 * @param criterion
	 * @return the average and standard deviation for the given 
	 */
	private QuestionStatistics getStatisticForGrade(Question question, String criterion) {
		final String sql = 
			"select min(gs.score) as MIN,"+
			" avg(gs.score) as MEAN,"+
			" max(gs.score) as MAX,"+
			" stddev_samp(gs.score) as STDDEV from question as q " +
			"join question as q1 on q.artifact_id=q1.artifact_id " +
			"join question_and_response as qar on qar.question=q1.id " +
			"join response as r on qar.response=r.id " +
			"join grade as g on r.grade=g.id " +
			"join grade_scores as gs on g.id=gs.grade "+
			"where q.id="+question.getId()+
			" and gs.mapkey='"+criterion+"'";
		Object[] result = (Object[]) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return session.createSQLQuery(sql)
						.addScalar("MIN", Hibernate.BIG_DECIMAL)
						.addScalar("MEAN", Hibernate.BIG_DECIMAL)
						.addScalar("MAX", Hibernate.BIG_DECIMAL)
						.addScalar("STDDEV", Hibernate.BIG_DECIMAL)
						.uniqueResult();
			}
		});
		
		return new QuestionStatistics(result[0],result[1],result[2],result[3]);
	}

	public QuestionStatistics findQuestionStatisticsByIdAndColumn(final Long questionId, String column) {
				
		Formatter formatter = new Formatter();
		final String query1 = formatter.format("select min(%s) as MIN," +
													  "avg(%s) as MEAN," +
													  "max(%s) as MAX," +
											  "stddev_samp(%s) as STDDEV " +
													"from v_question_candidate_stats where question=?",column,column,column,column).toString();
		
		Object[] result = (Object[]) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return session.createSQLQuery(query1)
					.addScalar("MIN", Hibernate.BIG_DECIMAL)
					.addScalar("MEAN", Hibernate.BIG_DECIMAL)
					.addScalar("MAX", Hibernate.BIG_DECIMAL)
					.addScalar("STDDEV", Hibernate.BIG_DECIMAL)
					.setLong(0, questionId)
					.uniqueResult();
			}
		});
		
		QuestionStatistics qs = new QuestionStatistics(result[0],result[1],result[2],result[3]);

		Formatter formatter2 = new Formatter();		
		final String query2 = formatter2.format("select status, avg(%s) as AVG from v_question_candidate_stats where question=? group by status",column).toString();

		// TODO change this to a transformer which loads the map without the loop?
		List results =  getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery(query2)
									.addScalar("status")
									.addScalar("AVG", Hibernate.BIG_DECIMAL);
				query.setLong(0, questionId);
				return query.list();
			}
		});
		for(Object rs : results) {
			logger.info("In the results");
			Object[] rsx = (Object[])rs;
			if (rsx[0].equals("ACTIVE")) {
				qs.setActiveMean((BigDecimal)rsx[1]);
			} else if (rsx[0].equals("BENCHMARKED")) {
				qs.setBenchmarkMean((BigDecimal)rsx[1]);
			} else if (rsx[0].equals("FUTURE")) {
				qs.setFutureMean((BigDecimal)rsx[1]);
			} else if (rsx[0].equals("REJECTED")) {
				qs.setRejectedMean((BigDecimal)rsx[1]);
			} else if (rsx[0].equals("HIRED")) {
				qs.setHiredMean((BigDecimal)rsx[1]);
			} 
		}
		logger.info("Done processing results.");
		
		return qs;
		
	}

	@SuppressWarnings("unchecked")
	public List<Question> getQuestionsWithoutCategory() {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createQuery("from Question where category is null and frozen = false")
						.list();
			}
		});
	}

	public Question findByArtifactId(final String questionArtifactId) {
		return (Question) getHibernateTemplate().execute(new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createCriteria(Question.class)
						.add(Restrictions.eq("artifactId", questionArtifactId))
                        .add(Restrictions.isNull("nextVersion"))
						.uniqueResult();
			}
		});
	}
}
