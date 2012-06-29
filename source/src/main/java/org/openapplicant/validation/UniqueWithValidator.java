package org.openapplicant.validation;

import java.util.Arrays;

import org.openapplicant.dao.IRowCounter;
import org.openapplicant.domain.DomainObject;
import org.springframework.beans.factory.annotation.Configurable;


@Configurable
public class UniqueWithValidator implements ExtendedValidator<UniqueWith> {

	private IRowCounter rowCounter;
	
	private String otherPropertyName;
	
	public void setRowCounter(IRowCounter value) {
		rowCounter = value;
	}
	
	public void initialize(UniqueWith annotation) {
		otherPropertyName = annotation.value();
	}
	
	public boolean isValid(Object arg0) {
		return true; // not enough info
	}
	
	public boolean isValid(Object bean, String propertyName, Object propertyValue) {
		return rowCounter.isUnique((DomainObject)bean, Arrays.asList(propertyName, otherPropertyName));
	}
}
