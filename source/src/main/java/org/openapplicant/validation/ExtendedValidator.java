package org.openapplicant.validation;

import java.lang.annotation.Annotation;

import org.hibernate.validator.Validator;

public interface ExtendedValidator<A extends Annotation> extends Validator<A>{

	boolean isValid(Object bean, String propertyName, Object propertyValue);
}
