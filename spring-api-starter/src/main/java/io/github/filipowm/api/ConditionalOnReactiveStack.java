package io.github.filipowm.api;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@ConditionalOnMissingClass("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping")
@ConditionalOnClass(name = "org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping")
@interface ConditionalOnReactiveStack {
}
