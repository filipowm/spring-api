package io.github.filipowm.api;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Set;

/**
 * Interface used for marking API versioning strategies.
 *
 * <p><b>IMPORTANT:</b> only ONE versioning strategy can be used</p>
 *
 * @see ApiVersioningContentTypeStrategy
 * @see ApiVersioningPathStrategy
 */
public interface ApiVersionStrategy extends ApiDecorator {

    Set<Integer> parseVersion(RequestMappingInfo requestMappingInfo);

    @Override
    default boolean supports(ApiBuilder builder) {
        return true;
    }
}
