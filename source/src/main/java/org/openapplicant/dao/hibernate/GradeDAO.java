package org.openapplicant.dao.hibernate;

import org.openapplicant.dao.IGradeDAO;
import org.openapplicant.domain.Grade;
import org.springframework.stereotype.Repository;


@Repository
public class GradeDAO extends DomainObjectDAO<Grade> implements IGradeDAO {
	
	public GradeDAO() {
		super(Grade.class);
	}
}
