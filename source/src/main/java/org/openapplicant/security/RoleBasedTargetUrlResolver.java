package org.openapplicant.security;

import static org.openapplicant.domain.User.Role.ROLE_SETTINGS;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.ui.TargetUrlResolver;
import org.springframework.security.ui.savedrequest.SavedRequest;

/**
 * Upon login, if the user has a ROLE_SETTINGS role, he's sent directly to 
 * the settings section. Otherwise, the default mechanism is triggered.
 * @author franco.zabarino
 *
 */
public class RoleBasedTargetUrlResolver implements TargetUrlResolver {
	
	private final TargetUrlResolver defaultSpringSecurityTargetUrl;
	
	private final static String SETTINGS_VIEW = "/admin/settings/index";
	
	public RoleBasedTargetUrlResolver(TargetUrlResolver targetUrlResolver) {
		defaultSpringSecurityTargetUrl = targetUrlResolver;
	}

	public String determineTargetUrl(SavedRequest savedRequest,
			HttpServletRequest currentRequest, Authentication auth) {
		GrantedAuthority[] authorities = auth.getAuthorities();
		if (authorities.length == 1 && authorities[0].getAuthority().equals(ROLE_SETTINGS.name())) {
			return SETTINGS_VIEW;
		}
		return defaultSpringSecurityTargetUrl.determineTargetUrl(savedRequest, currentRequest, auth);
	}
}
