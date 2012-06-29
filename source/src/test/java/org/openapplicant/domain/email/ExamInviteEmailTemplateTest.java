package org.openapplicant.domain.email;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.email.ExamInviteEmailTemplate;
import org.openapplicant.domain.email.PlaceHolder;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.domain.link.CandidateExamLinkBuilder;
import org.openapplicant.util.CalendarUtils;
import org.springframework.mail.SimpleMailMessage;


public class ExamInviteEmailTemplateTest {

	@Test
	public void compose() {
		ExamInviteEmailTemplate template = new ExamInviteEmailTemplate();
		template.setBody(
				PlaceHolder.CANDIDATE_FIRST_NAME + "," + 
				PlaceHolder.CANDIDATE_LAST_NAME + "," + 
				PlaceHolder.CANDIDATE_FULL_NAME + "," + 
				PlaceHolder.COMPANY_NAME + "," + 
				PlaceHolder.EXAM_LINK + "," +
				PlaceHolder.TODAY
		);
		Candidate candidate = new CandidateBuilder().build();
		CandidateExamLink link = new CandidateExamLinkBuilder()
								.withCandidate(candidate)
								.withCompany(candidate.getCompany())
								.build();
		
		SimpleMailMessage msg = template.compose(link);
		
		assertEquals(
				candidate.getName().getFirst() +"," +
				candidate.getName().getLast() + "," + 
				candidate.getName().getFullName() + "," + 
				candidate.getCompany().getName() + "," + 
				link.getUrl() + "," + 
				CalendarUtils.today(),
				msg.getText()
		);
		
		assertEquals(candidate.getEmail(), msg.getTo()[0]);
	}
}
