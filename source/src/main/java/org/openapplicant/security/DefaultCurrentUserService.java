package org.openapplicant.security;

import org.openapplicant.domain.User;
import org.openapplicant.service.AdminService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;


public class DefaultCurrentUserService implements ICurrentUserService {

	private AdminService adminService;
	
	@Required
	public void setAdminService(AdminService value) {
		adminService = value;
	}
	
	public User getCurrentUser() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return adminService.findUserById(((OpenApplicantUserDetails)obj).getUserId());
	}
}
