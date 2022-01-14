package pl.filipowm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import pl.filipowm.api.annotations.ApiVersion;

import java.util.Set;

@RequiredArgsConstructor
abstract class AbstractApiVersioningStrategy implements ApiVersionStrategy {

    private final ApiVersionNamingProvider namingProvider;

    @Override
    public void decorate(ApiBuilder builder) {
        var apiVersion = builder.getApiVersion();
        if (apiVersion == null || apiVersion.value() == ApiVersion.UNVERSIONED) {
            return;
        }
        var version = namingProvider.nameForVersion(apiVersion.value());
        var convertedVersion = convertVersion(version);
        getVersionTarget(builder).setVersion(convertedVersion);
    }

    @Override
    public Set<Integer> parseVersion(RequestMappingInfo requestMappingInfo) {
        return null; //TODO
    }

    protected String convertVersion(String version) {
        return version;
    }

    protected abstract VersionTarget getVersionTarget(ApiBuilder builder);
}
