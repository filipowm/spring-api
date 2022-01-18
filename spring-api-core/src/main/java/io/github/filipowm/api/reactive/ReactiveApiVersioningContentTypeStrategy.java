package io.github.filipowm.api.reactive;

import io.github.filipowm.api.AbstractApiVersioningStrategy;
import io.github.filipowm.api.ApiBuilder;
import io.github.filipowm.api.ApiUtils;
import io.github.filipowm.api.ApiVersionNamingProvider;
import io.github.filipowm.api.VersionTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.result.condition.MediaTypeExpression;
import org.springframework.web.reactive.result.method.RequestMappingInfo;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ReactiveApiVersioningContentTypeStrategy extends AbstractApiVersioningStrategy<RequestMappingInfo> implements InitializingBean {

    private final String contentTypeVnd;

    private final String versionPrefix;

    public ReactiveApiVersioningContentTypeStrategy(ApiVersionNamingProvider namingProvider, String contentTypeVnd, String versionPrefix) {
        super(namingProvider);
        this.contentTypeVnd = contentTypeVnd;
        this.versionPrefix = versionPrefix;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Enabled API versioning in content-type (e.g. application/{}.{}1+json", contentTypeVnd, versionPrefix);
    }

    @Override
    public Set<Integer> parseVersion(RequestMappingInfo requestMappingInfo) {
        return requestMappingInfo.getConsumesCondition()
                                 .getExpressions()
                                 .stream()
                                 .map(MediaTypeExpression::getMediaType)
                                 .map(MimeType::getType)
                                 .map(media -> ApiUtils.extractVersionFromContentType(media, contentTypeVnd, versionPrefix))
                                 .collect(Collectors.toSet());
    }

    @Override
    protected String convertVersion(String version) {
        var format = contentTypeVnd + ".%s";
        return String.format(format, version);
    }

    @Override
    protected VersionTarget getVersionTarget(ApiBuilder<?> builder) {
        return builder.getContentTypeHolders();
    }

}
