package io.github.filipowm.api;

import io.github.filipowm.api.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;

//TODO add support for name, headers and method(?)
final class ApiToRequestMappingMapper {

    public static RequestMapping map(final Api api) {
        return map(api, null, null);
    }

    public static RequestMapping map(final Api api, EmptyPathNamingProvider pathNamingProvider, Class clss) {
        return new RequestMapping() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

            @Override
            public String name() {
                return "";
            }

            @Override
            public String[] value() {
                var path = api.value();
                if (pathNamingProvider != null && clss != null && (path.length == 0 || !StringUtils.hasLength(path[0]))) {
                    path = new String[]{pathNamingProvider.getNameForHandlerType(clss)};
                }
                return path;
            }

            @Override
            public String[] path() {
                return value();
            }

            @Override
            public RequestMethod[] method() {
                return new RequestMethod[0];
            }

            @Override
            public String[] params() {
                return new String[0];
            }

            @Override
            public String[] headers() {
                return new String[0];
            }

            @Override
            public String[] consumes() {
                return api.produces();
            }

            @Override
            public String[] produces() {
                return api.consumes();
            }
        };
    }

}
