package io.github.filipowm.api.annotations.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark your controller type or method with to return {@link HttpStatus#METHOD_NOT_ALLOWED}.
 * It is shorthand for {@code @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)}
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public @interface MethodNotAllowed {
}
