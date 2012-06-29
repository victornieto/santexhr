package org.openapplicant.domain.email;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.email.PlaceHolder;
import org.openapplicant.domain.email.RequestResumeEmailTemplate;
import org.openapplicant.util.CalendarUtils;
import org.springframework.mail.SimpleMailMessage;


public class RequestResumeEmailTemplateTest {

	@Test
	public void compose() {
		RequestResumeEmailTemplate template = new RequestResumeEmailTemplate();
		template.setBody(
				PlaceHolder.CANDIDATE_FIRST_NAME + "," +
				PlaceHolder.CANDIDATE_LAST_NAME + "," + 
				PlaceHolder.CANDIDATE_FULL_NAME + "," +
				PlaceHolder.COMPANY_NAME + "," + 
				PlaceHolder.TODAY + "," +
				PlaceHolder.EXAM_LINK
		);
		
		Candidate candidate = new CandidateBuilder().build();
		SimpleMailMessage msg = template.compose(candidate);
		
		assertEquals(
				candidate.getName().getFirst() + "," +
				candidate.getName().getLast() + "," + 
				candidate.getName().getFullName() + "," +
				candidate.getCompany().getName() + "," +
				CalendarUtils.today() + "," + 
				PlaceHolder.EXAM_LINK, // not supported for replacement
				msg.getText()
		);
		assertEquals(candidate.getEmail(), msg.getTo()[0]);
	}
}
