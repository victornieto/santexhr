package org.openapplicant.util;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.openapplicant.domain.DomainObject;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Class of static test utility methods
 */
public abstract class TestUtils {
	
	/**
	 * @param domainObjects a list of domain objects who's ids to collect.
	 * @return a list of domain object ids
	 */
	public static List<Long> collectIds(List<? extends DomainObject> domainObjects) {
		if(null == domainObjects) {
			return new ArrayList<Long>();
		}
		List<Long> result = new ArrayList<Long>();
		for(DomainObject each : domainObjects) {
			if(null == each) {
				continue;
			}
			result.add(each.getId());
		}
		return result;
	}
	
	/**
	 * Creates a globally unique email.
	 * @return the unique email address.
	 */
	public static String uniqueEmail() {
		return UUID.randomUUID().toString() + "@gmail.com";
	}
	
	/**
	 * Sets the created date for the given entity info.
	 * @param entity the entity who's date to set
	 * @param date the created date
	 */
	public static void setCreatedDate(DomainObject entity, Calendar date) {
		ReflectionTestUtils.invokeSetterMethod(entity.getEntityInfo(), "createdDate", date);
	}
	
	/**
	 * <p>
	 * Assert that the given collection contains an instance of the given
	 * class
	 * </p>
	 * @param items the collection to test
	 * @param clazz the class to check
	 */
	@SuppressWarnings("unchecked")
	public static void assertContainsInstance(Collection items, Class clazz) {
		for(Object each : items) {
			if(clazz.isInstance(each)) {
				return;
			}
		}
		Assert.fail("[Assertion Failed] no item was an instance of the given class");
	}
	
	/**
	 * Assert that the given calendar is a day in march in the given year.
	 * 
	 * @param int day the day in march to test
	 * @param int year the year to test
	 * @param cal the calendar to test
	 */
	public static void assertMarch(int day, int year, Calendar cal) {
		assertEquals(cal.get(MONTH), MARCH);
		assertEquals(cal.get(DAY_OF_MONTH), day);
		assertEquals(cal.get(YEAR), year);
	}
	
	/**
	 * Assert that the given method which takes no parameters has the given annotation.
	 * 
	 * @param clazz the class who's method to check
	 * @param method the name of the method to check
	 * @param annotation the annotation who's existence to assert
	 */
	public static void assertHasAnnotation(Class clazz, String method, Class annotation) {
		try {
			Method m = clazz.getMethod(method, (Class[])null);
			assertNotNull("[Assertion Failed] Method is not annotated by: " + annotation, m.getAnnotation(annotation));
		} catch(Exception e) {
			throw new AssertionError(e);
		}
	}
	
	/**
	 * Returns an annotation on the given class's method
	 * @param clazz
	 * @param method the name of a method in clazz that takes no parameters
	 * @param annotationClass
	 * @return the specified annotation
	 */
	public static <T extends Annotation> T getAnnotation(Class clazz, String method, Class<T> annotationClass) {
		assertHasAnnotation(clazz, method, annotationClass);
		try {
			Method m = clazz.getMethod(method, (Class[])null);
			return m.getAnnotation(annotationClass);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
