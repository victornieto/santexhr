package org.openapplicant.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.openapplicant.util.TestUtils.assertMarch;

import java.util.Calendar;

import org.junit.Test;
import org.openapplicant.domain.CalendarRange;
import org.openapplicant.util.CalendarUtils;


public class CalendarUtilsTest {
	
	//========================================================================
	// TESTS
	//========================================================================
	
	//------------------------------------------------------------------------
	// PARSE
	//------------------------------------------------------------------------
	@Test
	public void parse() throws Exception {
		Calendar cal = CalendarUtils.parse("3/6/2007");
		assertMarch(6, 2007, cal);
		
		cal = CalendarUtils.parse("3-6-2007");
		assertMarch(6, 2007, cal);
	}
	
	//------------------------------------------------------------------------
	// PARSE DATE RANGE
	//------------------------------------------------------------------------
	@Test
	public void parse_dateRange() throws Exception {
		
		CalendarRange range = CalendarUtils.parseRange("foo bar 3/6/2007 to 3/6/2008 blah");
		assertMarch(6, 2007, range.getStartDate());
		assertMarch(6, 2008, range.getEndDate());
		
		range = CalendarUtils.parseRange("3-6-2007 - 3-8-2007");
		assertMarch(6, 2007, range.getStartDate());
		assertMarch(8, 2007, range.getEndDate());
		
		range = CalendarUtils.parseRange("3/6/2008");
		assertMarch(6, 2008, range.getStartDate());
		assertNull(range.getEndDate());
		
		range = CalendarUtils.parseRange("");
		assertNull(range.getStartDate());
		assertNull(range.getEndDate());
		
		range = CalendarUtils.parseRange(null);
		assertNull(range.getStartDate());
		assertNull(range.getEndDate());
	}
	
	@Test
	public void toString_basic() {
		assertNotNull(CalendarUtils.toString(CalendarUtils.december(29, 1981)));
		assertNotNull(CalendarUtils.toString(null));
	}
}
