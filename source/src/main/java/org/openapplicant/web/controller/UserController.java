package org.openapplicant.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Map;

import org.openapplicant.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController extends AdminController {
	
	@RequestMapping(method=GET)
	public String view(@RequestParam("id") long userId, Map<String, Object> model) {
		User user = getAdminService().findUserById(userId);
		model.put("user", user);
		return "user/view";
	}
	
	@Transactional
	@RequestMapping(method=POST)
	public String update(
			@ModelAttribute("user") User cmd,
			BindingResult binding,
			Map<String, Object> model) {
		
		getSession().beManual();
		
		User user = getAdminService().findUserById(cmd.getId());
		user.setEmail(cmd.getEmail());
		user.setName(cmd.getName());
		user.setEnabled(cmd.isEnabled());
		user.setRole(cmd.getRole());
		
		Errors errors = user.validate();
		if(errors.hasErrors()){
			binding.addAllErrors(errors);
			model.put("user", user);
			return "user/view";
		} else {
			getAdminService().saveUser(user);
			getSession().flush();
			return "redirect:view?id="+user.getId();
		}
	}
	
	@RequestMapping(method=GET)
	public String add(Map<String, Object> model) {
		model.put("user", new User());
		return "user/add";
	}
	
	@RequestMapping(method=POST)
	public String create(
			@ModelAttribute("user") User user,
			BindingResult binding,
			@RequestParam("confirmPassword") String confirmPassword,
			Map<String,Object> model) {
		
		Errors errors = user.validate();
		if(errors.hasErrors() || !user.passwordMatches(confirmPassword)) {
			binding.addAllErrors(errors);
			if(!user.passwordMatches(confirmPassword)) {
				model.put("confirmPasswordError", "Passwords do not match");
			}
			return "user/add";
		} else {
			user = getAdminService().createUser(currentUser().getCompany(), user);
			return "redirect:view?id="+user.getId();
		}
	}
}
