package org.openapplicant;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Captures common pointcut definitions.
 */
@Aspect
public class SystemArchitecture {
	
	/**
	 * A join point is in openapplicant if the method is defined in a type in
	 * the org.openapplicant package or any sub package under that.
	 */
	@Pointcut("within(org.openapplicant..*)")
	public void inopenapplicant() {}
	
	/**
	 * A join point is in the service layer if the method is defined
	 * in a type in the org.openapplicant.service package or any sub package
	 * under that.
	 */
	@Pointcut("within(org.openapplicant.service..*)")
	public void inServiceLayer(){}
	
	/**
	 * A service operation is the execution of any public method 
	 * defined on a type in the org.openapplicant.service package.
	 */
	@Pointcut("execution(public * org.openapplicant.service.*.*(..))")
	public void serviceOperation(){}
	
	/**
	 * A join point is in the data access layer if the method is defined
	 * in a type in the org.openapplicant.dao package or any sub-package under
	 * that.
	 */
	@Pointcut("within(org.openapplicant.dao..*)")
	public void inDataAccessLayer() {}
	
	/**
	 * A join point is in the hibernate data access layer if the method is 
	 * defined in a type in the org.openapplicant.dao.hiberate package or any 
	 * sub-package under that.
	 */
	@Pointcut("within(org.openapplicant.dao.hibernate..*)")
	public void inHibernateDataAccessLayer() {}
	
	/**
	 * A hibernate data access operation is the execution of any public method
	 * defined on a type in the org.openapplicant.dao.hibernate package.
	 */
	@Pointcut("execution( public * org.openapplicant.dao.hibernate.*.*(..))")
	public void hibernateDataAccessOperation(){}
	
	/**
	 * A web operation is the execution of any public method defined on a type
	 * in the org.openapplicant.web package or any subpackage under that.
	 */
	@Pointcut("execution( public * org.openapplicant.web..*.*(..))")
	public void webOperation(){}
	
	/**
	 * A web controller operation is the execution of any public method defined 
	 * on a type in the org.openapplicant.web.controller package or any subpackage
	 * under that.
	 */
	@Pointcut("execution (public * org.openapplicant.web.controller..*.*(..))")
	public void webControllerOperation(){}
	
	/**
	 * A dwr operation is the execution of any public method defined 
	 * on a type in teh org.openapplicant.web.dwr package or any subpackage
	 * under that.
	 */
	@Pointcut("execution (public * org.openapplicant.web.dwr..*.*(..))")
	public void dwrOperation(){}
	
	/**
	 * A security operation is the execution of any public method defined
	 * on a type in the org.openapplicant.security package.
	 */
	@Pointcut("execution(public * org.openapplicant.security.*.*(..))")
	public void securityOperation(){}
	
	/**
	 * A test operation is any method defined in a class who's name ends with
	 * test.
	 */
	@Pointcut("execution(* org.openapplicant..*Test.*(..))")
	public void testOperation(){}
	
	/**
	 * A join point is in in a test case if that class's name ends with 
	 * "Test"
	 */
	@Pointcut("within(org.openapplicant..*Test)")
	public void inTestCase(){}
	
	/**
	 * A join point is a repository if it is annotated with @Respository
	 */
	@Pointcut("execution(public * @org.springframework.stereotype.Repository *.*(..))")
	public void repository(){}
	
	/**
	 * A join point is a controller if it is annotated with @Controller
	 */
	@Pointcut("execution(public * @org.springframework.stereotype.Controller *.*(..))")
	public void controller(){}
	
	/**
	 * A join point is a service if it is annotated with @Service
	 */
	@Pointcut("execution(public * @org.springframework.stereotype.Service *.*(..))")
	public void service() {}
	
	@Pointcut("execution(public void *.set*(*))")
	public void setterMethod() {}
}
