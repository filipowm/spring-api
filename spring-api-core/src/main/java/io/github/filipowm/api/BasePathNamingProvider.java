package io.github.filipowm.api;

import org.springframework.util.StringUtils;

public class BasePathNamingProvider implements EmptyPathNamingProvider {

    private static final String[] ENDPOINT_DENOMINATORS = {"restcontroller", "apicontroller", "api", "controller", "endpoint", "resource"};

    @Override
    public String getNameForHandlerType(Class handler) {
        var name = StringUtils.uncapitalize(handler.getSimpleName());
        for (var denominator : ENDPOINT_DENOMINATORS) {
            int idx = name.toLowerCase().lastIndexOf(denominator);
            if (idx > 1) {
                return name.substring(0, idx);
            }
        }
        return name;
    }
}
