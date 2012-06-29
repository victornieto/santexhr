package org.openapplicant.web.controller;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.ExamDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ExamDefinitionsController extends AdminController {

	@RequestMapping(method=GET)
	public String index(Map<String, Object> model) {
		List<ExamDefinition> examDefinitions = getAdminService().findAllExamDefinitionsByCompany(currentUser().getCompany());
		model.put("examDefinitions", examDefinitions);
		return "examDefinitions/index";
	}
	
	@RequestMapping(method=GET)
	public String site(Map<String, Object> model) {
		Company company = currentUser().getCompany();
		model.put("welcomeText", company.getWelcomeText());
		model.put("completionText", company.getCompletionText());
		return "examDefinitions/site";
	}
	
	@RequestMapping(method=POST)
	public String updateSite(@RequestParam("welcomeText") String welcomeText,
			@RequestParam("completionText") String completionText,
            Map<String, Object> model) {
		Company company = currentUser().getCompany();
		company.setWelcomeText(welcomeText);
		company.setCompletionText(completionText);
        getAdminService().saveCompany(company);
        model.put("welcomeText", company.getWelcomeText());
        model.put("completionText", company.getCompletionText());
		return "examDefinitions/site";
	}

    @RequestMapping(method = POST)
    public String deleteExamDefinition(@RequestParam("ed") String examDefinitionArtifactId,
                                       Map<String, Object> model) {
        getAdminService().deleteExamDefinition(examDefinitionArtifactId);
        List<ExamDefinition> examDefinitions = getAdminService().findAllExamDefinitionsByCompany(currentUser().getCompany());
        model.put("examDefinitions", examDefinitions);
        return  "examDefinitions/index";
    }
}
