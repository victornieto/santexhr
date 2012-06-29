package org.openapplicant.util;

import java.util.Date;

/**
 * Class of static utility methods for working with dates.
 */
public abstract class DateUtils {

	//========================================================================
	// METHODS
	//========================================================================
	public static Date january(int day, int year) {
		return CalendarUtils.january(day, year).getTime();
	}
	
	public static Date february(int day, int year) {
		return CalendarUtils.february(day, year).getTime();
	}
	
	public static Date march(int day, int year) {
		return CalendarUtils.march(day, year).getTime();
	}
	
	public static Date april(int day, int year) {
		return CalendarUtils.april(day, year).getTime();
	}
	
	public static Date may(int day, int year) {
		return CalendarUtils.may(day, year).getTime();
	}
	
	public static Date june(int day, int year) {
		return CalendarUtils.june(day, year).getTime();
	}
	
	public static Date july(int day, int year) {
		return CalendarUtils.july(day, year).getTime();
	}
	
	public static Date august(int day, int year) {
		return CalendarUtils.august(day, year).getTime();
	}
	
	public static Date september(int day, int year) {
		return CalendarUtils.september(day, year).getTime();
	}
	
	public static Date october(int day, int year) {
		return CalendarUtils.october(day, year).getTime();
	}
	
	public static Date november(int day, int year) {
		return CalendarUtils.november(day, year).getTime();
	}
	
	public static Date december(int day, int year) {
		return CalendarUtils.december(day, year).getTime();
	}
}
