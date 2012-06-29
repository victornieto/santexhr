package org.openapplicant.domain.email;

import static org.openapplicant.domain.email.PlaceHolder.CANDIDATE_FIRST_NAME;
import static org.openapplicant.domain.email.PlaceHolder.CANDIDATE_FULL_NAME;
import static org.openapplicant.domain.email.PlaceHolder.CANDIDATE_LAST_NAME;
import static org.openapplicant.domain.email.PlaceHolder.COMPANY_NAME;
import static org.openapplicant.domain.email.PlaceHolder.EXAM_LINK;
import static org.openapplicant.domain.email.PlaceHolder.TODAY;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.util.CalendarUtils;
import org.springframework.mail.SimpleMailMessage;


@Entity
public class ExamInviteEmailTemplate extends EmailTemplate {
	
	@Override
	@Transient
	public String getName(){
		return "Exam Invite";
	}
	
	@Override
	protected String defaultSubject() {
		return "Santex HR Exam Invitation";
	}
	
	@Override
	protected String defaultBody() {
		return "Dear " +CANDIDATE_FIRST_NAME+ 
		",\n\nThank you for taking the time to submit your resume with "+COMPANY_NAME+ 
		" which is the first step in the employment process.\n\nThe next step in the process is to complete an online assessment.  We have prepared the assessment which can be accessed at the following link:\n\n"+ 
		EXAM_LINK + "\n\nFollowing your completion of the assessment, we will notify you and outline the remaining steps in the employment process.\n\nSincerely,\n\n" 
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
				EXAM_LINK,
				TODAY
		);
	}
	
	/**
	 * Composes a MailMessage from this template using the given examLink 
	 * as a model.
	 * @param examLink
	 * @return the composed MailMessage
	 */
	public SimpleMailMessage compose(CandidateExamLink examLink) {
		return createMailMessage(
				examLink.getCandidate().getEmail(), 
				processBody(examLink)
		);
	}
	
	private String processBody(CandidateExamLink examLink) {
		return getBody()
				.replace(CANDIDATE_FIRST_NAME.toString(), (examLink.getCandidate().getName().hasFirst() ? examLink.getCandidate().getName().getFirst() : "candidate"))
				.replace(CANDIDATE_LAST_NAME.toString(), examLink.getCandidate().getName().getLast())
				.replace(CANDIDATE_FULL_NAME.toString(), examLink.getCandidate().getName().getFullName())
				.replace(COMPANY_NAME.toString(), examLink.getCompany().getName())
				.replace(EXAM_LINK.toString(), examLink.getUrl().toString())
				.replace(TODAY.toString(), CalendarUtils.today());
	}
	
	// FIXME: 
	// CompanyExamLink examLink = new CompanyExamLink(candidate)
	// template.compose(CompanyExamLink examLink)
	// getExamLinkDao().save(examLink)
	public SimpleMailMessage compose(Candidate candidate, Company company) {
		return createMailMessage(
				candidate.getEmail(),			 
				processBody(candidate, company)
		);
	}
	
	private String processBody(Candidate candidate, Company company) {
		return getBody()
			.replace(CANDIDATE_FIRST_NAME.toString(), (candidate.getName().hasFirst()) ? candidate.getName().getFirst() : "candidate")
			.replace(CANDIDATE_LAST_NAME.toString(), candidate.getName().getLast())
			.replace(CANDIDATE_FULL_NAME.toString(), candidate.getName().getFullName())
			.replace(COMPANY_NAME.toString(), company.getName())
			.replace(EXAM_LINK.toString(), company.getLinkToAllExams().getUrl().toString())
			.replace(TODAY.toString(), CalendarUtils.today());
	}
	
	
}
