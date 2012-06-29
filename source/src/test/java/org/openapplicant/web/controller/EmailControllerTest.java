package org.openapplicant.web.controller;

import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openapplicant.dao.ISessionFacade;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.ProfileBuilder;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.domain.email.AutoInviteEmailTemplateBuilder;
import org.openapplicant.domain.email.EmailTemplate;
import org.openapplicant.security.ICurrentUserService;
import org.openapplicant.service.AdminService;
import org.openapplicant.util.TestUtils;
import org.openapplicant.web.controller.EmailController;
import org.springframework.validation.BeanPropertyBindingResult;


public class EmailControllerTest {
	
	private EmailController emailController;
	
	private ISessionFacade mockSession;
	
	private AdminService mockAdminService;
	
	private ICurrentUserService mockCurrentUserService;
	
	private User currentUser;
	
	@Before
	public void setUp() {
		currentUser = new UserBuilder().build();
		
		emailController = new EmailController();
		mockSession = createMock(ISessionFacade.class);
		mockAdminService = createMock(AdminService.class);
		mockCurrentUserService = createNiceMock(ICurrentUserService.class);
		
		emailController.setSessionFacade(mockSession);
		emailController.setAdminService(mockAdminService);
		emailController.setCurrentUserService(mockCurrentUserService);
		
		expect(mockCurrentUserService.getCurrentUser())
			.andReturn(currentUser)
			.anyTimes();
		replay(mockCurrentUserService);
	}
	
	@Test
	public void updateTemplate_valid() {
		EmailTemplate template = new AutoInviteEmailTemplateBuilder()
										.withId(1L)
										.build();
		
		mockSession.beManual();
		expectLastCall();
		
		expect(mockAdminService.findEmailTemplateById(anyLong()))
			.andReturn(template);
		expect(mockAdminService.saveEmailTemplate(template))
			.andReturn(template);
		
		mockSession.flush();
		expectLastCall();
		
		replay(mockSession, mockAdminService);
		
		Map<String,Object> model = new HashMap<String, Object>();
		String view = emailController.updateTemplate(
				template.getId(), 
				"foo@gmail.com", 
				"foo", 
				"foo", 
				model
		);
		
		verify(mockSession, mockAdminService);
		assertEquals("redirect:template?id="+template.getId(), view);
	}
	
	@Test
	public void updateTemplate_invalidFromAddress() {
		EmailTemplate template = new AutoInviteEmailTemplateBuilder()
										.withId(1L)
										.build();
		
		mockSession.beManual();
		expectLastCall();
		
		expect(mockAdminService.findEmailTemplateById(anyLong()))
			.andReturn(template);
		replay(mockSession, mockAdminService);
		
		Map<String,Object> model = new HashMap<String,Object>();
		String view = emailController.updateTemplate(
				template.getId(), 
				"bar", 
				"foo", 
				"foo", 
				model
		);
		
		verify(mockSession, mockAdminService);
		assertNotNull(model.get("fromAddressError"));
		assertEquals("email/template", view);
		assertEquals(template, model.get("template"));
		assertTrue(model.get("templates") instanceof Collection);
	}
	
	@Test
	public void updatePreferences_adminRecipients() {
		Profile profileCmd = new ProfileBuilder()
									.withDailyReportRecipient(TestUtils.uniqueEmail())
									.withForwardCandidateEmailRecipient(TestUtils.uniqueEmail())
									.withWhiteListRecipient(TestUtils.uniqueEmail())
									.build();
		
		mockSession.beManual();
		expectLastCall();
		
		expect(mockAdminService.saveProfile(currentUser.getCompany().getProfile()))
				.andReturn(currentUser.getCompany().getProfile());
		
		mockSession.flush();
		expectLastCall();
		
		replay(mockSession, mockAdminService);
		
		Map<String,Object> model = new HashMap<String,Object>();
		String view = emailController.updatePreferences(
				profileCmd,
				new BeanPropertyBindingResult(profileCmd, "profile"),
				false,
				false,
				false,
				null,
				"save",
				model
		);
		
		verify(mockSession, mockAdminService);
		assertEquals("redirect:preferences", view);
		assertEquals("", currentUser.getCompany().getProfile().getDailyReportsRecipient());
		assertEquals("", currentUser.getCompany().getProfile().getCandidateEmailsRecipient());
		assertEquals("", currentUser.getCompany().getProfile().getJobBoardEmailsRecipient());
	}

}
