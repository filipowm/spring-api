package io.github.filipowm.api;

import io.github.filipowm.api.annotations.Api;

/**
 * Provider used for handling API path mapping
 * when path is not provided in {@link Api}.
 *
 * Implement this interface to enable custom handling
 * for path mappings with unspecified path in {@link Api}.
 * By default path name is derived from name of class annotated with {@link io.swagger.annotations.Api}
 *
 * @see BasePathNamingProvider
 */
public interface EmptyPathNamingProvider {

    String getNameForHandlerType(Class handler);

}
