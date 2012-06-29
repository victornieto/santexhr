package org.openapplicant.domain.email;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.email.RejectCandidateEmailTemplate;
import org.springframework.mail.SimpleMailMessage;


public class EmailTemplateTest {
	
	@Test
	public void setFromAddress_empty() {
		RejectCandidateEmailTemplate template = new RejectCandidateEmailTemplate();
		template.setFromAddress("	");
		
		SimpleMailMessage msg = template.compose(new CandidateBuilder().build());
		assertEquals(template.getDefaultFromAddress(), msg.getFrom());
	}

}
