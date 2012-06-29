package org.openapplicant.util;

import org.junit.Test;
import org.openapplicant.util.Verify;

public class VerifyTest {
	
	@Test
	public void contians() {
		Verify.contains(new String[]{"foo", "bar"}, "foo");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void contains_false() {
		Verify.contains(new String[]{"foo","bar"},"baz");
	}

}
