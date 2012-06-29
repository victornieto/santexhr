package org.openapplicant.dao.hibernate;

import javax.annotation.Resource;

import org.openapplicant.dao.IDomainObjectDAO;
import org.openapplicant.dao.IFileAttachmentDAO;
import org.openapplicant.domain.FileAttachment;
import org.openapplicant.domain.ResumeBuilder;


public class FileAttachmentDAOTest extends DomainObjectDAOTest<FileAttachment> {

	@Resource
	private IFileAttachmentDAO fileAttachmentDao;
	
	@Override
	protected IDomainObjectDAO<FileAttachment> getDomainObjectDao() {
		return fileAttachmentDao;
	}

	@Override
	protected FileAttachment newDomainObject() {
		return new ResumeBuilder().build();
	}
	
	 

}
