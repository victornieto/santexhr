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
        return humanize(s, false);
	}

    public static String humanize(String s, boolean capitalizeEveryWord) {
        String[] values = trimToEmpty(s).split("_");
        for(int i=0; i<values.length; i++) {
            values[i] = values[i].toLowerCase();
        }
        if (capitalizeEveryWord) {
            for (int i = 0; i < values.length; i++) {
                values[i] = capitalize(values[i]);
            }
        } else {
            if(values.length > 0) {
                values[0] = capitalize(values[0]);
            }
        }
        return join(values, " ");
    }

    public static String dehumanize(String s) {
        return s.toUpperCase().replace(' ', '_');
    }
}
