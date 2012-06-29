package org.openapplicant.dao.hibernate;

import org.openapplicant.dao.IResponseDAO;
import org.openapplicant.domain.Response;
import org.springframework.stereotype.Repository;


@Repository
public class ResponseDAO extends DomainObjectDAO<Response> implements IResponseDAO {

	public ResponseDAO() {
		super(Response.class);
	}

}
