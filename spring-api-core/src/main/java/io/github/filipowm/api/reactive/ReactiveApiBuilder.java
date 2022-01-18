package io.github.filipowm.api.reactive;

import io.github.filipowm.api.AbstractApiBuilder;
import io.github.filipowm.api.ApiBuilder;
import io.github.filipowm.api.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.result.condition.ConsumesRequestCondition;
import org.springframework.web.reactive.result.condition.HeadersRequestCondition;
import org.springframework.web.reactive.result.condition.MediaTypeExpression;
import org.springframework.web.reactive.result.condition.ParamsRequestCondition;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.condition.ProducesRequestCondition;
import org.springframework.web.reactive.result.condition.RequestCondition;
import org.springframework.web.reactive.result.condition.RequestMethodsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class ReactiveApiBuilder extends AbstractApiBuilder<RequestMappingInfo> {
    public ReactiveApiBuilder(Api api, Method method, Class<?> handlerType, RequestMappingInfo info, String pathPrefix, String versionPrefix, String contentTypeVnd) {
        super(api, method, handlerType, info, pathPrefix, versionPrefix, contentTypeVnd);
    }

    @Override
    protected void initializePathHolders() {
        Optional.of(info.getPatternsCondition())
                .map(PatternsRequestCondition::getPatterns)
                .orElseGet(Set::of)
                .stream()
                .map(PathPattern::getPatternString)
                .forEach(pathHolders::add);
    }

    @Override
    protected void initializeContentTypeHolders() {
        initializeContentTypeHolderForExpressions(info.getConsumesCondition().getExpressions(), consumes -> contentTypeHolders.addConsumes(consumes, contentTypeVnd));
        initializeContentTypeHolderForExpressions(info.getProducesCondition().getExpressions(), produces -> contentTypeHolders.addProduces(produces, contentTypeVnd));
    }

    private void initializeContentTypeHolderForExpressions(Collection<MediaTypeExpression> expressions, Consumer<String> consumer) {
        expressions.stream()
                   .map(MediaTypeExpression::toString)
                   .forEach(consumer);
    }

    @Override
    public String getName() {
        return this.info.getName();
    }

    @Override
    public ApiBuilder combine(RequestMappingInfo other) {
        var combined = build().combine(other);
        return new ReactiveApiBuilder(api, method, handlerType, combined, pathPrefix, versionPrefix, contentTypeVnd);
    }

    @Override
    public RequestMappingInfo build() {
        var patterns = pathHolders.toReactiveCondition();
        var pair = contentTypeHolders.toReactiveCondition();
        var builder = new RequestMappingInfoBuilder(info);
        builder.addCondition(patterns);
        builder.addCondition(pair.getOne());
        builder.addCondition(pair.getTwo());
        return builder.build();
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

        ReactiveApiBuilder.RequestMappingInfoBuilder addCondition(RequestCondition condition) {
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
