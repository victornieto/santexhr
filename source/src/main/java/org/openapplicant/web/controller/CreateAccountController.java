package org.openapplicant.web.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.domain.AccountCreationToken;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.User;
import org.openapplicant.domain.User.Role;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
public class CreateAccountController extends AdminController {
	
	private static final Log logger = LogFactory.getLog(CreateAccountController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public String index() {
		logger.info("Account creation controller called with GET.");
		return "createAccount/index";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String create(@RequestParam("token") String guid,Map<String,Object> model) {
		logger.info("Calling create with token "+guid);
		
		AccountCreationToken token = getAdminService().findAccountCreationTokenByGuid(guid);
		
		String companyName = token.getCompanyName().toLowerCase().replaceAll("[^a-z]+", "");
		String email = token.getContactEmail();
		
		model.put("token",guid);
		model.put("company",companyName);
		model.put("email", email);
		
		return "createAccount/create";

	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public String create(@RequestParam("alias") String alias, 
			@RequestParam("user1email") String user1email, 
			@RequestParam("user1name") String user1name,
			@RequestParam("user1role") String user1role,
			@RequestParam("user2email") String user2email, 
			@RequestParam("user2name") String user2name,
			@RequestParam("user2role") String user2role,
			@RequestParam("user3email") String user3email, 
			@RequestParam("user3name") String user3name,
			@RequestParam("user3role") String user3role,
			@RequestParam("token") String guid,
				Map<String,Object> model) {
		
		AccountCreationToken token = getAdminService().findAccountCreationTokenByGuid(guid);
		if ((token == null) || (token.getCompany() != null)) {
			logger.info("Tried adding from an invalid or previously-used token.");
			return "createAccount/error";
		}
		
		
		
		Company company = new Company();
		company.setEmailAlias(alias);
		company.setHostName(alias + ".openapplicant.org");
		company.setHostPort(80);
		company.setName(token.getCompanyName());
		company.setProxyName(alias + ".localhost");
		company.setProxyPort(8080);
		
		Errors errors = company.validate();
		if (errors.hasFieldErrors("emailAlias")) {
			model.put("error", errors.getFieldError("emailAlias"));
			return "createAccount/error";
		}
	
		
		getAdminService().saveCompany(company);
		
		User user;
		logger.info("User to add is "+user1email);
		if (!StringUtils.isBlank(user1email)) {
			logger.info("Adding first user.");
			user = new User();
			user.setEmail(user1email);
			user.setName(new Name(user1name));
			user.setEnabled(true);
			user.setPassword("hello!");
			company.addUser(user);
			if(user1role.equals("Admin"))
				user.setRole(Role.ROLE_ADMIN);
			else if (user1role.equals("Settings"))
				user.setRole(Role.ROLE_SETTINGS);
			else if (user1role.equals("Grader"))
				user.setRole(Role.ROLE_GRADER);
			else if (user1role.equals("HR"))
				user.setRole(Role.ROLE_HR);
			errors = user.validate();
			if (errors.hasErrors()) {
				model.put("error","error validating user.");
				return "createAccount/error";
			}
			getAdminService().saveUser(user);
		}
		if (!StringUtils.isBlank(user2email)) {
			logger.info("Adding second user.");
			user = new User();
			user.setEmail(user2email);
			user.setName(new Name(user2name));
			user.setEnabled(true);
			user.setPassword("hello!");
			company.addUser(user);
			if(user2role.equals("Admin"))
				user.setRole(Role.ROLE_ADMIN);
			else if (user2role.equals("Settings"))
				user.setRole(Role.ROLE_SETTINGS);
			else if (user2role.equals("Grader"))
				user.setRole(Role.ROLE_GRADER);
			else if (user2role.equals("HR"))
				user.setRole(Role.ROLE_HR);
			getAdminService().saveUser(user);
		}
		if (!StringUtils.isBlank(user3email)) {
			logger.info("Adding third user");
			user = new User();
			user.setEmail(user3email);
			user.setName(new Name(user3name));
			user.setEnabled(true);
			user.setPassword("hello!");
			company.addUser(user);
			if(user3role.equals("Admin"))
				user.setRole(Role.ROLE_ADMIN);
			else if (user3role.equals("Settings"))
				user.setRole(Role.ROLE_SETTINGS);
			else if (user3role.equals("Grader"))
				user.setRole(Role.ROLE_GRADER);
			else if (user3role.equals("HR"))
				user.setRole(Role.ROLE_HR);
			getAdminService().saveUser(user);
		}
		
		
		String longQuestion = "What does the following C code do?:\n"
			+"\n"
			+"<pre>int w = 10, x = 0, z = 0;\n"
			+"int y = x++;\n"
			+"\n"
			+"for ( ; w >= 0; w-- )\n"
			+"{\n"
			+"z = x + y;\n"
			+"printf( \"%i, \", y = x );\n"
			+"x = z;\n"
			+"}</pre>\n";

		// TODO send email to sales staff
		SimpleMailMessage salesMail = new SimpleMailMessage();
		salesMail.setTo("mrw@originatelabs.com");
		salesMail.setSubject("Santex HR Account Creation Process Completed for "+token.getCompanyName());
		salesMail.setFrom("Santex HR Account Creation <openapplicant-account@openapplicant.org>");
		String salesBody = "A company has started their demo account via the website form\n" 
			+ " - Name "+company.getName()+"\n"
			+ " - Contact: "+token.getContactName()+"\n"
			+ " - Company alias: "+company.getEmailAlias()+"\n"
			+ " - Company ID: "+company.getId()+"\n\n";
				
		salesMail.setText(salesBody);
		getAdminService().sendAccountCreationEmail(salesMail, company);
		
		
		getAdminService().saveCompany(company);

		
		return "createAccount/success";
	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public String processForm(
			@RequestParam("company") String companyName,
			@RequestParam("contact") String contactName,
			@RequestParam("phone") String phone,
			@RequestParam("adminEmail") String email,
			@RequestParam("street") String street,
			@RequestParam("city") String city,
			@RequestParam("state") String state,
			@RequestParam("zip") String zip,
			@RequestParam("sourcepage") String sourcePage) {

	
		//getSession().beManual();
		
		logger.info("Calling confirmation controller.");

		// TODO - check to see if this user is already in the DB
		
		
		AccountCreationToken token = new AccountCreationToken();
		token.setCompanyName(companyName);
		token.setContactName(contactName);
		token.setContactPhone(phone);
		token.setContactEmail(email);
		token.setStreet(street);
		token.setCity(city);
		token.setState(state);
		token.setZipCode(zip);
		
		getAdminService().saveAccountCreationToken(token);
		logger.info("Saved account creation token.");
		
		// TODO send email to user
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom("Santex HR Account Creation <openapplicant-account@openapplicant.org>");
		message.setSubject("Santex HR Account Creation Confirmation");
		String body = "To complete the Santex HR Account Creation process, please visit the following URL:\n"
			+ "http://beta.openapplicant.org/openapplicant/admin/createAccount/create?token="+token.getEntityInfo().getBusinessGuid();
		message.setText(body);
		getAdminService().sendAccountCreationEmail(message, token.getCompany());

		// TODO send email to sales staff
		SimpleMailMessage salesMail = new SimpleMailMessage();
		salesMail.setTo("mrw@originatelabs.com");
		salesMail.setSubject("Santex HR Account Creation Process Started for "+token.getCompanyName());
		salesMail.setFrom("Santex HR Account Creation <openapplicant-account@openapplicant.org>");
		String salesBody = "A user has started the account creation process.\n"
			+" - Company: "+token.getCompanyName()+"\n"
			+" - Contact Name: "+token.getContactEmail()+"\n"
			+" - Contact Email: "+token.getContactEmail()+"\n"
			+" - Contact Phone: "+token.getContactPhone()+"\n"
			+" - Address: "+token.getStreet()+ "\n" + token.getCity() + " " + token.getState() + " " +token.getZipCode() +"\n"
			+"\n\n"
			+"The token provided to them was "+token.getGuid()+"\n";
		salesMail.setText(salesBody);
		getAdminService().sendAccountCreationEmail(salesMail, token.getCompany());
		
		return "createAccount/sentMail";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String success() {
		logger.debug("Calling success page.");
		return "createAccount/success";
	}
}
