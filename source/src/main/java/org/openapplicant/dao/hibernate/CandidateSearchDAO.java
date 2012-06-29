package org.openapplicant.dao.hibernate;

import java.util.List;

import org.openapplicant.dao.ICandidateSearchDAO;
import org.openapplicant.domain.CandidateSearch;
import org.openapplicant.util.Pagination;
import org.openapplicant.util.Params;
import org.springframework.stereotype.Repository;


@Repository
public class CandidateSearchDAO extends DomainObjectDAO<CandidateSearch> 
		implements ICandidateSearchDAO {
	
	public CandidateSearchDAO() {
		super(CandidateSearch.class);
	}

	public List<CandidateSearch> findAllByUserId(Long userId, Pagination pagination) {
		return findByQueryString(
				"from " + CandidateSearch.class.getName() + " where user.id =:id order by entityInfo.createdDate desc",
				new Params("id", userId),
				pagination
		);
	}
}
