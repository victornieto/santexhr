package org.openapplicant.web.controller;

import org.apache.commons.lang.StringUtils;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateSearch;
import org.openapplicant.domain.ExamDefinition;
import org.openapplicant.domain.Note;
import org.openapplicant.domain.event.AddNoteToCandidateEvent;
import org.openapplicant.domain.event.CandidateWorkFlowEvent;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.util.Pagination;
import org.openapplicant.web.view.CandidateNoteView;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
public class CandidatesController extends AdminController {
	
	private static final Pagination pagination = Pagination.oneBased().perPage(10000); // FIXME: we need real pagination!!!
	
	private static final String INDEX_VIEW = "candidates/index";
	
	private static final String DETAIL_VIEW = "candidates/detail";
	
	@RequestMapping(method=GET)
	public String index(Map<String,Object> model) {
		return "redirect:active";
	}
	
	@RequestMapping(method=GET)
	public String all(Map<String, Object> model) {
		List<Candidate> candidates = getAdminService().findAllCandidatesByCompany(
				currentUser().getCompany(),
				pagination.forPage(1)
		);
		
		populateModelForIndex(model, candidates);
		model.put("candidatesSidebar", true);
		return INDEX_VIEW;
	}
	
	@RequestMapping(method=GET)
	public String active(Map<String, Object> model) {
		List<Candidate> candidates = getAdminService().findAllActiveCandidatesByCompany(
				currentUser().getCompany(), 
				pagination.forPage(1)
		);
		populateModelForIndex(model, candidates);
		model.put("viewActive", true);
		return INDEX_VIEW;
	}
	
	@RequestMapping(method=GET)
	public String archived(Map<String, Object> model) {
		List<Candidate> candidates = getAdminService().findAllArchivedCandidatesByCompany(
				currentUser().getCompany(),
				pagination.forPage(1)
		);
		populateModelForIndex(model, candidates);
		model.put("viewArchived", true);
		return INDEX_VIEW;
	}
	
	@RequestMapping(method=GET)
	public String search(
			@RequestParam(value="q", required=false) String fullTextQuery,
			Map<String,Object> model) {
		
		CandidateSearch search = getAdminService().createTextCandidateSearch(
				currentUser(), 
				fullTextQuery
		);
		List<Candidate> candidates = search.execute(pagination.forPage(1));
		
		model.put("fullTextQuery", fullTextQuery);
		model.put("searchId", search.getId());
		populateModelForIndex(model, candidates);
		
		return INDEX_VIEW;
	}
	
	@RequestMapping(method=GET)
	public String find(
			@RequestParam(value="name", required=false) String nameQuery,
			@RequestParam(value="skills", required=false) String skillsQuery,
			@RequestParam(value="dates", required=false) String datesQuery,
			Map<String,Object> model){
		
		CandidateSearch search = getAdminService().createPropertyCandidateSearch(
				currentUser(), 
				nameQuery, 
				skillsQuery, 
				datesQuery
		);
		List<Candidate> candidates = search.execute(pagination.forPage(1));
		
		model.put("searchId", search.getId());
		populateModelForIndex(model, candidates);
		
		return INDEX_VIEW;
	}
	
	@RequestMapping(method=GET)
	public String recentsearch(
			@RequestParam("id") long searchId,
			Map<String,Object> model) {
		
		List<Candidate> candidates = getAdminService().findAllCandidatesBySearchId(
				searchId,
				pagination.forPage(1)
		);
		
		model.put("searchId", searchId);
		populateModelForIndex(model, candidates);
		
		return INDEX_VIEW;
	}
	
	@RequestMapping(method=GET)
	public String viewstatus(
			@RequestParam(value="status") String statusStr,
			Map<String,Object> model) {
		
		Candidate.Status status = Candidate.Status.valueOf(statusStr);
		List<Candidate> candidates = getAdminService().findAllCandidatesByCompanyAndStatus(
				currentUser().getCompany(), 
				status, 
				pagination.forPage(1)
		);
		
		model.put("view_status", status);
		populateModelForIndex(model, candidates);
		
		return INDEX_VIEW;
	}
	
	private void populateModelForIndex(Map<String,Object> model, List<Candidate> candidates) {
		List<CandidateSearch> searches = getAdminService().findAllCandidateSearchesByUser(
				currentUser(), 
				Pagination.oneBased().perPage(4)
		);
		model.put("searches", searches);
		model.put("candidates", candidates);
		model.put("activeStatuses", Candidate.Status.getActiveStatuses());
		model.put("archivedStatuses", Candidate.Status.getArchivedStatuses());
	}
	
	@RequestMapping(method=GET)
	public String updateStatus(
			@RequestParam("id") long id, 
			@RequestParam("status") String updatedStatus,
			@RequestParam(value="view_status", required=false) String viewStatus,
			@RequestParam(value="search_id", required=false) String searchId) {
		
		Candidate.Status status = Candidate.Status.valueOf(updatedStatus);
		
		getAdminService().updateCandidateStatus(id, status, currentUser());
		
		if (StringUtils.isNotEmpty(viewStatus)) {
			return "redirect:viewstatus?status="+viewStatus;
		} else if (StringUtils.isNotEmpty(searchId)) {
			return "redirect:recentsearch?id="+searchId;
		} else {
			return "redirect:all";
		}
	}
	
	@RequestMapping(method=GET)
	public String create(Map<String,Object> model) {
		Candidate candidate = new Candidate();
		
		model.put("candidateIsNew", true);
		populateModelForDetail(model, candidate);
		
		return DETAIL_VIEW;
	}
	
	@RequestMapping(method=POST)
	public String add_note(
			@RequestParam(value="id") Long id,
			@RequestParam(value="note") String body,
			Map<String,Object> model) {

        Candidate candidate = getAdminService().findCandidateById(id);
		if(StringUtils.isBlank(body)) {
			model.put("noteError", "Please enter text");
			populateModelForDetail(model, candidate);
			return DETAIL_VIEW;
		}
		Note note = new Note(currentUser(), body);
        Errors errors = note.validate();
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors("body")) {
                model.put("noteError", errors.getFieldError("body").getDefaultMessage());
            }
            model.put("body", body);
            populateModelForDetail(model, candidate);
            return DETAIL_VIEW;
        }
		getAdminService().addNoteToCandidate(id, note);
		return redirectToDetail(id);
	}
	
	@RequestMapping(method=GET)
	public String detail(
		@RequestParam(value="id") Long id,
		@RequestParam(value="ed", required=false) Long examDefinitionId,
        @RequestParam(required = false, value = "success") Boolean success,
		Map<String,Object> model) {
		
		Candidate candidate = getAdminService().findCandidateById(id);
		
		populateModelForDetail(model, candidate);
        if (success != null && success) {
            model.put("success", success);
        }
		if (examDefinitionId != null) {
			model.put("examDefinitionId", examDefinitionId);
		}
		return DETAIL_VIEW;
	}
	
	@RequestMapping(method=POST)
	public String history(
			@RequestParam(value="id") Long id,
			Map<String,Object> model) {
		
		Candidate candidate = getAdminService().findCandidateById(id);
		List<CandidateWorkFlowEvent> events = 
			getAdminService().findAllCandidateWorkFlowEventsByCandidateId(candidate.getId());
		
		model.put("candidate", candidate);
		model.put("events", events);
		
		return "candidates/history";
	}
	
	@RequestMapping(method=GET)
	public String create_link(
			@RequestParam(value="id") Long id,
			@RequestParam(value="ed") Long examDefinitionId,
			Map<String, Object> model) {
		
		Candidate candidate = getAdminService().findCandidateById(id);
		
		List<ExamDefinition> examDefinitions = new ArrayList<ExamDefinition>();
		examDefinitions.add(getAdminService().findExamDefinitionById(examDefinitionId));
		try {
			getAdminService().createExamLink(currentUser(), candidate, examDefinitions);
		} catch (IllegalStateException e) {
			model.put("createLinkError", e.getLocalizedMessage());
			populateModelForDetail(model, candidate);
			model.put("examDefinitionId", examDefinitionId);
			return DETAIL_VIEW;
		}
		return redirectToDetail(candidate.getId(), examDefinitionId);
	}
	
	@RequestMapping(method=POST)
	public String emailTemplate(
			@RequestParam(value="id") long examLinkId,
			Map<String,Object> model) {
		
		CandidateExamLink examLink = getAdminService().findCandidateExamLinkById(examLinkId);
		
		SimpleMailMessage message = currentUser()
									.getCompany()
									.getExamInviteEmailTemplate()
									.compose(examLink);
		
		Assert.state(message.getTo().length == 1, "Should only have 1 recipient");
		
		model.put("to", message.getTo()[0]);
		model.put("from", message.getFrom());
		model.put("subject",message.getSubject());
		model.put("body", message.getText());
		model.put("candidateId", examLink.getCandidate().getId());
		model.put("examLinkId", examLinkId);
		return "candidates/emailLinkDialog";
	}
	
	@RequestMapping(method=POST)
	public String sendEmail(
				@RequestParam("candidateId") long candidateId,
				@RequestParam("examLinkId") long examLinkId,
				@RequestParam(value="to") String to,
				@RequestParam(value="from") String from,
				@RequestParam(value="subject", required=false) String subject,
				@RequestParam(value="body", required=false) String body) {
		
		// TODO: validate that body contains examLink url
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setFrom(from);
		message.setSubject(subject);
		message.setText(body);
		
		getAdminService().sendExamInviteEmail(currentUser(), examLinkId, message);
		return redirectToDetail(candidateId);
	}
	
	@Transactional
	@RequestMapping(method=POST)
	public String update(
			@ModelAttribute("candidate") Candidate cmd,
			BindingResult binding,
			@RequestParam(value="save", required=false) String save,
			Map<String,Object> model) {
		
		if(save == null) {
            return "redirect:/admin/candidates/all";
		}

		getSession().beManual();

        Candidate candidate;
        if (cmd.getId() != null && cmd.getId() > 0) {
		    candidate = getAdminService().findCandidateById(cmd.getId());
        } else {
            candidate = getAdminService().createCandidate(currentUser());
        }

        candidate.setEmail(cmd.getEmail());
        candidate.setCellPhoneNumber(cmd.getCellPhoneNumber());
        candidate.setHomePhoneNumber(cmd.getHomePhoneNumber());
        candidate.setWorkPhoneNumber(cmd.getWorkPhoneNumber());
        candidate.setAddress(cmd.getAddress());
        candidate.setName(cmd.getName());
		Errors errors = candidate.validate();
		if(errors.hasErrors()) {
			binding.addAllErrors(errors);
			populateModelForDetail(model, candidate);
			return DETAIL_VIEW;
		} else {
			getAdminService().saveCandidate(candidate, currentUser());
			getSession().flush();
            if (cmd.getId() == null) {
                model.put("success", Boolean.TRUE);
            }
            return redirectToDetail(candidate.getId());
		}
	}

    @RequestMapping(method = GET)
    public String notes(Map<String, Object> model) {
        List<Candidate> candidates = getAdminService().findAllCandidatesByCompany(currentUser().getCompany(), Pagination.oneBased());
        List<CandidateNoteView> candidatesNotes = new ArrayList<CandidateNoteView>();
        for (Candidate candidate : candidates) {
            final List<AddNoteToCandidateEvent> addNoteToCandidateEvents = getAdminService().findAddNoteToCandidateEventsByCandidateId(candidate.getId());
            for (Note note : candidate.getNotes()) {
                candidatesNotes.add(new CandidateNoteView(candidate, note));
            }
        }
        model.put("candidatesNotes", candidatesNotes);
        return "candidates/notes";
    }
	
	private void populateModelForDetail(Map<String,Object> model, Candidate candidate) {
		List<CandidateWorkFlowEvent> events = 
			getAdminService().findAllCandidateWorkFlowEventsByCandidateId(candidate.getId());
		
		List<ExamDefinition> examDefinitions =
			getAdminService().findAllExamDefinitionsByCompany(currentUser().getCompany());
		
		List <CandidateExamLink> examLinks = 
			getAdminService().getExamLinksForCandidate(candidate);
		
		model.put("activeStatuses", Candidate.Status.getActiveStatuses());
		model.put("archivedStatuses", Candidate.Status.getArchivedStatuses());
		model.put("examDefinitions", examDefinitions);
		model.put("examLinks", examLinks);
		model.put("candidate", candidate);
		model.put("events", events);
	}
	
	private String redirectToDetail(Long candidateId) {
        return "redirect:detail?id="+candidateId;
	}
	
	private String redirectToDetail(Long candidateId, Long examId) {
		return redirectToDetail(candidateId) + "&ed=" + examId;
	}
}
