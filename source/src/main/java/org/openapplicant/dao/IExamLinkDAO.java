package org.openapplicant.dao;

import java.util.List;

import org.openapplicant.domain.link.ExamLink;


public interface IExamLinkDAO extends IDomainObjectDAO<ExamLink> {
	
	/**
	 * Finds all exam links for the given company.
	 * 
	 * @param companyId the id of the company who's exam links to find.
	 * @return a list of the company's exam links
	 */
	List<ExamLink> findAllByCompanyId(Long companyId);
}
