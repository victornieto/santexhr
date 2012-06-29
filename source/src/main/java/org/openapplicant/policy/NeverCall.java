package org.openapplicant.policy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method should never be called in standard programming.
 * This marker annotation can be used, for instance, to flag setter methods 
 * or default constructors that exist for java bean tools but otherwise 
 * should never be called.
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeverCall {

}
