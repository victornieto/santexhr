package org.openapplicant.dao.hibernate;

import java.util.List;

import org.openapplicant.dao.IExamDefinitionDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.ExamDefinition;

public class ExamDefinitionDAO extends DomainObjectDAO<ExamDefinition> implements IExamDefinitionDAO {

	public ExamDefinitionDAO() {
		super(ExamDefinition.class);
	}

	@SuppressWarnings("unchecked")
	public List<ExamDefinition> findAllExamDefinitionsByCompany(Company company) {
		return (List<ExamDefinition>) getHibernateTemplate().find("from ExamDefinition ed where ed.company = ?", company);
	}

	public ExamDefinition findExamDefinitionByArtifactId(
			String examDefinitionArtifactId) {
		return (ExamDefinition) getHibernateTemplate().find("from ExamDefinition ed where ed.artifactId = ?", examDefinitionArtifactId).get(0);
	}

}
