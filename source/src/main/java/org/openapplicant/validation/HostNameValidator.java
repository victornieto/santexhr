package org.openapplicant.validation;

import org.hibernate.validator.Validator;

public class HostNameValidator implements Validator<HostName> {

	public void initialize(HostName arg0) {}

	public boolean isValid(Object obj) {
		if(!(obj instanceof String)) {
			return false;
		}
		String hostName = (String) obj;
		return !hostName.contains(":");
	}
}
