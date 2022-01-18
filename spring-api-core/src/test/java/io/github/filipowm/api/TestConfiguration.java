package io.github.filipowm.api;

import io.github.filipowm.api.servlet.ServletApiVersioningPathStrategy;
import io.github.filipowm.api.servlet.ServletApiRequestMappingHandlerMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
class TestConfiguration {

    @Bean
    ApiPathEnhancer apiPathEnhancer() {
        return new ApiPathEnhancer(ApiTestHelper.PATH_PREFIX);
    }

    @Bean
    @Primary
    EmptyPathNamingProvider emptyPathNamingProvider(ApplicationContext context) {
        return new BasePathNamingProvider();
    }

    @Bean
    ServletApiRequestMappingHandlerMapping apiRequestMappingHandlerMapping(List<ApiDecorator> apiDecorators, EmptyPathNamingProvider pathNamingProvider) {
        return new ServletApiRequestMappingHandlerMapping(apiDecorators, pathNamingProvider, ApiTestHelper.PATH_PREFIX, ApiTestHelper.VERSION_PREFIX, ApiTestHelper.CONTENT_TYPE_VND);
    }
    @Bean
    ApiVersionNamingProvider apiVersionNamingProvider() {
        return new ApiVersionNamingProvider(ApiTestHelper.VERSION_PREFIX);
    }

    @Bean
    ServletApiVersioningPathStrategy apiVersioningPathStrategy(ApiVersionNamingProvider namingProvider) {
        return new ServletApiVersioningPathStrategy(namingProvider, ApiTestHelper.VERSION_PREFIX);
    }

}
