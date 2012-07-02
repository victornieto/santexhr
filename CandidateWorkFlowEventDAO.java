package org.openapplicant.dao.hibernate;

import java.util.List;

import org.openapplicant.dao.ICandidateWorkFlowEventDAO;
import org.openapplicant.domain.event.AddNoteToCandidateEvent;
import org.openapplicant.domain.event.CandidateWorkFlowEvent;
import org.springframework.stereotype.Repository;


@Repository
public class CandidateWorkFlowEventDAO extends DomainObjectDAO<CandidateWorkFlowEvent> 
	implements ICandidateWorkFlowEventDAO {
	
	public CandidateWorkFlowEventDAO() {
		super(CandidateWorkFlowEvent.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<CandidateWorkFlowEvent> findAllByCandidateId(Long candidateId) {
		return getHibernateTemplate().find(
				"from " + CandidateWorkFlowEvent.class.getName() + " where candidate.id = ? order by entityInfo.createdDate desc", 
				candidateId
		);
	}

    @SuppressWarnings("unchecked")
    public List<AddNoteToCandidateEvent> findNotesToCandidateByCandidateId(Long candidateId) {
        return getHibernateTemplate().find(
                "from " + AddNoteToCandidateEvent.class.getName() + " where candidate.id = ? order by entityInfo.createdDate desc",
                candidateId
        );
    }

}
