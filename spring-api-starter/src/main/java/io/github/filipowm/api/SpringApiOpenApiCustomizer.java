package io.github.filipowm.api;

import io.github.filipowm.api.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
class SpringApiOpenApiCustomizer implements OpenApiBuilderCustomizer {

    private final ApplicationContext applicationContext;

    @Override
    public void customise(OpenAPIService openApiService) {
        var apiBeans = applicationContext.getBeansWithAnnotation(Api.class);
        openApiService.addMappings(apiBeans);
    }
}
