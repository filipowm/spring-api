package io.github.filipowm.api;

import io.github.filipowm.api.annotations.ApiVersion;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractApiVersioningStrategy<T> implements ApiVersionStrategy<T> {

    private final ApiVersionNamingProvider namingProvider;

    @Override
    public void decorate(ApiBuilder<?> builder) {
        var apiVersion = builder.getApiVersion();
        if (apiVersion == null || apiVersion.value() == ApiVersion.UNVERSIONED) {
            return;
        }
        var version = namingProvider.nameForVersion(apiVersion.value());
        var convertedVersion = convertVersion(version);
        getVersionTarget(builder).setVersion(convertedVersion);
    }

    protected String convertVersion(String version) {
        return version;
    }

    protected abstract VersionTarget getVersionTarget(ApiBuilder<?> builder);
}
