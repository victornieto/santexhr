package org.openapplicant.service.facilitator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.domain.*;
import org.openapplicant.domain.Candidate.Status;
import org.openapplicant.domain.email.AutoInviteEmailTemplate;
import org.openapplicant.domain.email.RejectCandidateEmailTemplate;
import org.openapplicant.domain.email.RequestResumeEmailTemplate;
import org.openapplicant.domain.event.CandidateCreatedEvent;
import org.openapplicant.domain.event.FacilitatorRejectedResumeEvent;
import org.openapplicant.domain.event.FacilitatorRequestedResumeEvent;
import org.openapplicant.domain.event.FacilitatorSentExamLinkEvent;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.domain.link.DynamicExamsStrategy;
import org.openapplicant.domain.setting.Smtp;
import org.openapplicant.service.ApplicationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class FacilitatorService extends ApplicationService {

	private static final Log log = LogFactory.getLog(FacilitatorService.class);
	
	public void facilitateCandidate(Message mailMessage) throws IOException, MessagingException {
		MessageReader reader = new MessageReader(mailMessage);
		DocumentResolver resolver = new DocumentResolver(reader);
		
		String companyEmail = reader.getTo();
		String candidateEmail = reader.getSender();
		String candidateName = reader.getSenderPersonalName();
		Resume resume = resolver.getResume();
		CoverLetter coverLetter = resolver.getCoverLetter();
		
		Company company = findCompanyOrNull(companyEmail);
		if(company == null) {
			log.warn("no company for email: " + companyEmail);
			return; // TODO: save invalid message
		}
		
		Candidate candidate = findOrCreateCandidate(company, candidateEmail, candidateName);
		
		if(resume == null) {
			if(company.getProfile().isSolicitResumes()) {
				RequestResumeEmailTemplate template = company.getRequestResumeEmailTemplate();
				SimpleMailMessage message = template.compose(candidate);
                sendEmailAsynchronously(message, company.getSmtp());
				getCandidateWorkFlowEventDao().save(new FacilitatorRequestedResumeEvent(candidate));
				candidate.setStatus(Status.NOT_TESTED);
			}
		} else {
			candidate.setResume(resume);
			updateCandidateFromResume(candidate, resume);
			
			// do the screening
			BigDecimal score = company.getProfile().screenResume(resume.getStringContent());
			resume.setScreeningScore(score);
			if(company.getProfile().getMinInviteScore() != null) {
				if (score.intValue() >= company.getProfile().getMinInviteScore()) {
					candidate.setStatus(Status.SENT_EXAM);
					AutoInviteEmailTemplate template = company.getAutoInviteEmailTemplate();
					CandidateExamLink examLink = new CandidateExamLink(company,candidate,new DynamicExamsStrategy());							
					examLink.setDescription("Facilitator-generated company test invitation");
                    SimpleMailMessage message = template.compose(examLink);
                    sendEmailAsynchronously(message, company.getSmtp());
					getCandidateWorkFlowEventDao().save(new FacilitatorSentExamLinkEvent(candidate, examLink));
					getCandidateExamLinkDao().save(examLink);
				}
			}
			if(company.getProfile().getMaxRejectScore() != null) {
				if (resume.getScreeningScore().intValue() < company.getProfile().getMaxRejectScore()) {
					candidate.setStatus(Status.RESUME_REJECTED);
					RejectCandidateEmailTemplate template = company.getRejectCandidateEmailTemplate();
					SimpleMailMessage message = template.compose(candidate);
                    sendEmailAsynchronously(message, company.getSmtp());
					getCandidateWorkFlowEventDao().save(new FacilitatorRejectedResumeEvent(candidate));
				}
			}
		}
		
		if(coverLetter != null) {
			candidate.setCoverLetter(coverLetter);
		}
		getCandidateDao().save(candidate);
		
		if(company.getProfile().isForwardCandidateEmails()) {
			try {
				forwardMessage(company.getProfile().getCandidateEmailsRecipient(), reader, company.getSmtp());
			} catch(Exception e) {
				log.error(e);
				//TODO: save message
			}
		}
	}
	
	private Company findCompanyOrNull(String email) {
		return getCompanyDao().findByEmailAliasOrNull(StringUtils.substringBefore(email,"@"));
	}
	
	// FIXME: duplication with QuizService.resolveCandidate
	private Candidate findOrCreateCandidate(Company company, String candidateEmail, String candidateName) {
		if(StringUtils.isNotBlank(candidateEmail)) {
			Candidate result = getCandidateDao().findByEmailAndCompanyIdOrNull(
					candidateEmail, 
					company.getId()
			);
			if(result != null) {
				return result;
			}
		}
		
		Candidate result = new Candidate();
		result.setName(new Name(candidateName));
		result.setCompany(company);
		getCandidateDao().save(result);
		getCandidateWorkFlowEventDao().save(new CandidateCreatedEvent(result));
		return result;
	}
	
	public Candidate extractCandidate(String resume) {
		resume = StringUtils.trim(resume);
		String lines[] = resume.split("\r|\r\n|\n");
		int lineCount = lines.length;
		
		// name
		// assumption:  first line
		String name = lines[0];
		
		// address
		// find a line of the format <blah>, NN\s+NNNNN(-NNNN)
		// then take the previous line too!
		Pattern addressPattern = Pattern.compile("(.*), (\\w{2})\\s+(\\d{5})(-\\d{4})?(.*)?");
		String address = null;
		for (int i = 1; i < lineCount; i++) {
			Matcher matcher = addressPattern.matcher(lines[i]);
			if (matcher.find()) {
				log.debug("-- address on line "+i);
				if (matcher.group(1).contains(",")) {
					address = matcher.group(1).replace(',', '\n')+ ", " + matcher.group(2) + " " +matcher.group(3);
				} else {
					address = lines[i-1] + "\n" + matcher.group(1) + ", " + matcher.group(2) + " " +matcher.group(3);
				}
			}
		}
		if (address != null) {
			address = address.trim();
			log.debug("address is "+address);
		}
		
		// phone numbers
		Map<String,String> numbers = new HashMap<String,String>(3);
		Pattern phonePattern = Pattern.compile("(\\d{3})[-\\ .](\\d{3})[-\\. ](\\d{4}) ?(\\([^\\)]+\\))?");
		boolean backwards = false;
		String kind = "";
		for (int i = 1; i < lineCount; i++) {
			Matcher matcher = phonePattern.matcher(lines[i]);
			while (matcher.find()) {
				log.debug("-- phone number matched on line "+i+" '"+lines[i]+"'");
				String number = matcher.group(1) + "-" + matcher.group(2) + "-" +matcher.group(3);
				log.debug("Number is: "+number);
				if (matcher.group(4) != null) {
					log.debug("numbertype is "+matcher.group(4));
					if (StringUtils.containsIgnoreCase(matcher.group(4),"cell"))
						kind = "cell";
					else if (StringUtils.containsIgnoreCase(matcher.group(4),"work"))
						kind = "work";
					else 
						kind = "home";
					backwards = true;
				} else if (!backwards) {
					int start = matcher.regionStart();
					int end = matcher.start();
					log.debug("checking region from start "+start+" to end "+end);
					if (StringUtils.containsIgnoreCase(lines[i].substring(start,end),"cell"))
						kind = "cell";
					else if (StringUtils.containsIgnoreCase(lines[i].substring(start,end),"work"))
						kind = "work";
					else 
						kind = "home";
					log.debug("kind: "+kind);
				}
				numbers.put(kind, number);
			}
			
		}
		
		
		// email
		String email = "";
		Pattern emailPattern = Pattern.compile("\\b([A-Za-z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})\\b");
		Matcher emailMatcher = emailPattern.matcher(resume);
		if (emailMatcher.find()) {
			email = emailMatcher.group(1);
			log.debug("Email "+email);
		} else {
			log.fatal("Could not find an e-mail address!");
		}

		if ((name.isEmpty()) || (email.isEmpty())) {
			log.error("Unable to extract e-mail or name.");
			return null;
		}
		Candidate candidate = new Candidate();
		Name nameObject = new Name(name);
		candidate.setName(nameObject);
		candidate.setEmail(email);
		candidate.setAddress(address);
		if (numbers.containsKey("cell"))
			candidate.setCellPhoneNumber(new PhoneNumber(numbers.get("cell")));
		if (numbers.containsKey("home"))
			candidate.setHomePhoneNumber(new PhoneNumber(numbers.get("home")));
		if (numbers.containsKey("work"))
			candidate.setWorkPhoneNumber(new PhoneNumber(numbers.get("work")));
		return candidate;
		
	}
	
	private void updateCandidateFromResume(Candidate existingCandidate, Resume resume) {
		Candidate extractedCandidate = extractCandidate(resume.getStringContent());
		if(extractedCandidate == null) {
			return;
		}
      	//Company company = existingCandidate.getCompany();
      	//Profile profile = company.getProfile();
      	//existingCandidate.setScreeningScore(profile.screenResume(resume.getStringContent()));
      	if (StringUtils.isBlank(existingCandidate.getEmail())) {
      		existingCandidate.setEmail(extractedCandidate.getEmail());
      	}
      	if (existingCandidate.getCellPhoneNumber().isBlank()) {
      		existingCandidate.setCellPhoneNumber(extractedCandidate.getCellPhoneNumber());
      	}
      	if (existingCandidate.getHomePhoneNumber().isBlank()) {
      		existingCandidate.setHomePhoneNumber(extractedCandidate.getHomePhoneNumber());
      	}
      	if (existingCandidate.getWorkPhoneNumber().isBlank()) {
      		existingCandidate.setWorkPhoneNumber(extractedCandidate.getWorkPhoneNumber());
      	}
      	if (StringUtils.isBlank(existingCandidate.getAddress())) {
      		existingCandidate.setAddress(extractedCandidate.getAddress());
      	}
      	if (existingCandidate.getName().isBlank()) {
      		existingCandidate.setName(extractedCandidate.getName());
      	}
	}
	
	private void forwardMessage(String recipient, MessageReader reader, Smtp smtp)
		throws MessagingException {
		
		MimeMessage fwd = new JavaMailSenderImpl().createMimeMessage();
		fwd.setSubject("Fwd: [Santex HR]");
		fwd.addFrom(reader.getMessage().getFrom());
		fwd.addRecipient(RecipientType.TO, new InternetAddress(recipient));
		
		BodyPart textPart = new MimeBodyPart();
		textPart.setText(
				String.format(
						"\n----------------- Original Message -----------------\n" +
						"From: <%s>\nDate: %s\nTo: <%s>\nSubject: %s\n\n%s",
						StringUtils.join(reader.getMessage().getFrom(), ", "),
						reader.getMessage().getSentDate(),
						StringUtils.join(reader.getMessage().getRecipients(RecipientType.TO), ", "),
						StringUtils.trimToEmpty(reader.getMessage().getSubject()),
						reader.getBodyText()
				)
		);
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(textPart);
		
		BodyPart messagePart = new MimeBodyPart();
		messagePart.setDataHandler(reader.getMessage().getDataHandler());

		multipart.addBodyPart(messagePart);
		fwd.setContent(multipart);
		
		sendEmailSynchronously(fwd, smtp);
	}
}
