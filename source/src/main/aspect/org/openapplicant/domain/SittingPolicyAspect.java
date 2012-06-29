package org.openapplicant.domain;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;

/**
 * Enforces Sitting programming policies.
 */
@Aspect
public class SittingPolicyAspect {

	/**
	 * It is an error to access Sitting.setCandidate outside of
	 * Candidate.addSitting.
	 */
	@DeclareError(
			"call(* org.openapplicant.domain.Sitting.setCandidate(..)) && " +
			"!withincode(* org.openapplicant.domain.Candidate.addSitting(..))"
	)
	public static final String setCandidateMessage = 
		"this method is used to set a bidirectional relationship and should only be called from within Candidate.addSitting";
}
