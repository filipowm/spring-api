package io.github.filipowm.api;

import io.github.filipowm.api.servlet.ServletApiVersioningContentTypeStrategy;
import io.github.filipowm.api.servlet.ServletApiVersioningPathStrategy;

import java.util.Set;

/**
 * Interface used for marking API versioning strategies.
 *
 * <p><b>IMPORTANT:</b> only ONE versioning strategy can be used</p>
 *
 * @see ServletApiVersioningContentTypeStrategy
 * @see ServletApiVersioningPathStrategy
 */
public interface ApiVersionStrategy<T> extends ApiDecorator {

    Set<Integer> parseVersion(T requestMappingInfo);

    @Override
    default boolean supports(ApiBuilder<?> builder) {
        return true;
    }
}
