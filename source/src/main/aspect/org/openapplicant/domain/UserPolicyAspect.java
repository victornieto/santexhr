package org.openapplicant.domain;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;

/**
 * Enforces User programming policies.
 */
@Aspect
public class UserPolicyAspect {

	/**
	 * It is an error to call User.setCompany outside of the Company.addUser
	 * or Company.removeUser.
	 */
	@DeclareError(
			"call(* org.openapplicant.domain.User.setCompany(..)) && " + 
			"!(withincode(* org.openapplicant.domain.Company.addUser(..)) || " +
			"withincode(* org.openapplicant.domain.Company.removeUser(..)))"
	)
	public static final String setCompanyMessage = 
		"this method is used to set a bidirectional relationship and should only be called within Company.addUser or Company.removeUser";
}
