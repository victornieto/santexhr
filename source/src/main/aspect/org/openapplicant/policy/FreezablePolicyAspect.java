package org.openapplicant.policy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareError;

/**
 * Prevents developers from inadvertently modifying a class who's state is 
 * frozen.  This aspect advises only calls made in the openapplicant subpackages,
 * thus allowing bean tools to bypass the state assertion.
 */
@Aspect
public class FreezablePolicyAspect {

	@DeclareError(
			"execution(@org.openapplicant.policy.AssertNotFrozen * *(..)) && " + 
			"!within(org.openapplicant.policy.IFreezable+)"
	)
	public static final String freezableMessage = 
		 "@AssertNotFrozen may only annotate methods of a class implementing the IFreezable interface";
	
	/**
	 * Assert that a method marked as <code>@Freezable</code> must not be 
	 * invoked if the target object's state is frozen.
	 */
	@Before(
			"call(@org.openapplicant.policy.AssertNotFrozen * *(..)) " +
			"&& org.openapplicant.SystemArchitecture.inopenapplicant()"
	)
	public void failIfFrozen(JoinPoint jp) {
		IFreezable target = (IFreezable)jp.getTarget();
		if(target.isFrozen()) {
			throw new IllegalStateException(
					"Attempted to invoke " + jp.getSignature().toLongString() +
					" while frozen"
			);
		}
	}
}
