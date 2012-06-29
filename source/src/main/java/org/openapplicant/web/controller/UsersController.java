package org.openapplicant.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openapplicant.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UsersController extends AdminController {
	
	@RequestMapping(method=GET)
	public String index(Map<String, Object> model) {
		model.put("users", currentUser().getCompany().getUsers());
	    	model.put("usersSidebar", true);
		return "users/index";
	}
	
	@RequestMapping(method=POST)
	public String toggleEnabled(@RequestParam("id") long userId) {
		User user = getAdminService().findUserById(userId);
		user.toggleEnabled();
		getAdminService().saveUser(user);
		return "redirect:index";
	}
	
	@RequestMapping(method=POST)
	public String changeRole(
			@RequestParam("id") long userId, 
			@RequestParam("role") String role) {
		
		User user = getAdminService().findUserById(userId);
		user.setRole(User.Role.valueOf(role));
		getAdminService().saveUser(user);
		return "redirect:index";
	}
	
	/**
	 * Dwr remote method
	 * @param userId the id of the user who's password to change
	 * @param password the new password
	 * @param confirmPassword 
	 * @return the result model
	 */
	@Transactional
	public Map<String, Object> changePassword(
			long userId, 
			String password,
			String confirmPassword) {
		
		Map<String,Object> model = new HashMap<String,Object>();
		
		if(!StringUtils.equals(password, confirmPassword)) {
			model.put("error", "Passwords do not match");
			return model;
		}
		
		getSession().beManual();
		
		User user = getAdminService().findUserById(userId);
		user.setPassword(password);
		
		Errors errors = user.validate();
		if(errors.hasErrors()) {
			model.put("error", errors.getFieldError().getDefaultMessage());
			return model;
		} else {
			getAdminService().saveUser(user);
			getSession().flush();
			model.put("success", true);
			return model;
		}
	}

}
