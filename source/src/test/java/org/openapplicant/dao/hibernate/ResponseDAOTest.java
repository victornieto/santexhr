package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.openapplicant.dao.IResponseDAO;
import org.openapplicant.domain.GradeBuilder;
import org.openapplicant.domain.Response;
import org.openapplicant.domain.ResponseBuilder;


public class ResponseDAOTest extends DomainObjectDAOTest<Response> {
	
	@Resource
	protected IResponseDAO responseDao;
	
	@Override
	public Response newDomainObject() {
		return new ResponseBuilder().build();
	}
	
	@Override
	public IResponseDAO getDomainObjectDao() {
		return responseDao;
	}

	@Test
	public void save_defaults() {
		Response response = new ResponseBuilder()
					.withContent(null)
					.withKeypressEvents(null)
					.withFocusEvents(null)
					.build();
									
		response = responseDao.save(response);
		
		Response found = responseDao.findByGuid(response.getGuid());
		assertNotNull(found.getGrade());
		assertNotNull(found.getContent());
		assertNotNull(found.getKeypressEvents());
		assertNotNull(found.getFocusEvents());
	}
	
	@Test
	public void save_grade() {
		Response response = new ResponseBuilder().build();
		assertNotNull(response.getGrade());
		
		response = responseDao.save(response);
		
		assertNotNull(response.getGrade());
	}

}
