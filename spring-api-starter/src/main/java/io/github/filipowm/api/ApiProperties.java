package io.github.filipowm.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Spring API configuration properties.
 */
@ConfigurationProperties(prefix = "spring.api")
public record ApiProperties(

    /**
     * API path prefix, e.g. api, rest
     * Default: <i>/api</i>
     */
    @DefaultValue("/api") String pathPrefix,

    /**
     * API path base context
     * Default: empty string
     */
    @DefaultValue("") String baseContext,

    /**
     * API versioning properties
     */
    @DefaultValue ApiVersioningProperties versioning) {

    public record ApiVersioningProperties(

        /**
         * Flag if API versioning is enabled
         * Default: <i>true</i> (versioning enabled)
         */
        @DefaultValue("true") boolean enabled,

        /**
         * API version prefix
         * Default: <i>v</i>
         */
        @DefaultValue("v") String versionPrefix,

        /**
         * Content-type vnd, e.g. if {@code contentTypeVnd='api'}, then content-type may be (depending on version)
         * {@code application/vnd.api.v2}
         * Default: empty string
         */
        @DefaultValue("") String contentTypeVnd,

        /**
         * Flag if versioning should be in content-type, otherwise in path
         * Default: <i>false</i> (path versioning)
         */
        @DefaultValue("false") boolean versionInContentType) {
    }

}
