package org.openapplicant.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
public class LoggingAspect {
	
	private static final Log log = LogFactory.getLog(LoggingAspect.class);
	
	@Pointcut(
			"(org.openapplicant.SystemArchitecture.repository() || " +
			"org.openapplicant.SystemArchitecture.controller() || " +
			"org.openapplicant.SystemArchitecture.service()) && " +
			"!org.openapplicant.SystemArchitecture.setterMethod()"
	)
	protected void targets(){}
	
	@AfterThrowing(pointcut="targets()", throwing="t")
	public void logError(JoinPoint jp, Throwable t) {
		if(!log.isErrorEnabled()) {
			return;
		}
		String msg = new StringBuilder()
			.append(formatSignature(jp))
			.append(": ")
			.toString();
		log.error(msg, t);
	}
	
	@Before("targets()") 
	public void logEntering(JoinPoint jp) {
		if(!log.isDebugEnabled()) {
			return;
		}
		String msg =  new StringBuilder()
							.append("Entering ")
							.append(formatSignature(jp))
							.append(": ")
							.append(Arrays.toString(jp.getArgs()))
							.toString();
		log.debug(msg);
	}
	
	@AfterReturning(pointcut="targets()", returning="result")
	public void logExiting(JoinPoint jp , Object result) {
		if(!log.isDebugEnabled()) {
			return;
		}
		String msg = new StringBuilder()
							.append("Exiting ")
							.append(formatSignature(jp))
							.append(": ")
							.append(result)
							.toString();
		log.debug(msg);
	}

	protected String formatSignature(JoinPoint jp) {
		if(null == jp.getTarget() || null == jp.getTarget().getClass()) {
			return null == jp.getSignature() ? "" : jp.getSignature().toLongString();
		}
		return new StringBuilder()
			.append(jp.getTarget().getClass().getName())
			.append(".")
			.append(jp.getSignature().toShortString())
			.toString();
	}
}
