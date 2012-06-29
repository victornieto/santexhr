package org.openapplicant.domain;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;

@Aspect
public class QuestionAndResponsePolicyAspect {

	@DeclareError(
			"call(* org.openapplicant.domain.QuestionAndResponse.setResponse(..)) && " +
			"!within(org.openapplicant.domain.Sitting)"
	)
	public static final String errorMessage = 
		"This method should only be called from the Sitting class";
}
