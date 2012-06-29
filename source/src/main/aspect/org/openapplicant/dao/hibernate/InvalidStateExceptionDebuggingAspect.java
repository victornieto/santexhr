package org.openapplicant.dao.hibernate;

import java.util.Arrays;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.validator.InvalidStateException;

/**
 * Hibernate's InvalidStateException frustratingly doesn't print out the 
 * invalid values in its stack trace.  Here's a workaround.
 */
@Aspect
public class InvalidStateExceptionDebuggingAspect {

	@Pointcut("org.openapplicant.SystemArchitecture.controller() || org.openapplicant.SystemArchitecture.dwrOperation()")
	private void targets() {}
	
	@AfterThrowing(pointcut="targets()", throwing="t")
	public void throwMoreHelpfulException(InvalidStateException t) {
		throw new RuntimeException(t.toString() + Arrays.toString(t.getInvalidValues()), t);
	}
}
