package io.github.filipowm.api.reactive;

import io.github.filipowm.api.AbstractApiVersioningStrategy;
import io.github.filipowm.api.ApiBuilder;
import io.github.filipowm.api.ApiUtils;
import io.github.filipowm.api.ApiVersionNamingProvider;
import io.github.filipowm.api.VersionTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Component adding versioning on path, i.e. /api/v1..., /v1/...
 * If path already is versioned with this versioning schema (v1, v2, v99, ..)
 * then new pattern will added (current won't be replaced and will still exist)
 */
@Slf4j
public class ReactiveApiVersioningPathStrategy extends AbstractApiVersioningStrategy<RequestMappingInfo> implements InitializingBean {

    private final String versionPrefix;

    public ReactiveApiVersioningPathStrategy(ApiVersionNamingProvider namingProvider, String versionPrefix) {
        super(namingProvider);
        this.versionPrefix = versionPrefix;
    }

    @Override
    public void afterPropertiesSet() {
        log.info("Enabled API versioning in request path (e.g. /{}1/resource", versionPrefix);
    }

    @Override
    public Set<Integer> parseVersion(RequestMappingInfo requestMappingInfo) {
        return Optional.ofNullable(requestMappingInfo.getPatternsCondition())
                       .map(PatternsRequestCondition::getPatterns)
                       .stream()
                       .flatMap(Collection::stream)
                       .map(PathPattern::getPatternString)
                       .map(path -> ApiUtils.extractVersionFromPath(path, versionPrefix))
                       .collect(Collectors.toSet());
    }

    @Override
    protected VersionTarget getVersionTarget(ApiBuilder<?> builder) {
        return builder.getPathHolders();
    }

}
