package org.openapplicant.util;

import java.util.ResourceBundle;

/**
 * Provides static access to the application messages
 */
public abstract class Messages {

	private static final ResourceBundle messages = ResourceBundle.getBundle("messages");
	
	/**
	 * @return the default welcome text to display in the quiz portal
	 */
	public static String getDefaultCompanyWelcomeText() {
		return messages.getString("company.welcomeText");
	}
	
	/**
	 * @return the default completion text to display in the quiz portal
	 */
	public static String getDefaultCompanyCompletionText() {
		return messages.getString("company.completionText");
	}

    public static String getJobPositionDeleteDataIntegrityViolationExceptionText() {
        return messages.getString("jobPosition.deleteDataIntegrityViolationExceptionText");
    }
}
