package io.github.filipowm.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Spring API configuration properties.
 */
@ConfigurationProperties(prefix = "spring.api")
@ConstructorBinding
@Getter
@EqualsAndHashCode
@ToString
public class ApiProperties {

    /**
     * API path prefix, e.g. api, rest
     * Default: <i>/api</i>
     */
    private final String pathPrefix;

    /**
     * API path base context
     * Default: empty string
     */
    private final String baseContext;

    /**
     * API versioning properties
     */
    private final ApiVersioningProperties versioning;

    public ApiProperties(@DefaultValue("/api") String pathPrefix,
                         @DefaultValue("") String baseContext,
                         @DefaultValue ApiVersioningProperties versioning) {
        this.pathPrefix = pathPrefix;
        this.baseContext = baseContext;
        this.versioning = versioning;
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    @ConstructorBinding
    public static class ApiVersioningProperties {

        /**
         * Flag if API versioning is enabled
         * Default: <i>true</i> (versioning enabled)
         */
        private final boolean enabled;

        /**
         * API version prefix
         * Default: <i>v</i>
         */
        private final String versionPrefix;

        /**
         * Content-type vnd, e.g. if {@code contentTypeVnd='api'}, then content-type may be (depending on version)
         * {@code application/vnd.api.v2}
         * Default: empty string
         */
        private final String contentTypeVnd;

        /**
         * Flag if versioning should be in content-type, otherwise in path
         * Default: <i>false</i> (path versioning)
         */
        private final boolean versionInContentType;

        public ApiVersioningProperties(@DefaultValue("true") boolean enabled,
                                       @DefaultValue("v") String versionPrefix,
                                       @DefaultValue("") String contentTypeVnd,
                                       @DefaultValue("false") boolean versionInContentType) {
            this.enabled = enabled;
            this.versionPrefix = versionPrefix;
            this.contentTypeVnd = contentTypeVnd;
            this.versionInContentType = versionInContentType;
        }
    }

}
