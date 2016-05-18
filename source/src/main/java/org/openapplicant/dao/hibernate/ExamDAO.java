package org.openapplicant.dao.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.openapplicant.dao.IExamDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.QuestionStatistics;
import org.openapplicant.util.Params;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


@Repository
public class ExamDAO extends DomainObjectDAO<Exam> implements IExamDAO {
	
	public ExamDAO() {
		super(Exam.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Exam> findByCompanyId(long companyId) {
        /*
		return getHibernateTemplate().find(
				"from " + Exam.class.getName() + 
				" where company.id = ?",
				companyId
		);*/
        // FIXME This method was being called by DynamicExamsStrategy, but now the system works with
        // ExamDefinition instances, not with Exam instances
        return new ArrayList<Exam>();
    }
	
	public Exam findByArtifactId(String artifactId) {
		return findUniqueResult(
				"from " + Exam.class.getName() + 
				" where artifactId = :artifactId", 
				new Params("artifactId", artifactId)
		);
	}
	
	public Exam findByCompanyAndName(Company company, String name) {
		Params parameters = new Params();
		parameters.add("companyId", company.getId());
		parameters.add("name", name);
		return findUniqueResult(
				"from " + Exam.class.getName() +
				" where company.id=? and name=?",
				parameters
			);
	}
	
	
	
	public QuestionStatistics findExamStatisticsByArtifactIdAndColumn(final String artifactId, String column) {
		
		Formatter formatter = new Formatter();
		final String query1 = formatter.format("select min(vs.%s) as MIN," +
													  "avg(vs.%s) as MEAN," +
													  "max(vs.%s) as MAX," +
											  "stddev_samp(vs.%s) as STDDEV "
											  +"from "
										      +"exam as e "
										      		+"left join exam_questions eq "
										      		 	+"on e.id=eq.exam "
										      		+"left join v_question_candidate_stats as vs "
										      		    +"on eq.questions=vs.question "
											  +"where e.artifact_id=?",column,column,column,column).toString();
		
		Object[] result = (Object[]) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return session.createSQLQuery(query1)
					.addScalar("MIN", Hibernate.BIG_DECIMAL)
					.addScalar("MEAN", Hibernate.BIG_DECIMAL)
					.addScalar("MAX", Hibernate.BIG_DECIMAL)
					.addScalar("STDDEV", Hibernate.BIG_DECIMAL)
					.setString(0, artifactId)
					.uniqueResult();
			}
		});
		
		QuestionStatistics qs = new QuestionStatistics(result[0],result[1],result[2],result[3]);

		

		Formatter formatter2 = new Formatter();		
		final String query2 = formatter2.format("select vs.status, ifnull(avg(vs.%s),0) as AVG "
				+"from exam as e "
	      		+"left join exam_questions eq "
	      		 	+"on e.id=eq.exam "
	      		+"left join v_question_candidate_stats as vs "
	      		    +"on eq.questions=vs.question "
		  		+" where e.artifact_id=? group by vs.status",column).toString();

		// TODO change this to a transformer which loads the map without the loop?
		List results =  getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery(query2)
									.addScalar("status")
									.addScalar("AVG", Hibernate.BIG_DECIMAL);
				query.setString(0, artifactId);
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
		
		return qs;
		
	}
	
	public QuestionStatistics findSittingStatisticsBySittingId(final Long sittingId, String column) {

		Formatter formatter = new Formatter();
		final String query1 = formatter.format("select min(vs.%s) as MIN," +
													  "avg(vs.%s) as MEAN," +
													  "max(vs.%s) as MAX," +
											  "stddev_samp(vs.%s) as STDDEV "
											  +"from "
										      +" v_question_candidate_stats as vs "
											  +"where vs.sitting_id=?",column,column,column,column).toString();
		
		Object[] result = (Object[]) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				return session.createSQLQuery(query1)
					.addScalar("MIN", Hibernate.BIG_DECIMAL)
					.addScalar("MEAN", Hibernate.BIG_DECIMAL)
					.addScalar("MAX", Hibernate.BIG_DECIMAL)
					.addScalar("STDDEV", Hibernate.BIG_DECIMAL)
					.setLong(0, sittingId)
					.uniqueResult();
			}
		});
		
		return new QuestionStatistics(result[0],result[1],result[2],result[3]);
	}
	
	
	
}
