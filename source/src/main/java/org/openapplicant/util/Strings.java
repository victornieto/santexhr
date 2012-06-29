package org.openapplicant.util;

import org.apache.commons.lang.StringUtils;

/**
 * Class of static string utility methods.
 */
public abstract class Strings extends StringUtils {
	
	/**
	 * <p>
	 * Converts the given underscore separated string to a human friendly 
	 * format.
	 * </p>
	 * 
	 * <pre>
	 * Strings.humanize(null)					= ""
	 * String.humanize("   ")					= ""
	 * Strings.humanize("RESUME_REJECTED")		= "Resume rejected"
	 * </pre>
	 * @param s the string to humanize
	 * @return the humanized string.
	 */
	public static String humanize(String s) {
		String[] values = trimToEmpty(s).split("_");
		for(int i=0; i<values.length; i++) {
			values[i] = values[i].toLowerCase();
		}
		if(values.length > 0) {
			values[0] = capitalize(values[0]);
		}
		return join(values, " ");
	}

}
