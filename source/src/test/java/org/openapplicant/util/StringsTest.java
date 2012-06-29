package org.openapplicant.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openapplicant.util.Strings;

public class StringsTest {
	
	@Test
	public void humanize() {
		assertEquals("Resume rejected", Strings.humanize("RESUME_REJECTED"));
		assertEquals("null", "", Strings.humanize(null));
		assertEquals("empty", "", Strings.humanize(""));
		assertEquals("blank", "", Strings.humanize(" "));
	}

}
