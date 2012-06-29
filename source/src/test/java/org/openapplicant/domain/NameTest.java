package org.openapplicant.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openapplicant.domain.Name;

public class NameTest {
	
	@Test
	public void commaTest() {
		Name name1 = new Name("Meadows, Rob");
		assertEquals("Rob",name1.getFirst());
		assertEquals("Meadows",name1.getLast());
	}
	
	@Test
	public void garbageTest() {
		Name name1 = new Name("Matt Williams¨¿Œ«¿¬");
		assertEquals("Matt",name1.getFirst());
		assertEquals("Williams",name1.getLast());
	}
	
	@Test
	public void garbageComma() {
		Name name1 = new Name("Meadows, RobŒ¿«¬Œ¿«¬Œ¬ö");
		assertEquals("Rob",name1.getFirst());
		assertEquals("Meadows",name1.getLast());
	}
	
	@Test
	public void equals_basic() {
		Name name1 = new NameBuilder()
							.withFirst("john")
							.withMiddle("q")
							.withLast("matrix")
							.build();
			
		Name name2 = new NameBuilder()
							.withFirst("john")
							.withMiddle("q")
							.withLast("matrix")
							.build();

		assertEquals(name1, name2);
		assertEquals(name1.hashCode(), name2.hashCode());
	}
	
	@Test
	public void properties_null() {
		Name name = new NameBuilder().build();
		name.setFirst(null);
		name.setLast(null);
		name.setMiddle(null);
		
		assertEquals("", name.getFirst());
		assertEquals("", name.getMiddle());
		assertEquals("", name.getLast());
		assertEquals("", name.getFullName());
	}

}
