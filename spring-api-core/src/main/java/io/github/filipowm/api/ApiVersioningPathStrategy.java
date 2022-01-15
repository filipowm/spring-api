package io.github.filipowm.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

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
public class ApiVersioningPathStrategy extends AbstractApiVersioningStrategy implements InitializingBean {

    private final String versionPrefix;

    public ApiVersioningPathStrategy(ApiVersionNamingProvider namingProvider, String versionPrefix) {
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
                       .map(path -> ApiUtils.extractVersionFromPath(path, versionPrefix))
                       .collect(Collectors.toSet());
    }

    @Override
    protected VersionTarget getVersionTarget(ApiBuilder builder) {
        return builder.getPathHolders();
    }

}