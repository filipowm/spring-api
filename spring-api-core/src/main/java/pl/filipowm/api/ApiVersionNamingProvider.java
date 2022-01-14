package pl.filipowm.api;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ApiVersionNamingProvider {

    static final String LATEST_VERSION = "latest"; //TODO add support for handling latest

    private final String versionPrefix;

    String nameForVersion(int version) {
        return version < 1 ? "" : versionPrefix + version;
    }

    String nameForVersion(int version, boolean isLatest) {
        throw new UnsupportedOperationException("To be implemented");
//        return isLatest ? LATEST_VERSION : nameForVersion(version);
    }
}
