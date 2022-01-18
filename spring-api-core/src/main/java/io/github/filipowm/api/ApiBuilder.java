package io.github.filipowm.api;

import io.github.filipowm.api.annotations.Api;
import io.github.filipowm.api.annotations.ApiVersion;

import java.lang.reflect.Method;

public interface ApiBuilder<T> {
    /**
     * @return API annotation applied on class
     * @see #getHandlerType()
     */
    Api getApi();

    /**
     * @return API version object applicable to this API mapping. If method-level {@link ApiVersion}
     * is defined, then it overrides type-level value.
     */
    ApiVersion getApiVersion();
    /**
     * @return original mapping name. Shorthand for {@code getInfo().getName();}
     */
    String getName();
    /**
     * @return method handling given API call defined within {@link #handlerType}.
     */
    Method getMethod();
    /**
     * @return controller class annotated with {@link Api}
     */
    Class<?> getHandlerType();
    /**
     * @return object holding path definition
     */
    PathHolders getPathHolders();
    /**
     * @return object holding content-type definition
     */
    ContentTypeHolders getContentTypeHolders();
    /**
     * Builds request mapping from this definition and combines with other request mapping.
     *
     * <p>This method first builds new API mapping and then it applies custom mapping.
     * It does not operate on original mapping!</p>
     *
     * <p>This operation can be chained!</p>
     *
     * <p><b>IMPORTANT:</b> Use it with care, as it may completely change how given mapping is built,
     * however it offers most powerful capabilities, because you can define custom request mapping
     * and apply it to original.</p>
     *
     * @return new API builder with applied custom request mapping.
     */
    ApiBuilder combine(T info);
    /**
     * Method building customized API mapping based on original mapping
     * and applied customizations
     *
     * @return API mapping object
     */
    T build();
}
