package pl.filipowm.api;

/**
 * Provider used for handling API path mapping
 * when path is not provided in {@link pl.filipowm.api.annotations.Api}.
 *
 * Implement this interface to enable custom handling
 * for path mappings with unspecified path in {@link pl.filipowm.api.annotations.Api}.
 * By default path name is derived from name of class annotated with {@link io.swagger.annotations.Api}
 *
 * @see BasePathNamingProvider
 */
public interface EmptyPathNamingProvider {

    String getNameForHandlerType(Class handler);

}
