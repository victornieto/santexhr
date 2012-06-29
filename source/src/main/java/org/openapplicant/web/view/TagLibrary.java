package org.openapplicant.web.view;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.openapplicant.util.Strings;


public class TagLibrary {
    
	// express containment of a string in a set
	public static String formatLong(long milliseconds) {
		double seconds = milliseconds/1000;
	    if(seconds < 60) {
	    	return Math.floor(seconds*10)/10 + " secs";
	    }
	    else {
	        int minutes = (int) Math.floor(seconds/60);
	        int sec = (int) Math.floor(seconds%60);			
	        return minutes + ":" + (sec < 10 ? "0" + sec : sec) + " mins";
	    }
	}
	
	public static String abbreviate(String text) {
	    return StringUtils.abbreviate(text, 20);
	}
	
	public static String abbreviateTo(String text, int length) {
	    return StringUtils.abbreviate(text, length);
	}
	
	public static boolean notBlank(String str) {
	    return StringUtils.isNotBlank(str);
	}
	
	public static String humanize(String s) {
		return Strings.humanize(s);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean contains(Collection collection, Object o) {
		return collection.contains(o);
	}
}