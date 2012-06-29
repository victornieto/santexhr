package org.openapplicant.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

@ValidatorClass(UniqueValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Unique {
	
	String message() default "field is not unique";

}
