package org.openapplicant.policy;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;

@Aspect
public class NeverCallPolicyAspect {

	/**
	 * It is an error to call any method annotated with <code>@NeverCall</code>
	 * in the openapplicant project.
	 */
	@DeclareError(
			"call(@org.openapplicant.policy.NeverCall * *(..)) && " +
			"org.openapplicant.SystemArchitecture.inopenapplicant()"
	)
	public static final String neverCallMessage = "This method should never be called";
	
	@DeclareError(
			"call(@org.openapplicant.policy.NeverCall *.new(..)) && " +
			"org.openapplicant.SystemArchitecture.inopenapplicant()"
	)
	public static final String neverCallConstructorMessage = "This constructor should never be called";
}
