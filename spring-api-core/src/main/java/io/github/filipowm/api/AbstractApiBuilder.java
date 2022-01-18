package io.github.filipowm.api;

import io.github.filipowm.api.annotations.Api;
import io.github.filipowm.api.annotations.ApiVersion;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

@Getter
abstract public class AbstractApiBuilder<T> implements ApiBuilder<T> {

    protected final Api api;

    protected final Method method;

    protected final Class<?> handlerType;

    protected final T info;

    protected final PathHolders pathHolders;

    protected final ContentTypeHolders contentTypeHolders;

    protected final String pathPrefix;

    protected final String versionPrefix;

    protected final String contentTypeVnd;

    protected final ApiVersion apiVersion;

    public AbstractApiBuilder(Api api, Method method, Class<?> handlerType, T info,
                       String pathPrefix, String versionPrefix, String contentTypeVnd) {
        this.api = api;
        this.method = method;
        this.handlerType = handlerType;
        this.info = info;
        this.pathPrefix = pathPrefix;
        this.versionPrefix = versionPrefix;
        this.contentTypeVnd = contentTypeVnd;
        this.pathHolders = new PathHolders(pathPrefix, versionPrefix);
        this.contentTypeHolders = new ContentTypeHolders();
        this.apiVersion = resolveApiVersion(method, handlerType, api);
        initializePathHolders();
        initializeContentTypeHolders();
    }

    private static ApiVersion resolveApiVersion(Method method, Class<?> handlerType, Api api) {
        var version = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        if (version == null) {
            version = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
            if (version == null) {
                version = api.version();
            }
        }
        return version;
    }

    protected abstract void initializePathHolders();

    protected abstract void initializeContentTypeHolders();
}
