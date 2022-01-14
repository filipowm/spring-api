package io.github.filipowm.api.http;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

/**
 * Useful request-related utilities for operating on {@link HttpServletRequest} and related classes.
 */
@UtilityClass
public final class RequestUtil {

    public Optional<RequestAttributes> getRequestAttributes() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes());
    }

    public HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        return requestAttributes != null ? requestAttributes.getRequest() : null;
    }

    private ServletRequestAttributes getServletRequestAttributes() {
        try {
            return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public boolean isXhr(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String xhrHeader = request.getHeader(HttpConst.Headers.X_REQUESTED_WITH);
        return HttpConst.XML_HTTP_REQUEST.equals(xhrHeader);
    }
}
