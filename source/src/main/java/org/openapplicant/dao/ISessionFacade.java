package org.openapplicant.dao;

/**
 * Facade interface for the hibernate Session
 */
public interface ISessionFacade {
	
	/**
	 * Sets the flush mode of the current session to manual.
	 */
	void beManual();
	
	/**
	 * Synchronizes the session with the database.
	 */
	void flush();

}
