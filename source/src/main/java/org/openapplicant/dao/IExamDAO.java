package org.openapplicant.dao;

import java.util.List;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.QuestionStatistics;


public interface IExamDAO extends IDomainObjectDAO<Exam> {
	
	/**
	 * Find all exams for the company with
	 * the given id
	 * 
	 * @param companyId the id of the company who's exams to find
	 * @return a list of exams.
	 */
	List<Exam> findByCompanyId(long companyId);
	
	/**
	 * Find the exam with the given artifact id
	 * 
	 * @param the artifact
	 * @return the latest version.
	 */
	Exam findByArtifactId(String groupId);

	
	/**
	 * find the latest version of an exam with the specified company and name
	 */
	Exam findByCompanyAndName(Company company,String name);
	
	/**
	 * return the question statistics for the specified exam and column
	 * @param artifactId
	 * @param column
	 * @return
	 */
	QuestionStatistics findExamStatisticsByArtifactIdAndColumn(final String artifactId, String column);
	
	
	// TODO move me to sittingDAO?
	QuestionStatistics findSittingStatisticsBySittingId(final Long sittingId, String column);
	
}
