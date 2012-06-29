package org.openapplicant.util;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Class of static assertion utility methods
 */
public abstract class Verify {
	
	/**
	 * Assert that the given object is contained in the given array
	 * @param array the array that should contain valueToFind
	 * @param valueToFind the object to find
	 */
	public static void contains(Object[] array, Object valueToFind) {
		contains(array, valueToFind, "[Assertion Failed] This array does not contain a required object");
	}
	
	public static void contains(Object[] array, Object valueToFind, String message) {
		Assert.isTrue(ArrayUtils.contains(array, valueToFind), message);
	}

}
