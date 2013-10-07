/**
 * 
 */
package com.cucreek.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation marker that can be used in the entities to indicate which
 * attributes are used as code table representations
 * 
 * @author jljdavidson
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CodeTableEntityAnnotation
{
   String description();
   String code();
   String effectiveDate();
   String endDate() default "";
   String convertCodeToLongOnLookup() default "no";
}
