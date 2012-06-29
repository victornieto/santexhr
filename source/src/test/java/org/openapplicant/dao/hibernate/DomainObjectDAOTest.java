package org.openapplicant.dao.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;
import org.openapplicant.dao.IDomainObjectDAO;
import org.openapplicant.domain.DomainObject;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


@ContextConfiguration(locations="/applicationContext-test.xml")
public abstract class DomainObjectDAOTest<T extends DomainObject>
	extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Test
	public void save() {
		T domainObject = newDomainObject();
		assertNull(domainObject.getId());
		assertTrue(domainObject.isNew());
		
		domainObject = getDomainObjectDao().save(domainObject);
		assertNotNull(domainObject.getId());
		assertFalse(domainObject.isNew());
	}
	
	@Test
	public void find() {
		T domainObject = newDomainObject();
		domainObject = getDomainObjectDao().save(domainObject);
		
		T found = getDomainObjectDao().find(domainObject.getId());
		
		assertEquals(found.getId(), domainObject.getId());
	}
	
	@Test(expected=DataRetrievalFailureException.class)
	public void find_failed() {
		getDomainObjectDao().find(Long.valueOf(-1));
	}
	
	@Test
	public void findByGuid() {
		T domainObject = newDomainObject();
		domainObject = getDomainObjectDao().save(domainObject);
		
		T found = getDomainObjectDao().findByGuid(domainObject.getGuid());
		assertEquals(found.getId(), domainObject.getId());
	}
	
	@Test
	public void findByGuidOrNull() {
		assertNull(getDomainObjectDao().findByGuidOrNull(UUID.randomUUID().toString()));
	}
	
	@Test(expected=DataRetrievalFailureException.class)
	public void findByGuid_failed() {
		getDomainObjectDao().findByGuid(UUID.randomUUID().toString());
	}
	
	@Test
	public void delete() {
		T domainObject = newDomainObject();
		domainObject = getDomainObjectDao().save(domainObject);
		Long id = domainObject.getId();
		
		getDomainObjectDao().delete(id);
		
		assertNull(getDomainObjectDao().findOrNull(id));
	}
	
	/**
	 * Test that validation does not throw any errors.  Some domain objects use
	 * Daos to test for uniqueness, so test that these methods are called correctly.
	 */
	@Test
	public void validate() {
		newDomainObject().validate();
	}
	
	/**
	 * Called by subclasses to instantiate a new domain object
	 * 
	 * @return the new domain object.
	 */
	protected abstract T newDomainObject();
	
	/**
	 * Called by subclasses to provide an IDomainObjectDAO implementation
	 * for CRUD tests.
	 * 
	 * @return the IDomainObjecDAO implementation.
	 */
	protected abstract IDomainObjectDAO<T> getDomainObjectDao();

}
