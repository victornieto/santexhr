package org.openapplicant.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

/**
 * Marks the given property as being unique with another property
 * of the same object. 
 */
@ValidatorClass(UniqueWithValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UniqueWith {
	
	/**
	 * An additional property of the target entity to validate.  The combination
	 * of the annotated property and this property will be checked for uniqueness.
	 */
	String value();
	
	String message() default "field is not unique";
}
