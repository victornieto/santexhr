package org.openapplicant.web.controller;

import static java.lang.Boolean.TRUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map;

import org.openapplicant.domain.Profile;
import org.openapplicant.domain.Profile.Priority;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ScreeningController extends AdminController {
	
	@RequestMapping(method=GET)
	public String index(Map<String, Object> model) {
	    	model.put("screeningSidebar", true);
		return "screening/index";
	}
	
	@RequestMapping(method=GET)
	public String preferences(Map<String, Object> model) {
		model.put("profile", currentUser().getCompany().getProfile());
	    	model.put("preferencesSidebar", true);
		return "screening/preferences";
	}
	
	@Transactional
	@RequestMapping(method=POST)
	public String updatePreferences(
			@ModelAttribute("profile") Profile cmd,
			BindingResult binding,
			@RequestParam(value="minCheckBox", required=false) Boolean minCheck,
			@RequestParam(value="maxCheckBox", required=false) Boolean maxCheck,
			Map<String,Object> model) {
		
		getSession().beManual();
		
		Profile profile = currentUser().getCompany().getProfile();
		if(TRUE.equals(minCheck)) {
			profile.setMinInviteScore(cmd.getMinInviteScore());
		} else {
			profile.setMinInviteScore(null);
		}
		if(TRUE.equals(maxCheck)) {
			profile.setMaxRejectScore(cmd.getMaxRejectScore());
		} else {
			profile.setMaxRejectScore(null);
		}
		profile.setSolicitResumes(cmd.isSolicitResumes());
		
		Errors errors = profile.validate();
		if(errors.hasErrors()) {
			binding.addAllErrors(errors);
			model.put("profile", profile);
			return "screening/preferences";
		} else {
			getAdminService().saveProfile(profile);
			getSession().flush();
			return "redirect:preferences";
		}
	}
	
	@RequestMapping(method=GET)
	public String keywords(Map<String, Object> model) {
		Profile profile = currentUser().getCompany().getProfile();
		model.put("keywords", profile.getKeywords());
		model.put("priorities", Profile.Priority.values());
	    model.put("keywordsSidebar", true);
		return "screening/keywords";
	}
	
	@RequestMapping(method=POST)
	public String updateKeywords(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "priority", required = false) String priority,
			@RequestParam(value = "remove", required = false) List<String> removeKeywordList) {
		
		Profile profile = currentUser().getCompany().getProfile();
		profile.addKeyword(keyword, Priority.valueOf(priority));
		profile.removeKeywords(removeKeywordList);
		getAdminService().saveProfile(profile);
		return "redirect:keywords";
	}

}
