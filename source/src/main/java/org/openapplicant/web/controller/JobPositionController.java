package org.openapplicant.web.controller;

import org.openapplicant.domain.JobPosition;
import org.openapplicant.domain.Seniority;
import org.openapplicant.util.Messages;
import org.openapplicant.util.Strings;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
public class JobPositionController extends AdminController {
	
	@RequestMapping(method=GET)
	public String index(Map<String, Object> model) {
		Collection<JobPosition> jobPositions = getAdminService().findJobPositionsByCompany(currentUser().getCompany());
		model.put("jobPositions", jobPositions);
	    model.put("jobPositionsSidebar", true);
		return "jobPositions/index";
	}

    @RequestMapping(method = GET)
    public String view(@RequestParam(required = false, value = "jp") Long id,
                       Map<String, Object> model) {
        if (id != null && id > 0) {
            model.put("jobPosition", getAdminService().findJobPositionById(id));
        } else {
            model.put("jobPosition", new JobPosition());
        }
        model.put("seniorities", Seniority.getList());
        return "jobPositions/view";
    }

    @RequestMapping(method = POST)
    public String update(
            @ModelAttribute JobPosition jobPosition,
            BindingResult bindingResult,
            @RequestParam(required = false) String save,
            Map<String, Object> model) {
        if (save == null) {
            return "redirect:index";
        }
        if (jobPosition.getId() != null && jobPosition.getId() > 0) {
            Errors errors = jobPosition.validate();
            if (!errors.hasErrors()) {
                getAdminService().updateJobPositionInfo(jobPosition.getId(), jobPosition.getName(), jobPosition.getSeniorities());
            } else {
                bindingResult.addAllErrors(errors);
            }
        } else {
            jobPosition.setCompany(currentUser().getCompany());
            Errors errors = jobPosition.validate();
            if (!errors.hasErrors()) {
                jobPosition = getAdminService().saveJobPosition(jobPosition);
                model.put("success", true);
            } else {
                bindingResult.addAllErrors(errors);
            }
        }
        model.put("seniorities", Seniority.getList());
        model.put("jobPosition", jobPosition);
        return "jobPositions/view";
    }

    @RequestMapping(method = POST)
    public String deleteJobPosition(@RequestParam("jp") Long id,
                                    Map<String, Object> model) {
        try {
            getAdminService().deleteJobPositionWithId(id);
        } catch (DataIntegrityViolationException e) {
            model.put("error", Messages.getJobPositionDeleteDataIntegrityViolationExceptionText());
        }
        return index(model);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Seniority.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(Seniority.valueOf(Strings.dehumanize(text)));
            }
        });
    }
}
