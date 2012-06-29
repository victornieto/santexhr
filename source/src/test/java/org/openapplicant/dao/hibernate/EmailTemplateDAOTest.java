package org.openapplicant.dao.hibernate;

import javax.annotation.Resource;

import org.openapplicant.dao.ICompanyDAO;
import org.openapplicant.dao.IDomainObjectDAO;
import org.openapplicant.dao.IEmailTemplateDAO;
import org.openapplicant.domain.email.AutoInviteEmailTemplateBuilder;
import org.openapplicant.domain.email.EmailTemplate;


public class EmailTemplateDAOTest extends DomainObjectDAOTest<EmailTemplate> {
	
	@Resource
	protected IEmailTemplateDAO emailTemplateDAO;

	@Resource
	protected ICompanyDAO companyDAO;
	
	@Override
	protected IDomainObjectDAO<EmailTemplate> getDomainObjectDao() {
		return emailTemplateDAO;
	}

	@Override
	protected EmailTemplate newDomainObject() {
		return new AutoInviteEmailTemplateBuilder().build();
	}
}
