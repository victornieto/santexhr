package org.openapplicant.dao;

import java.util.List;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.ExamDefinition;

public interface IExamDefinitionDAO extends IDomainObjectDAO<ExamDefinition> {

	/**
	 * Finds all exam definitions for the given company
	 * @param compan
	 */
	List<ExamDefinition> findAllExamDefinitionsByCompany(Company company);

	ExamDefinition findExamDefinitionByArtifactId(
			String examDefinitionArtifactId);

}
