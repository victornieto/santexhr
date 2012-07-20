package org.openapplicant.web.controller;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.JobOpening;
import org.openapplicant.domain.JobPosition;
import org.openapplicant.util.Pagination;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * User: Gian Franco Zabarino
 * Date: 16/07/12
 * Time: 13:34
 */
@Controller
public class JobOpeningController extends AdminController {

    private final static Pagination pagination = Pagination.zeroBased().perPage(10000); // FIXME We need real pagination!

    @ModelAttribute(value = "activeStatuses")
    public List<JobOpening.Status> populateActiveStatuses() {
        return JobOpening.Status.getActiveStatus();
    }

    @ModelAttribute(value = "archivedStatuses")
    public List<JobOpening.Status> populateArchivedStatuses() {
        return JobOpening.Status.getArchivedStatus();
    }

    @RequestMapping(method = GET)
    public String all(Map<String, Object> model) {
        model.put("jobOpeningsSidebar", true);
        model.put("jobOpenings", getAdminService().findAllJobOpeningsByCompany(currentUser().getCompany()));
        return "jobOpenings/index";
    }

    @RequestMapping(method = GET)
    public String active(Map<String, Object> model) {
        model.put("jobOpenings", getAdminService().findActiveJobOpeningsByCompany(currentUser().getCompany()));
        model.put("viewActive", true);
        return "jobOpenings/index";
    }

    @RequestMapping(method = GET)
    public String archived(Map<String, Object> model) {
        model.put("jobOpenings", getAdminService().findArchivedJobOpeningsByCompany(currentUser().getCompany()));
        model.put("viewArchived", true);
        return "jobOpenings/index";
    }

    @RequestMapping(method = GET, value = "viewstatus")
    public String viewStatus(@RequestParam JobOpening.Status status,
                             Map<String, Object> model) {
        model.put("jobOpenings", getAdminService().findJobOpeningsByCompanyAndStatus(currentUser().getCompany(), status));
        model.put("view_status", status);
        return "jobOpenings/index";
    }

    @RequestMapping(method = GET)
    public String view(@RequestParam(value = "jo") Long jobOpeningId,
                       Map<String, Object> model) {
        JobOpening jobOpening = getAdminService().findJobOpeningById(jobOpeningId);
        model.put("jobOpening", jobOpening);
        model.put("applicants", jobOpening.getApplicants());
        model.put("jobPositions", getAdminService().findJobPositionsByCompany(currentUser().getCompany()));
        return "jobOpenings/view";
    }

    @RequestMapping(method = GET)
    public String add(Map<String, Object> model) {
        model.put("jobOpening", new JobOpening());
        model.put("jobPositions", getAdminService().findJobPositionsByCompany(currentUser().getCompany()));
        return "jobOpenings/view";
    }

    @RequestMapping(method = POST)
    public String update(@ModelAttribute JobOpening jobOpening,
                         @RequestParam(value = "submit", required = false) String submit,
                         Map<String, Object> model) {
        if (submit != null) {
            model.put("jobPositions", getAdminService().findJobPositionsByCompany(currentUser().getCompany()));
            Errors errors = jobOpening.validate();
            if (errors.hasErrors()) {
                model.put("jobOpening", jobOpening);
                model.put("applicants", jobOpening.getApplicants());
                return "jobOpenings/view";
            }
            if (jobOpening.getId() != null && jobOpening.getId() > 0) {
                getAdminService().updateJobOpeningInfo(
                        jobOpening.getId(),
                        jobOpening.getJobPosition(),
                        jobOpening.getFinishDate(),
                        jobOpening.getClient(),
                        jobOpening.getDescription(),
                        jobOpening.getApplicants()
                );
            } else {
                jobOpening.setCompany(currentUser().getCompany());
                getAdminService().saveJobOpening(jobOpening);
            }
            model.put("applicants", jobOpening.getApplicants());
            model.put("jobOpening", jobOpening);
            model.put("success", Boolean.TRUE);
            return "jobOpenings/view";
        }
        return "redirect:all";
    }

    @RequestMapping(method = POST)
    public String listCandidatesForJobOpening(
            @RequestParam(value = "aap", required = false) List<Candidate> applicantsAlreadyPresent,
            Map<String, Object> model) {
        List<Candidate> candidates = getAdminService().findAllCandidatesByCompany(currentUser().getCompany(), pagination);
        if (applicantsAlreadyPresent != null) {
            for (Candidate applicant : applicantsAlreadyPresent) {
                if (candidates.contains(applicant)) {
                    candidates.remove(applicant);
                }
            }
        }
        model.put("candidates", candidates);
        return "jobOpenings/candidates";
    }

    @RequestMapping(method = POST)
    public String addCandidatesToJobOpening(
            @RequestParam(value = "aap", required = false) List<Candidate> applicantsAlreadyPresent,
            @RequestParam(value = "ata", required = false) List<Candidate> applicantsToAdd,
            Map<String, Object> model) {
        List<Candidate> applicantsList = new ArrayList<Candidate>();
        if (applicantsAlreadyPresent != null) {
            applicantsList.addAll(applicantsAlreadyPresent);
        }
        if (applicantsToAdd != null) {
            applicantsList.addAll(applicantsToAdd);
        }
        model.put("applicants", applicantsList);
        return "jobOpenings/applicants";
    }

    @RequestMapping(method = POST)
    public String deleteApplicantsFromJobOpening(
            @RequestParam(value = "aap", required = false) List<Candidate> applicantsAlreadyPresent,
            @RequestParam(value = "atd", required = false) List<Candidate> applicantsToDelete,
            Map<String, Object> model) {
        List<Candidate> applicantsList = new ArrayList<Candidate>();
        if (applicantsAlreadyPresent!= null) {
            applicantsList.addAll(applicantsAlreadyPresent);
            if (applicantsToDelete != null) {
                applicantsList.removeAll(applicantsToDelete);
            }
        }
        model.put("applicants", applicantsList);
        return "jobOpenings/applicants";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, Locale locale) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormat.getDateInstance(DateFormat.SHORT, locale), false));
        binder.registerCustomEditor(JobPosition.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                if (this.getValue() != null && this.getValue() instanceof JobPosition) {
                    return ((JobPosition)getValue()).getId().toString();
                }
                return super.getAsText();
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.hasText(text)) {
                    setValue(getAdminService().findJobPositionById(Long.parseLong(text)));
                }
            }
        });
        binder.registerCustomEditor(Candidate.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                if (this.getValue() != null && this.getValue() instanceof Candidate) {
                    return ((Candidate)getValue()).getId().toString();
                }
                return super.getAsText();
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.hasText(text)) {
                    setValue(getAdminService().findCandidateById(Long.parseLong(text)));
                }
            }
        });
    }
}
