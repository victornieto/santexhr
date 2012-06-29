package org.openapplicant.web.controller;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openapplicant.domain.PasswordRecoveryToken;
import org.openapplicant.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ForgotPasswordController extends AdminController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String index() {
		return "forgotPassword/index";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String sendEmail(@RequestParam("email") String email, Map<String,Object> model) {
		if(StringUtils.isBlank(email)) {
			model.put("error", "Required field cannot be left blank");
			return "forgotPassword/index";
		}
		
		User user = getAdminService().findUserByEmailOrNull(email);
		if(user == null) {
			model.put("email", email);
			return "forgotPassword/tryAgain";
		} else {
			getAdminService().sendPasswordRecoveryEmail(new PasswordRecoveryToken(user));
			model.put("email", email);
			return "redirect:emailSent";
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String emailSent(@RequestParam("email") String email, Map<String,Object> model) {
		model.put("email", email);
		return "forgotPassword/emailSent";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String confirm(@RequestParam("id") String guid, Map<String, Object> model) {
		PasswordRecoveryToken token = getAdminService().findPasswordRecoveryTokenByGuid(guid);
		model.put("userId", token.getUser().getId());
		return "forgotPassword/confirm";
	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public String doConfirm(
			@RequestParam("userId") long id,
			@RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword,
			Map<String, Object> model) {
		
		if(!StringUtils.equals(password, confirmPassword)) {
			model.put("confirmError", "Passwords do not match");
			model.put("userId", id);
			return "forgotPassword/confirm";
		}
	
		getSession().beManual();
		
		User user = getAdminService().findUserById(id);
		user.setPassword(password);
		Errors errors = user.validate();
		if(errors.hasErrors()) {
			model.put("passwordError", errors.getFieldError("password").getDefaultMessage());
			model.put("userId", id);
			return "forgotPassword/confirm";
		} else {
			// FIXME: too much business logic here
			getAdminService().saveUser(user);
			getAdminService().deleteAllPasswordRecoveryTokensForUser(user);
			getSession().flush();
			return "redirect:success";
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String success() {
		return "forgotPassword/success";
	}
}
