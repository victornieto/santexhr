package org.openapplicant.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openapplicant.util.Pagination;


public class PaginationTest {
	
	@Test
	public void forPage() {
		Pagination oneBased = Pagination.oneBased().perPage(10);
		assertEquals(10, oneBased.getLimit());
		assertEquals(0, oneBased.getOffset());
		
		Pagination zeroBased = Pagination.zeroBased().perPage(10);
		assertEquals(10, zeroBased.getLimit());
		assertEquals(0, zeroBased.getOffset());
		
		oneBased = oneBased.forPage(1);
		zeroBased = zeroBased.forPage(0);
		
		assertEquals(oneBased.getLimit(), zeroBased.getLimit());
		assertEquals(oneBased.getOffset(), zeroBased.getOffset());
		assertEquals(10, oneBased.getLimit());
		assertEquals(0, oneBased.getOffset());
		
		oneBased = oneBased.forPage(2);
		zeroBased = zeroBased.forPage(1);
		
		assertEquals(oneBased.getLimit(), zeroBased.getLimit());
		assertEquals(oneBased.getOffset(), zeroBased.getOffset());
		assertEquals(10, oneBased.getLimit());
		assertEquals(10, oneBased.getOffset());
		
		oneBased = oneBased.forPage(5);
		zeroBased= zeroBased.forPage(4);
		
		assertEquals(oneBased.getLimit(), zeroBased.getLimit());
		assertEquals(oneBased.getOffset(), zeroBased.getOffset());
		assertEquals(10, oneBased.getLimit());
		assertEquals(40, oneBased.getOffset());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void forPage_oneBasedException() {
		Pagination.oneBased().forPage(0);
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void forPage_zeroBasedException() {
		Pagination.zeroBased().forPage(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void resultsOf_oneBasedException() {
		Pagination.oneBased().perPage(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void resultsOf_zeroBasedException() {
		Pagination.zeroBased().perPage(-1);
	}

}
