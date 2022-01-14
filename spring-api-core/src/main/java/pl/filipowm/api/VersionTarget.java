package pl.filipowm.api;

import org.springframework.util.StringUtils;

/**
 * Interface used by objects which may store
 * API versioning scheme and be responsible for API versioning.
 */
interface VersionTarget {

    String getVersion();

    void setVersion(String version);

    default boolean isVersioned() {
        return StringUtils.hasLength(getVersion());
    }

}
