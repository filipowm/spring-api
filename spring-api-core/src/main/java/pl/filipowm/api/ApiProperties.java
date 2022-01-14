//package pl.filipowm.api;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ApiProperties {
//
//    /**
//     * API path base context
//     * Default: empty string
//     */
//    @Value("${spring.api.baseContext:}")
//    private String baseContext;
//    /**
//     * Content-type vnd, e.g. if {@code contentTypeVnd='api'}, then content-type may be (depending on version)
//     * {@code application/vnd.api.v2}
//     * Default: empty string
//     */
//    @Value("${spring.api.contentTypeVnd:}")
//    private String contentTypeVnd;
//    /**
//     * API version prefix
//     * Default: <i>v</i>
//     */
//    @Value("${spring.api.versionPrefix:v}")
//    private String versionPrefix;
//    /**
//     * API path prefix, e.g. api, rest
//     * Default: <i>api</i>
//     */
//    @Value("${spring.api.pathPrefix:api}")
//    private String pathPrefix;
//    /**
//     * Flag if versioning should be in content-type, otherwise in path
//     * Default: <i>false</i> (path versioning)
//     */
//    @Value("${spring.api.versionInContentType:false}")
//    private boolean versionInContentType;
//    /**
//     * Flag if API versioning is enabled
//     * Default: <i>true</i> (versioning enabled)
//     */
//    @Value("${spring.api.enableVersioning:false}")
//    private boolean enableVersioning;
//
//
//}
