package org.openapplicant.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.openapplicant.dao.hibernate.DomainObjectDAO;
import org.openapplicant.domain.DomainObject;

/**
 * Ensures synchronicity between domain objects and the database during
 * transactional test methods.
 */
@Aspect
public class HibernateFlushingAspect {

	private static final Log log = LogFactory.getLog(HibernateFlushingAspect.class);
	
	@Pointcut("call(* org.openapplicant.dao..save(..)) && org.openapplicant.SystemArchitecture.inTestCase()")
	private void saveOperationInTestCode() {}
	
	/**
	 * Synchronizes a domain object made persistent by invoking IDomainObjectDAO.save().
	 * This method ensures that all domain objects in the first-level cache
	 * reflect the true database state.
	 * @param jp
	 */
	@After("saveOperationInTestCode()")
	public void refreshSavedObject(JoinPoint jp) {
		if(!(jp.getTarget() instanceof DomainObjectDAO)) {
			return;
		}
		if(jp.getArgs().length != 1) {
			return;
		}
		if(!(jp.getArgs()[0] instanceof DomainObject)) {
			return;
		}
		
		logJoinPoint(jp);
		DomainObjectDAO dao = (DomainObjectDAO)jp.getTarget();
		
		logFlushing();
		dao.getHibernateTemplate().flush();
		
		logRefreshing();
		dao.getHibernateTemplate().refresh(jp.getArgs()[0]);
	}
	
	private void logJoinPoint(JoinPoint jp) {
		if(log.isInfoEnabled()) {
			String msg = new StringBuilder()
								.append("Advising ")
								.append(jp.getTarget().getClass().getName())
								.append(".")
								.append(jp.getSignature().toShortString())
								.toString();
			log.info(msg);
		}
	}
	
	private void logFlushing() {
		if(log.isInfoEnabled()) {
			log.info("FLUSHING");
		}
	}
	
	private void logRefreshing() {
		if(log.isInfoEnabled()) {
			log.info("REFRESHING");
		}
	}
}
