package org.openapplicant.dao.hibernate;

import org.hibernate.FlushMode;
import org.openapplicant.dao.ISessionFacade;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class SessionFacade extends HibernateDaoSupport 
	implements ISessionFacade {

	public void beManual() {
		getSession().setFlushMode(FlushMode.MANUAL);
	}

	public void flush() {
		getSession().flush();
	}
}
