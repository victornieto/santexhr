package org.openapplicant.policy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a given property should not be modified if an object's state is frozen.  
 * Should be placed on setter, add, remove type methods. 
 * @see IFreezable
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertNotFrozen {

}
