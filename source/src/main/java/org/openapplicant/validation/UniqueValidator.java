package org.openapplicant.validation;

import java.util.Arrays;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.openapplicant.dao.IRowCounter;
import org.openapplicant.domain.DomainObject;
import org.springframework.beans.factory.annotation.Configurable;


@Configurable
public class UniqueValidator implements ExtendedValidator<Unique>, PropertyConstraint{

	private IRowCounter rowCounter;
	
	public void setRowCounter(IRowCounter value) {
		rowCounter = value;
	}
	
	public void initialize(Unique arg0) {}
	
	public boolean isValid(Object bean, String propertyName, Object propertyValue) {
		return rowCounter.isUnique((DomainObject)bean, Arrays.asList(propertyName));
	}

	public boolean isValid(Object arg0) {
		return true; // not enough information
	}

	@SuppressWarnings("unchecked")
	public void apply(Property property) {
		for(Iterator<Column> it = property.getColumnIterator(); it.hasNext();) {
			Column column = it.next();
			column.setUnique(true);
		}
	}
}
