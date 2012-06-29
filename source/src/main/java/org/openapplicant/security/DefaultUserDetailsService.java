package org.openapplicant.security;

import org.openapplicant.domain.User;
import org.openapplicant.service.AdminService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;


/**
 * Default user details service for the admin application.
 */
public class DefaultUserDetailsService implements UserDetailsService {
	
	private AdminService adminService;
	
	public void setAdminService(AdminService value) {
		adminService = value;
	}
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		User user = adminService.findUserByEmail(username);
		return new OpenApplicantUserDetails(user);
	}
}
