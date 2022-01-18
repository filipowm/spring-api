package io.github.filipowm.api;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiPathEnhancer implements ApiDecorator {

    private final String pathPrefix;

    @Override
    public void decorate(ApiBuilder<?> builder) {
        builder.getPathHolders().applyApi(pathPrefix);
    }

    @Override
    public boolean supports(ApiBuilder<?> builder) {
        return builder.getApi() != null;
    }
}
