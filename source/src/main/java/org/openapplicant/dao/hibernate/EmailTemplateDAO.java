package org.openapplicant.dao.hibernate;

import org.openapplicant.dao.IEmailTemplateDAO;
import org.openapplicant.domain.email.EmailTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class EmailTemplateDAO extends DomainObjectDAO<EmailTemplate> 
		implements IEmailTemplateDAO {

	public EmailTemplateDAO() {
		super(EmailTemplate.class);
	}
}
