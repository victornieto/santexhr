package org.openapplicant.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Map;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.setting.Facilitator;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SettingsController extends AdminController {
	
	@RequestMapping(method=GET)
	public String index(Map<String, Object> model) {
	    model.put("company", currentUser().getCompany());
	    model.put("examsUrl", currentUser().getCompany().getLinkToAllExams().getUrl());
	    model.put("facilitatorEmailAddress", currentUser().getCompany().getFacilitatorEmailAddress());
	    model.put("settingsSidebar", true);
		return "settings/index";
	}
	
	@Transactional
	@RequestMapping(method=POST)
	public String update(
			@ModelAttribute("company") Company cmd,
			BindingResult binding,
			Map<String,Object> model) {
			
			getSession().beManual();
			
			Company company = currentUser().getCompany();
			
			company.setName(cmd.getName());
			company.setHostName(cmd.getHostName());
			company.setHostPort(cmd.getHostPort());
			
			getAdminService().saveCompany(company);
			getSession().flush();
			return "redirect:emailConnect";
	}
}
