package io.github.filipowm.api.servlet;

import io.github.filipowm.api.AbstractApiBuilder;
import io.github.filipowm.api.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.MediaTypeExpression;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Representation of API mapping to be built using servlet approach.
 * It uses base {@link RequestMappingInfo} built by {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping}
 * and then enhanced by API customizations.
 *
 * <p>Using this class, you can operate directly on original mapping ({@link #getInfo()}
 * or any other object used to customize API mapping.</p>
 */
public class ServletApiBuilder extends AbstractApiBuilder<RequestMappingInfo> {

    public ServletApiBuilder(Api api, Method method, Class<?> handlerType, RequestMappingInfo info, String pathPrefix, String versionPrefix, String contentTypeVnd) {
        super(api, method, handlerType, info, pathPrefix, versionPrefix, contentTypeVnd);
    }

    protected void initializePathHolders() {
        Optional.ofNullable(info.getPatternsCondition())
                .map(PatternsRequestCondition::getPatterns)
                .orElseGet(Set::of)
                .forEach(pathHolders::add);
    }

    protected void initializeContentTypeHolders() {
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
    public ServletApiBuilder combine(RequestMappingInfo other) {
        var combined = build().combine(other);
        return new ServletApiBuilder(api, method, handlerType, combined, pathPrefix, versionPrefix, contentTypeVnd);
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
