package io.github.filipowm.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The version of API, applied to path mapping or content-type based on chosen versioning strategy.
 * Method-level version definition override type-level definition.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

    int UNVERSIONED = -1;

    /**F
     * Version value
     */
    int value() default 1;

}
