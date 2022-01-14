package pl.filipowm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.MediaTypeExpression;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import pl.filipowm.api.annotations.Api;
import pl.filipowm.api.annotations.ApiVersion;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Representation of API mapping to be built.
 * It uses base {@link RequestMappingInfo} built by {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping}
 * and then enhanced by API customizations.
 *
 * <p>Using this class, you can operate directly on original mapping ({@link #getInfo()}
 * or any other object used to customize API mapping.</p>
 */
public class ApiBuilder {

    private final Api api;

    private final Method method;

    private final Class<?> handlerType;

    private final RequestMappingInfo info;

    private final PathHolders pathHolders;

    private final ContentTypeHolders contentTypeHolders;

    private final String pathPrefix;

    private final String versionPrefix;

    private final String contentTypeVnd;

    private ApiVersion apiVersion;

    ApiBuilder(Api api, Method method, Class<?> handlerType, RequestMappingInfo info,
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
        initializeVersion();
        initializePathHolders();
        initializeContentTypeHolders();
    }

    private void initializeVersion() {
        var version = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        if (version == null) {
            version = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
            if (version == null) {
                version = api.version();
            }
        }
        this.apiVersion = version;
    }

    private void initializePathHolders() {
        Optional.ofNullable(info.getPatternsCondition())
                .map(PatternsRequestCondition::getPatterns)
                .orElseGet(Set::of)
                .forEach(pathHolders::add);
    }

    private void initializeContentTypeHolders() {
        initializeContentTypeHolderForExpressions(info.getConsumesCondition().getExpressions(), consumes -> contentTypeHolders.addConsumes(consumes, contentTypeVnd));
        initializeContentTypeHolderForExpressions(info.getProducesCondition().getExpressions(), produces -> contentTypeHolders.addProduces(produces, contentTypeVnd));
    }

    private void initializeContentTypeHolderForExpressions(Collection<MediaTypeExpression> expressions, Consumer<String> consumer) {
        expressions.stream()
                   .map(MediaTypeExpression::toString)
                   .forEach(consumer);
    }

    /**
     * Method building customized API mapping based on original mapping
     * and applied customizations
     *
     * @return API mapping object
     */
    public RequestMappingInfo build() {
        var patterns = pathHolders.toCondition();
        var pair = contentTypeHolders.toCondition();
        var builder = new RequestMappingInfoBuilder(info);
        builder.addCondition(patterns);
        builder.addCondition(pair.getOne());
        builder.addCondition(pair.getTwo());
        return builder.build();
    }

    /**
     * @return API annotation applied on class
     * @see #getHandlerType()
     */
    public Api getApi() {
        return this.api;
    }

    /**
     * @return method handling given API call defined within {@link #handlerType}.
     * This method will be called whenever request matches {@link RequestMappingInfo}
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * @return controller class annotated with {@link Api}
     */
    public Class<?> getHandlerType() {
        return this.handlerType;
    }

    /**
     * @return object holding path definition
     */
    public PathHolders getPathHolders() {
        return this.pathHolders;
    }

    /**
     * @return object holding content-type definition
     */
    public ContentTypeHolders getContentTypeHolders() {
        return this.contentTypeHolders;
    }

    /**
     * @return API version object applicable to this API mapping. If method-level {@link ApiVersion}
     * is defined, then it overrides type-level value.
     */
    public ApiVersion getApiVersion() {
        return this.apiVersion;
    }

    /**
     * @return original mapping built by {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping},
     * on which then are applied customizations
     */
    public RequestMappingInfo getInfo() {
        return this.info;
    }

    /**
     * @return original consumes condition. Shorthand for {@code getInfo().getConsumesCondition();}
     */
    public ConsumesRequestCondition getConsumesCondition() {
        return this.info.getConsumesCondition();
    }

    /**
     * @return original headers condition. Shorthand for {@code getInfo().getHeadersCondition();}
     */
    public HeadersRequestCondition getHeadersCondition() {
        return this.info.getHeadersCondition();
    }

    /**
     * @return original produces condition. Shorthand for {@code getInfo().getProducesCondition();}
     */
    public ProducesRequestCondition getProducesCondition() {
        return this.info.getProducesCondition();
    }

    /**
     * @return original patterns condition. Shorthand for {@code getInfo().getPatternsCondition();}
     */
    public PatternsRequestCondition getPatternsCondition() {
        return this.info.getPatternsCondition();
    }

    /**
     * @return original request methods condition. Shorthand for {@code getInfo().getMethodsCondition();}
     */
    public RequestMethodsRequestCondition getMethodsCondition() {
        return this.info.getMethodsCondition();
    }

    /**
     * @return original params condition. Shorthand for {@code getInfo().getParamsCondition();}
     */
    public ParamsRequestCondition getParamsCondition() {
        return this.info.getParamsCondition();
    }

    /**
     * @return original custom condition. Shorthand for {@code getInfo().getCustomCondition();}
     */
    public RequestCondition<?> getCustomCondition() {
        return this.info.getCustomCondition();
    }

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
    public ApiBuilder combine(RequestMappingInfo other) {
        RequestMappingInfo combined = build().combine(other);
        return new ApiBuilder(api, method, handlerType, combined, pathPrefix, versionPrefix, contentTypeVnd);
    }

    /**
     * @return original mapping name. Shorthand for {@code getInfo().getName();}
     */
    public String getName() {
        return this.info.getName();
    }

    @RequiredArgsConstructor
    private static class RequestMappingInfoBuilder {
        private final RequestMappingInfo parentMappingInfo;

        private PatternsRequestCondition patterns;

        private RequestMethodsRequestCondition methods;

        private ParamsRequestCondition params;

        private HeadersRequestCondition headers;

        private ConsumesRequestCondition consumes;

        private ProducesRequestCondition produces;

        private RequestCondition custom;

        RequestMappingInfoBuilder addCondition(RequestCondition condition) {
            if (condition instanceof PatternsRequestCondition) {
                this.patterns = (PatternsRequestCondition) condition;
            } else if (condition instanceof RequestMethodsRequestCondition) {
                this.methods = (RequestMethodsRequestCondition) condition;
            } else if (condition instanceof ParamsRequestCondition) {
                this.params = (ParamsRequestCondition) condition;
            } else if (condition instanceof HeadersRequestCondition) {
                this.headers = (HeadersRequestCondition) condition;
            } else if (condition instanceof ConsumesRequestCondition) {
                this.consumes = (ConsumesRequestCondition) condition;
            } else if (condition instanceof ProducesRequestCondition) {
                this.produces = (ProducesRequestCondition) condition;
            } else {
                this.custom = condition;
            }
            return this;
        }

        RequestMappingInfo build() {
            if (patterns == null) {
                patterns = parentMappingInfo.getPatternsCondition();
            }
            if (methods == null) {
                methods = parentMappingInfo.getMethodsCondition();
            }
            if (params == null) {
                params = parentMappingInfo.getParamsCondition();
            }
            if (headers == null) {
                headers = parentMappingInfo.getHeadersCondition();
            }
            if (consumes == null) {
                consumes = parentMappingInfo.getConsumesCondition();
            }
            if (produces == null) {
                produces = parentMappingInfo.getProducesCondition();
            }
            if (parentMappingInfo.getCustomCondition() != null) {
                custom = parentMappingInfo.getCustomCondition();
            }
            return new RequestMappingInfo(patterns, methods, params, headers, consumes, produces, custom);
        }
    }
}
