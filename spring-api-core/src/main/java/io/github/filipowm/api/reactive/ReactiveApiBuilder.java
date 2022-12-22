package io.github.filipowm.api.reactive;

import io.github.filipowm.api.AbstractApiBuilder;
import io.github.filipowm.api.ApiBuilder;
import io.github.filipowm.api.annotations.Api;
import org.springframework.web.reactive.result.condition.MediaTypeExpression;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
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
        return info.mutate()
                   .paths(pathHolders.toPaths())
                   .consumes(contentTypeHolders.toConsumes())
                   .produces(contentTypeHolders.toProduces())
                   .build();
    }
}
