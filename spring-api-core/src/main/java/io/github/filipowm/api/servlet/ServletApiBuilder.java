package io.github.filipowm.api.servlet;

import io.github.filipowm.api.AbstractApiBuilder;
import io.github.filipowm.api.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.condition.MediaTypeExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Representation of API mapping to be built using servlet approach.
 * It uses base {@link RequestMappingInfo} built by {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping}
 * and then enhanced by API customizations.
 *
 * <p>Using this class, you can operate directly on original mapping ({@link #getInfo()}
 * or any other object used to customize API mapping.</p>
 */
@Slf4j
public class ServletApiBuilder extends AbstractApiBuilder<RequestMappingInfo> {

    public ServletApiBuilder(Api api, Method method, Class<?> handlerType, RequestMappingInfo info, String pathPrefix, String versionPrefix, String contentTypeVnd) {
        super(api, method, handlerType, info, pathPrefix, versionPrefix, contentTypeVnd);
    }

    protected void initializePathHolders() {
        info.getPatternValues().forEach(pathHolders::add);
        log.info("Path holders: {}", pathHolders);
    }

    protected void initializeContentTypeHolders() {
        initializeContentTypeHolderForExpressions(info.getConsumesCondition().getExpressions(), consumes -> contentTypeHolders.addConsumes(consumes, contentTypeVnd));
        initializeContentTypeHolderForExpressions(info.getProducesCondition().getExpressions(), produces -> contentTypeHolders.addProduces(produces, contentTypeVnd));
    }

    private void initializeContentTypeHolderForExpressions(Collection<MediaTypeExpression> expressions, Consumer<String> consumer) {
        expressions.stream()
                   .map(MediaTypeExpression::toString)
                   .forEach(consumer);
    }

    /**
     * Method building customized API mapping based on original mapping
     * and applied customizations
     *
     * @return API mapping object
     */
    public RequestMappingInfo build() {
        return info.mutate()
                   .paths(pathHolders.toPaths())
                   .consumes(contentTypeHolders.toConsumes())
                   .produces(contentTypeHolders.toProduces())
                   .build();
    }

    /**
     * Builds request mapping from this definition and combines with other request mapping.
     *
     * <p>This method first builds new API mapping and then it applies custom mapping.
     * It does not operate on original mapping!</p>
     *
     * <p>This operation can be chained!</p>
     *
     * <p><b>IMPORTANT:</b> Use it with care, as it may completely change how given mapping is built,
     * however it offers most powerful capabilities, because you can define custom request mapping
     * and apply it to original.</p>
     *
     * @return new API builder with applied custom request mapping.
     */
    public ServletApiBuilder combine(RequestMappingInfo other) {
        var combined = build().combine(other);
        return new ServletApiBuilder(api, method, handlerType, combined, pathPrefix, versionPrefix, contentTypeVnd);
    }

    /**
     * @return original mapping name. Shorthand for {@code getInfo().getName();}
     */
    public String getName() {
        return this.info.getName();
    }

}
