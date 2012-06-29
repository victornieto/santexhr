package org.openapplicant.web.controller;

import static java.lang.Boolean.FALSE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.email.EmailTemplate;
import org.openapplicant.domain.setting.Facilitator;
import org.openapplicant.domain.setting.Smtp;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class EmailController extends AdminController {

	@RequestMapping(method=GET)
	public String index(Map<String, Object> model) {
	    model.put("emailSidebar", true);
		return "email/index";
	}
	
	@RequestMapping(method=GET)
	public String preferences(Map<String, Object> model) {
		model.put("profile", currentUser().getCompany().getProfile());
	    model.put("preferencesSidebar", true);
		return "email/preferences";
	}
	
	@Transactional
	@RequestMapping(method=POST) 
	public String updatePreferences(
			@ModelAttribute("profile") Profile cmd,
			BindingResult binding,
			@RequestParam(value="forward_daily_reports_to_user", required=false) Boolean forwardDailyReportsToUser,
			@RequestParam(value="forward_candidate_emails_to_user", required=false) Boolean forwardCandidateEmailsToUser,
			@RequestParam(value="forward_job_board_emails_to_user", required=false) Boolean forwardJobBoardEmailsToUser,
			@RequestParam(value="job_board", required=false) String jobBoard,
			@RequestParam(value="delete_job_boards", required=false) String action,
			Map<String,Object> model){
		
		forwardDailyReportsToUser = forwardDailyReportsToUser == null? 
				FALSE : forwardDailyReportsToUser;
		
		forwardCandidateEmailsToUser = forwardCandidateEmailsToUser == null?
				FALSE : forwardCandidateEmailsToUser;
		
		forwardJobBoardEmailsToUser = forwardJobBoardEmailsToUser == null ? 
				FALSE: forwardJobBoardEmailsToUser;
	
		getSession().beManual();
		
		Profile profile = currentUser().getCompany().getProfile();
		profile.setForwardDailyReports(cmd.isForwardDailyReports());
		profile.setForwardCandidateEmails(cmd.isForwardCandidateEmails());
		profile.setForwardJobBoardEmails(cmd.isForwardJobBoardEmails());
		
		if(forwardDailyReportsToUser) {
			profile.setDailyReportsRecipient(cmd.getDailyReportsRecipient());
		} else {
			profile.setDailyReportsRecipient("");
		}
		if(forwardCandidateEmailsToUser) {
			profile.setCandidateEmailsRecipient(cmd.getCandidateEmailsRecipient());
		} else {
			profile.setCandidateEmailsRecipient("");
		}
		if(forwardJobBoardEmailsToUser) {
			profile.setJobBoardEmailsRecipient(cmd.getJobBoardEmailsRecipient());
		} else {
			profile.setJobBoardEmailsRecipient("");
		}
		if(StringUtils.isNotBlank(jobBoard)) {
			profile.addJobBoard(jobBoard);
		}
		if("delete".equalsIgnoreCase(action)){
			profile.deleteJobBoards(cmd.getJobBoards());
		}
		
		Errors errors = profile.validate();
		if(errors.hasErrors()) {
			binding.addAllErrors(errors);
			model.put("profile", profile);
			return "email/preferences";
		} else {
			getAdminService().saveProfile(profile);
			getSession().flush();
			return "redirect:preferences";
		}
	}
	
	@RequestMapping(method=GET) 
	public String templates(Map<String, Object> model) {
		populateEmailTemplates(model);
	    model.put("templatesSidebar", true);
		return "email/templates";
	}
	
	@RequestMapping(method=GET)
	public String template(@RequestParam("id") long emailTemplateId, Map<String, Object> model) {
		EmailTemplate template = getAdminService().findEmailTemplateById(emailTemplateId);
		model.put("template", template);
		populateEmailTemplates(model);
		return "email/template";
	}
	
	@Transactional
	@RequestMapping(method=POST)
	public String updateTemplate(
			@RequestParam("id") long templateId,
			@RequestParam("fromAddress") String fromAddress,
			@RequestParam("subject") String subject,
			@RequestParam("body") String body,
			Map<String,Object> model) {
		
		getSession().beManual();
		
		EmailTemplate template = getAdminService().findEmailTemplateById(templateId);
		template.setFromAddress(fromAddress);
		template.setBody(body);
		template.setSubject(subject);
		
		Errors errors = template.validate();
		if(errors.hasErrors()) {
			if(errors.hasFieldErrors("fromAddress")) {
				model.put("fromAddressError", errors.getFieldError("fromAddress").getDefaultMessage());
			}
			if(errors.hasFieldErrors("subject")) {
				model.put("subjectError", errors.getFieldError("subject").getDefaultMessage());
			}
			if(errors.hasFieldErrors("body")) {
				model.put("bodyError", errors.getFieldError("body").getDefaultMessage());
			}
			populateEmailTemplates(model);
			model.put("template", template);
			return "email/template";
		} else {
			getAdminService().saveEmailTemplate(template);
			getSession().flush();
			return "redirect:template?id="+template.getId();
		}
	}
	
	@RequestMapping(method=GET)
	public String revertTemplate(@RequestParam("id") long id) {
		getAdminService().revertEmailTemplate(id);
		return "redirect:template?id="+id;
	}
	
	@RequestMapping(method=GET)
	public String smtp(Map<String, Object> model) {
		model.put("smtp", currentUser().getCompany().getSmtp());
	    model.put("smtpSidebar", true);
		return "email/smtp";
	}
	
	@Transactional
	@RequestMapping(method=POST)
	public String updateSmtp(
			@ModelAttribute("smtp") Smtp cmd,
			BindingResult binding,
			Map<String,Object> model) {
			
			getSession().beManual();
			
			Company company = currentUser().getCompany();
			company.setSmtp(cmd);
			getAdminService().saveCompany(company);
			
			getSession().flush();
			
			return "redirect:smtp";
	}
	
	@RequestMapping(method=GET)
	public String emailConnect(Map<String, Object> model) {
		model.put("facilitator", currentUser().getCompany().getFacilitator());
	    model.put("emailConnectSidebar", true);
		return "email/emailConnect";
	}
	
	@Transactional
	@RequestMapping(method=POST)
	public String updateFacilitator(
			@ModelAttribute("facilitator") Facilitator cmd,
			BindingResult binding,
			Map<String,Object> model) {
			
			getSession().beManual();
			
			Company company = currentUser().getCompany();
			company.setFacilitator(cmd);
			getAdminService().saveCompany(company);
			
			getSession().flush();

			return "redirect:emailConnect";
	}
	
	private void populateEmailTemplates(Map<String,Object> model) {
		model.put(
				"templates", 
				Arrays.asList(
						currentUser().getCompany().getAutoInviteEmailTemplate(),
						currentUser().getCompany().getExamInviteEmailTemplate(),
						currentUser().getCompany().getRejectCandidateEmailTemplate(),
						currentUser().getCompany().getRequestResumeEmailTemplate()
				)
		);
	}
}
