package org.openapplicant.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openapplicant.domain.CalendarRange;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * Class of static utility methods for working with calendars.
 */
public abstract class CalendarUtils {
	
	//========================================================================
	// CONSTANTS
	//========================================================================
	private static final String[] SIMPLE_DATE_FORMATS = new String[] {
		"MM/dd/yyyy", 
		"MM-dd-yyyy"
	};
	
	//========================================================================
	// METHODS
	//========================================================================
	/**
	 * @param day a day in January
	 * @param year the calendar year
	 * @return a calendar in the month of January
	 */
	public static Calendar january(int day, int year) {
		return _create(year, Calendar.JANUARY, day);
	}
	
	/**
	 * @param day a day in February
	 * @param year the calendar year
	 * @return a calendar in the month of February
	 */
	public static Calendar february(int day, int year) {
		return _create(year, Calendar.FEBRUARY, day);
	}
	
	/**
	 * @param day a day in March
	 * @param year the calendar year
	 * @return a calendar in the month of March
	 */
	public static Calendar march(int day, int year) {
		return _create(year, Calendar.MARCH, day);
	}
	
	/**
	 * @param day a day in April
	 * @param year the calendar year
	 * @return a calendar in the month of April
	 */
	public static Calendar april(int day, int year) {
		return _create(year, Calendar.APRIL, day);
	}
	
	/**
	 * @param day a day in May
	 * @param year the calendar year
	 * @return a calendar in the month of May
	 */
	public static Calendar may(int day, int year) {
		return _create(year, Calendar.MAY, day);
	}
	
	/**
	 * @param day a day in June
	 * @param year the calendar year
	 * @return a calendar in the month of June
	 */
	public static Calendar june(int day, int year) {
		return _create(year, Calendar.JUNE, day);
	}
	
	/**
	 * @param day a day in July
	 * @param year the calendar year
	 * @return a calendar in the month of July
	 */
	public static Calendar july(int day, int year) {
		return _create(year, Calendar.JULY, day);
	}
	
	/**
	 * @param day a day in august
	 * @param year the calendar year
	 * @return a calendar in the month of August
	 */
	public static Calendar august(int day, int year) {
		return _create(year, Calendar.AUGUST, day);
	}
	
	/**
	 * @param day a day in September
	 * @param year the calendar year
	 * @return a calendar in the month of September
	 */
	public static Calendar september(int day, int year) {
		return _create(year, Calendar.SEPTEMBER, day);
	}
	
	/**
	 * @param day a day in October
	 * @param year the calendar year
	 * @return a calendar in the month of October
	 */ 
	public static Calendar october(int day, int year) {
		return _create(year, Calendar.OCTOBER, day);
	}
	
	/**
	 * @param day a day in November
	 * @param year the calendar year
	 * @return a calendar in the month of November
	 */ 
	public static Calendar november(int day, int year) {
		return _create(year, Calendar.NOVEMBER, day);
	}
	
	/**
	 * @param day a day in December
	 * @param year the calendar year
	 * @return a calendar in the month of December
	 */ 
	public static Calendar december(int day, int year) {
		return _create(year, Calendar.DECEMBER, day);
	}
	
	/**
	 * @return the current time as a date string (eg. 3/6/2007).
	 */
	public static String today() {
		return toString(Calendar.getInstance());
	}
	
	/**
	 * @return the current time
	 */
	public static Calendar now() {
		return Calendar.getInstance();
	}
	
	/**
	 * Parse a CalendarRange from the given input string.
	 * 
	 * @param str the string to parse.  Should be space delimited.
	 * (eg. "3/6/2007 to 3/8/2008", "3/6/2007 3/9/2007")
	 * 
	 * @return the parsed calendar range
	 */
	public static CalendarRange parseRange(String str) {
		if(null == str) {
			return CalendarRange.EMPTY;
		}
		
		Calendar startDate = null;
		Calendar endDate = null;
		for(String each : StringUtils.split(str)) {
			try {
				Calendar date = parse(each);
				if(null == startDate) {
					startDate = date;
				} else {
					endDate = date;
					break;
				}
			} catch(ParseException e) {
				// ignore
			}
		}
		return new CalendarRange(startDate, endDate);
	}
	
	/**
	 * Parse a calendar form the given string.
	 * 
	 * @param str the calendar string to parse (eg: 3/6/2007)
	 * @return the parsed calendar
	 * @throws ParseException if no format parses.
	 */
	public static Calendar parse(String str) throws ParseException {
		return parse(str, SIMPLE_DATE_FORMATS);
	}
	
	/**
	 * Parse a calendar from the given string using the fist successful
	 * simple date format.
	 * 
	 * <pre>
	 * Calendar c = CalendarUtils.parse("3/6/2007", "MM/dd/yyyy", "MM-dd-yyyy");
	 * </pre>
	 * 
	 * @param str the calendar string to parse
	 * @param simpleDateFormats an array of simple date formats to try
	 * @return the parsed calendar
	 * @throws ParseException if no format parses.
	 */
	private static Calendar parse(String str, String... simpleDateFormats)
		throws ParseException {
		Date date = DateUtils.parseDate(str, simpleDateFormats);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}
	
	/**
	 * Null safe conversion of a calendar to a string using a default 
	 * date formatting pattern.
	 * 
	 * @param cal the calendar to convert
	 * @return the calendar as a string or "null" if cal was null.
	 */
	public static String toString(Calendar cal) {
		if(null == cal) {
			return "null";
		}
		return DateFormatUtils.format(cal.getTime(), SIMPLE_DATE_FORMATS[0]);
	}
	
	/**
	 * Worker method for constructing a new calendar.
	 */
	private static Calendar _create(int year, int month, int day) {
		Calendar result = Calendar.getInstance();
		result.set(year, month, day);
		return result;
	}
}
