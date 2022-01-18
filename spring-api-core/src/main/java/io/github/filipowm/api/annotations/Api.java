package io.github.filipowm.api.annotations;

import io.github.filipowm.api.ApiPathEnhancer;
import io.github.filipowm.api.servlet.ServletApiRequestMappingHandlerMapping;
import io.github.filipowm.api.ApiVersionStrategy;
import io.github.filipowm.api.BasePathNamingProvider;
import io.github.filipowm.api.ContextPathEnhancer;
import io.github.filipowm.api.EmptyPathNamingProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to mark your controllers eligible for spring-api handling. It is applicable only on type-level.
 * Controllers marked with <b>@Api</b> will be registered by {@link ServletApiRequestMappingHandlerMapping}
 * Configuration is similar to {@link org.springframework.web.bind.annotation.RequestMapping}, but currently limited to:
 * <ul>
 *     <li><pre>value</pre></li>
 *     <li><pre>consumes</pre></li>
 *     <li><pre>produces</pre></li>
 * </ul>
 * Additional parameters offered by this annotation are:
 * <ul>
 *     <li><pre>baseContext</pre></li>
 *     <li><pre>version</pre></li>
 * </ul>
 * <p>
 * No parameters are mandatory and additional {@link org.springframework.web.bind.annotation.RequestMapping} must
 * be defined on method level. If <i>value</i> parameter is missing, then {@link EmptyPathNamingProvider}
 * is used to determine path. By default {@link BasePathNamingProvider} is used.
 * <p>
 * Example:
 * <pre>
 * &#64;Api(value = "examples",
 *      consumes = "application/json",
 *      produces = "application/json",
 *      version = &#64;ApiVersion(2))
 * class ExampleController {
 *
 *     &#64;GetMapping // 1
 *     Object doFirst() {
 *         ...
 *     }
 *
 *     &#64;RequestMapping(path = "second", method = RequestMethod.GET, produces = "text/html") // 2
 *     Object doSecond() {
 *         ...
 *     }
 *
 *     &#64;PostMapping("third")
 *     &#64;ApiVersion(3) // 3
 *     Object doThird() {
 *         ...
 *     }
 * }
 * </pre>
 * <p>
 * Depending on your other configuration of versioning ({@link ApiVersionStrategy}), path
 * prefix ({@link ApiPathEnhancer}) and base context ({@link ContextPathEnhancer},
 * resulting endpoint configuration may be following:
 * <ol>
 *     <li>GET mapping is created with path <i>api/v2/examples</i></li>
 *     <li>GET mapping is created with path <i>api/v2/examples/second</i></li>
 *     <li>POST mapping is created with path <i>api/v3/examples</i></li>
 * </ol>
 *
 * <b>IMPORTANT:</b> if you mark same controller class with {@link org.springframework.stereotype.Controller}
 * or {@link org.springframework.web.bind.annotation.RestController}, mappings will registered twice, including
 * {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping} strategy.
 * <p>
 * Swagger, if used, automatically adds a documentation for API provided by
 * annotated controller.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@ResponseBody
public @interface Api {

    /**
     * The primary mapping expressed by this annotation.
     * <p>In a Servlet environment this is an alias for <i>path</i>.
     * For example {@code @Api("foo")} is equivalent to
     * {@code @RequestMapping(path="foo")}.
     * All method-level mappings inherit this primary mapping,
     * narrowing it for a specific handler method.
     */
    String[] value() default {};

    /**
     * The consumable media types of the mapped request, narrowing the primary mapping.
     * <p>The format is a single media type or a sequence of media types,
     * with a request only mapped if the {@code Content-Type} matches one of these media types.
     * Examples:
     * <pre class="code">
     * consumes = "text/plain"
     * consumes = {"text/plain", "application/*"}
     * </pre>
     * Expressions can be negated by using the "!" operator, as in "!text/plain", which matches
     * all requests with a {@code Content-Type} other than "text/plain".
     * <p>
     * All method-level mappings override this consumes restriction.
     *
     * @see org.springframework.http.MediaType
     * @see javax.servlet.http.HttpServletRequest#getContentType()
     */
    String[] consumes() default {};

    /**
     * The producible media types of the mapped request, narrowing the primary mapping.
     * <p>The format is a single media type or a sequence of media types,
     * with a request only mapped if the {@code Accept} matches one of these media types.
     * Examples:
     * <pre class="code">
     * produces = "text/plain"
     * produces = {"text/plain", "application/*"}
     * produces = "application/json; charset=UTF-8"
     * </pre>
     * <p>It affects the actual content type written, for example to produce a JSON response
     * with UTF-8 encoding, {@code "application/json; charset=UTF-8"} should be used.
     * <p>Expressions can be negated by using the "!" operator, as in "!text/plain", which matches
     * all requests with a {@code Accept} other than "text/plain".
     * <p>All method-level mappings override this produces restriction.
     *
     * @see org.springframework.http.MediaType
     */
    String[] produces() default {};

    /**
     * The base context distinguishing multiple APIs, especially useful in distributed
     * environments (e.g. in microservices), where you may have multiple same paths.
     * Example:
     * <pre class="code">
     * baseContext = "foo"
     * </pre>
     * In given example, path will be mapped as <i>PATH_PREFIX/VERSION/<b>foo</b>/RESOURCE</i>
     * <p>If no base context is used, it is simply omitted and not added to path mapping</p>
     * <p>Base context defined here overrides one defined globally for your application!</p>
     */
    String baseContext() default "";

    /**
     * The version of API, applied to path mapping or content-type based on chosen versioning strategy.
     * Method-level version definition override this version definition.
     */
    ApiVersion version() default @ApiVersion;
}
