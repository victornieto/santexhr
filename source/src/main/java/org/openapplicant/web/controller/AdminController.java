package org.openapplicant.web.controller;

import org.openapplicant.dao.ISessionFacade;
import org.openapplicant.domain.User;
import org.openapplicant.security.ICurrentUserService;
import org.openapplicant.service.AdminService;
import org.springframework.beans.factory.annotation.Required;


/**
 * Provides common methods to admin portal controllers
 */
public abstract class AdminController {
	
	private AdminService adminService;
	
	private ICurrentUserService currentUserService;
	
	private ISessionFacade session;
	
	public AdminService getAdminService() {
		return adminService;
	}
	
	@Required
	public void setAdminService(AdminService value) {
		adminService = value;
	}
	
	@Required
	public void setCurrentUserService(ICurrentUserService value) {
		currentUserService = value;
	}
	
	@Required
	public void setSessionFacade(ISessionFacade value) {
		session = value;
	}
	
	/**
	 * @return the current admin portal user.
	 */
	protected User currentUser() {
		return currentUserService.getCurrentUser();
	}
	
	/**
	 * @return the current persistence manager (eg. Hibernate Session)
	 */
	public ISessionFacade getSession() {
		return session;
	}
}
