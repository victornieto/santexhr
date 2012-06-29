package org.openapplicant.util;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.openapplicant.util.Messages;

public class MessagesTest {
	
	@Test
	public void companyWelcomeText() {
		assertTrue(StringUtils.isNotBlank(Messages.getDefaultCompanyWelcomeText()));
	}
	
	@Test
	public void companyCompletionText() {
		assertTrue(StringUtils.isNotBlank(Messages.getDefaultCompanyCompletionText()));
	}
}
