package org.openapplicant.dao.hibernate;

import org.openapplicant.dao.IFileAttachmentDAO;
import org.openapplicant.domain.FileAttachment;
import org.springframework.stereotype.Repository;


@Repository
public class FileAttachmentDAO extends DomainObjectDAO<FileAttachment> 
	implements IFileAttachmentDAO {
	
	public FileAttachmentDAO() {
		super(FileAttachment.class);
	}
	
}
