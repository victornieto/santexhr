package org.openapplicant.security;

import org.apache.commons.lang.Validate;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;

/**
 * UserDetails implementation for the openapplicant application.  This class
 * also stores additional details like user id, and company id.
 */
class OpenApplicantUserDetails implements UserDetails {
	
	//========================================================================
	// MEMBERS
	//=========================================================================
	private final User _user;
	
	private final Long _userId;
	
	private Long _companyId;
	
	/**
	 * Constructs a new user details.
	 * @param user
	 */
	public OpenApplicantUserDetails(org.openapplicant.domain.User user) {
		Validate.notNull(user.getId());
		
		_userId = user.getId();
		
		if(null != user.getCompany()) {
			_companyId = user.getCompany().getId();
		}
		
		_user = new User(
				user.getEmail(),
				user.getPassword(),
				user.isEnabled(), // enabled
				user.isEnabled(), // account non expired
				user.isEnabled(), // credentials non expired
				user.isEnabled(), // account non locked
				new GrantedAuthorityImpl[]{new GrantedAuthorityImpl(user.getRole().name())}
		);
	}

	//========================================================================
	// USER DETAILS IMPLEMENTATION
	//========================================================================
	public GrantedAuthority[] getAuthorities() {
		return _user.getAuthorities();
	}

	public String getPassword() {
		return _user.getPassword();
	}

	public String getUsername() {
		return _user.getUsername();
	}

	public boolean isAccountNonExpired() {
		return _user.isAccountNonExpired();
	}

	public boolean isAccountNonLocked() {
		return _user.isAccountNonLocked();
	}

	public boolean isCredentialsNonExpired() {
		return _user.isCredentialsNonExpired();
	}

	public boolean isEnabled() {
		return _user.isEnabled();
	}
	
	//========================================================================
	// METHODS
	//========================================================================
	public Long getUserId() {
		return _userId;
	}
	
	public Long getCompanyId() {
		return _companyId;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof OpenApplicantUserDetails)) {
			return false;
		}
		if(other == this) {
			return true;
		}
		OpenApplicantUserDetails rhs = (OpenApplicantUserDetails)other;
		return _user.equals(rhs._user);
	}
	
	@Override
	public int hashCode() {
		return _user.hashCode();
	}
	
	@Override
	public String toString() {
		return _user.toString();
	}
}
