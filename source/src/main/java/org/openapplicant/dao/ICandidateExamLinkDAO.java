package org.openapplicant.dao;

import java.util.List;

import org.openapplicant.domain.link.CandidateExamLink;


public interface ICandidateExamLinkDAO extends IDomainObjectDAO<CandidateExamLink> {

	List<CandidateExamLink> findAllByCandidateId(Long candidateId);
}
