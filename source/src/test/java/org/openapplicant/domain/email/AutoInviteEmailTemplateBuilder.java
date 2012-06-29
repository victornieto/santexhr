package org.openapplicant.domain.email;

import org.openapplicant.domain.email.AutoInviteEmailTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class AutoInviteEmailTemplateBuilder {
	
	private Long id;
	
	public AutoInviteEmailTemplateBuilder withId(Long value){ 
		id = value;
		return this;
	}
	
	public AutoInviteEmailTemplate build() {
		AutoInviteEmailTemplate template = new AutoInviteEmailTemplate();
		ReflectionTestUtils.invokeSetterMethod(template, "id", id);
		return template;
	}

}
