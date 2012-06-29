package org.openapplicant.dao.hibernate;

import org.openapplicant.dao.ISittingDAO;
import org.openapplicant.domain.Sitting;
import org.springframework.stereotype.Repository;


@Repository
public class SittingDAO extends DomainObjectDAO<Sitting> implements ISittingDAO {

	public SittingDAO() {
		super(Sitting.class);
	}
	
	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException("Sittings may not be deleted");
	}
	
}
