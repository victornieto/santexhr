package org.openapplicant.domain.email;

import static org.openapplicant.domain.email.PlaceHolder.CANDIDATE_FIRST_NAME;
import static org.openapplicant.domain.email.PlaceHolder.CANDIDATE_FULL_NAME;
import static org.openapplicant.domain.email.PlaceHolder.CANDIDATE_LAST_NAME;
import static org.openapplicant.domain.email.PlaceHolder.COMPANY_NAME;
import static org.openapplicant.domain.email.PlaceHolder.TODAY;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.openapplicant.domain.Candidate;
import org.openapplicant.util.CalendarUtils;
import org.springframework.mail.SimpleMailMessage;


@Entity
public class RequestResumeEmailTemplate extends EmailTemplate {

	@Override
	@Transient
	public String getName() {
		return "Request Resume";
	}
	
	@Override
	protected String defaultSubject() {
		return "Santex HR Resume Request";
	}
	
	@Override
	protected String defaultBody() {
		return "Dear " +CANDIDATE_FIRST_NAME+ 
		",\n\nThank you for taking the time to submit your resume with "+COMPANY_NAME+ 
		" which is the first step in the employment process.\n\nTo continue the employment process, please submit your resume for review.  You can do so by replying directly to this email and attaching a resume and cover letter (optional).\n\nSincerely,\n\n" 
		+ COMPANY_NAME;
	}
	
	@Transient
	@Override
	public List<PlaceHolder> getPlaceHolders() {
		return Arrays.asList(
				CANDIDATE_FIRST_NAME, 
				CANDIDATE_LAST_NAME, 
				CANDIDATE_FULL_NAME,
				COMPANY_NAME,
				TODAY
		);
	}
	
	/**
	 * Composes a MailMessage from this template using the given candidate
	 * as a model
	 * @param candidate
	 * @return the composed MailMessage
	 */
	public SimpleMailMessage compose(Candidate candidate) {
		return createMailMessage(
				candidate.getEmail(), 
				processBody(candidate)
		);
	}
	
	private String processBody(Candidate candidate) {
		return getBody()
			.replace(CANDIDATE_FIRST_NAME.toString(), candidate.getName().getFirst())
			.replace(CANDIDATE_LAST_NAME.toString(), candidate.getName().getLast())
			.replace(CANDIDATE_FULL_NAME.toString(), candidate.getName().getFullName())
			.replace(COMPANY_NAME.toString(), candidate.getCompany().getName())
			.replace(TODAY.toString(), CalendarUtils.today());
	}
}
