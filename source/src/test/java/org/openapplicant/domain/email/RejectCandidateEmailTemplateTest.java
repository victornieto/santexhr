package org.openapplicant.domain.email;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.email.PlaceHolder;
import org.openapplicant.domain.email.RejectCandidateEmailTemplate;
import org.openapplicant.util.CalendarUtils;
import org.springframework.mail.SimpleMailMessage;


public class RejectCandidateEmailTemplateTest {

	@Test
	public void compose() {
		RejectCandidateEmailTemplate template = new RejectCandidateEmailTemplate();
		template.setBody(
				PlaceHolder.CANDIDATE_FIRST_NAME + "," +
				PlaceHolder.CANDIDATE_LAST_NAME + "," + 
				PlaceHolder.CANDIDATE_FULL_NAME + "," + 
				PlaceHolder.TODAY + "," + 
				PlaceHolder.COMPANY_NAME + "," +
				PlaceHolder.EXAM_LINK
		);
		
		Candidate candidate = new CandidateBuilder().build();
		
		SimpleMailMessage msg = template.compose(candidate);
		
		assertEquals(
				candidate.getName().getFirst() + "," + 
				candidate.getName().getLast() + "," + 
				candidate.getName().getFullName() + "," + 
				CalendarUtils.today() + "," + 
				candidate.getCompany().getName() + "," + 
				PlaceHolder.EXAM_LINK, // not supported for replacement
				msg.getText()
		);
		
		assertEquals(candidate.getEmail(), msg.getTo()[0]);
	}
}
