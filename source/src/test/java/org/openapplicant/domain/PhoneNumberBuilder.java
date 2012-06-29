package org.openapplicant.domain;

import org.openapplicant.domain.PhoneNumber;

/**
 * Builds test PhoneNumber objects.
 */
public class PhoneNumberBuilder {
	
	private String number = "1112223333";
	
	public PhoneNumberBuilder withNumber(String value) {
		number = value;
		return this;
	}
	
	public PhoneNumber build() {
		PhoneNumber result = new PhoneNumber(number);
		return result;
	}

}
