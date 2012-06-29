package org.openapplicant.security;

import java.io.IOException;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openapplicant.service.AdminService;
import org.springframework.security.Authentication;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;


/**
 * A custom AuthenticationProcessingFilter for the admin portal.  This filter
 * adds the following behavior to AuthenticationProcessingFilter.
 * 
 * 1. Restore the location hash (ajax history) of a request url.  
 * The browser's location hash should be passed to the server as a 
 * request parameter.  The default parameter name is "location_hash".
 * 
 * 2.  Record the authenticated user's last login time. 
 * 
 * 3.  Configure the user's time zone.  The time zone offset in minutes should
 * be passed to the server as a request parameter.  The default parameter name
 * is "timezone_offset"
 */
public class AdminAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

	private String historyParameterName = "location_hash";
	
	private String timeZoneOffsetParameterName = "timezone_offset";
	
	private AdminService adminService;
	
	public void setHistoryParameterName(String value) {
		historyParameterName = value;
	}
	
	public void setTimeZoneOffsetParameterName(String value) {
		timeZoneOffsetParameterName = value;
	}
	
	public void setAdminService(AdminService value) {
		adminService = value;
	}
	
	@Override
	public void onSuccessfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Authentication authResult) 
			throws IOException{
		
		super.onSuccessfulAuthentication(request, response, authResult);
		
		updateLastLoginTime(authResult);
		setTimeZoneOffset(request);
	}
	
	private void updateLastLoginTime(Authentication auth) {
		OpenApplicantUserDetails details = (OpenApplicantUserDetails) auth.getPrincipal();
		adminService.updateUserLastLoginTime(details.getUserId());
	}
	
	private void setTimeZoneOffset(HttpServletRequest request) {
		if(StringUtils.isBlank(request.getParameter(timeZoneOffsetParameterName))) {
			return;
		}
		int offsetMinutes = Integer.parseInt(request.getParameter(timeZoneOffsetParameterName));
		TimeZone timeZone = TimeZone.getTimeZone("GMT");
		timeZone.setRawOffset((int)(offsetMinutes * DateUtils.MILLIS_PER_MINUTE));
		Config.set(request.getSession(), Config.FMT_TIME_ZONE, timeZone);
	}
	
	@Override
	protected void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {

		String hash = getAjaxBookmark(request);
		if(hash != null) {
			url += hash;
		}
		super.sendRedirect(request, response, url);
	}
	
	private String getAjaxBookmark(HttpServletRequest request) {
		if(request.getParameter(historyParameterName) == null) {
			return null;
		}
		String hash = request.getParameter(historyParameterName);
		hash = hash.startsWith("#") ? hash : "#" + hash;
		return hash;
	}
}
