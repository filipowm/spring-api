package io.github.filipowm.api.servlet;

import io.github.filipowm.api.ApiDecorator;
import io.github.filipowm.api.ApiToRequestMappingMapper;
import io.github.filipowm.api.EmptyPathNamingProvider;
import io.github.filipowm.api.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Creates {@link RequestMappingInfo} instances from type and method-level
 * {@link RequestMapping @RequestMapping} annotations in
 * {@link Api @Api} classes.
 * <p>
 * It uses {@link ApiDecorator} objects to customize endpoint mappings.
 */
@Slf4j
public class ServletApiRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final List<ApiDecorator> apiDecorators;

    private final EmptyPathNamingProvider pathNamingProvider;

    private final String pathPrefix;

    private final String versionPrefix;

    private final String contentTypeVnd;

    public ServletApiRequestMappingHandlerMapping(List<ApiDecorator> apiDecorators,
                                                  EmptyPathNamingProvider pathNamingProvider,
                                                  String pathPrefix, String versionPrefix, String contentTypeVnd) {
        this.apiDecorators = apiDecorators;
        this.pathPrefix = pathPrefix;
        this.versionPrefix = versionPrefix;
        this.contentTypeVnd = contentTypeVnd;
        this.pathNamingProvider = pathNamingProvider;
        setOrder(-1);
    }

    /**
     * If bean is annotated with {@link Api} then it will be handled by this class
     */
    @Override
    protected boolean isHandler(Class<?> beanType) {
        var type = ClassUtils.getUserClass(beanType);
        return type.isAnnotationPresent(Api.class);
    }

    private RequestMappingInfo getMappingForMethodInternal(Method method, Class<?> handlerType) {
        var info = createRequestMappingInfo(method);
        if (info != null) {
            var typeInfo = createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        var api = AnnotatedElementUtils.findMergedAnnotation(element, Api.class);
        RequestMapping requestMapping;
        if (api == null) {
            requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        } else {
            requestMapping = ApiToRequestMappingMapper.map(api, pathNamingProvider, (Class) element);
        }

        var condition = (element instanceof Class ?
                             getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        return requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        var info = getMappingForMethodInternal(method, handlerType);
        if (info == null) {
            return null;
        }
        var api = AnnotationUtils.findAnnotation(handlerType, Api.class);
        if (api != null) {
            var current = info.toString();
            var builder = new ServletApiBuilder(api, method, handlerType, info, pathPrefix, versionPrefix, contentTypeVnd);
            info = getMappingForApi(builder);
            log.info("Enhanced request mapping from {} to {}", current, info);
        }
        return info;
    }

    private RequestMappingInfo getMappingForApi(final ServletApiBuilder builder) {
        apiDecorators.stream()
                     .filter(decorator -> decorator.supports(builder))
                     .forEach(decorator -> decorator.decorate(builder));
        return builder.build();
    }
}
