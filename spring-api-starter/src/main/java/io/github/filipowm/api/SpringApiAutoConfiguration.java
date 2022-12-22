package io.github.filipowm.api;

import io.github.filipowm.api.reactive.ReactiveApiRequestMappingHandlerMapping;
import io.github.filipowm.api.reactive.ReactiveApiVersioningPathStrategy;
import io.github.filipowm.api.servlet.ServletApiRequestMappingHandlerMapping;
import io.github.filipowm.api.servlet.ServletApiVersioningContentTypeStrategy;
import io.github.filipowm.api.servlet.ServletApiVersioningPathStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(ApiProperties.class)
@RequiredArgsConstructor
@Slf4j
@ConditionalOnWebApplication
public class SpringApiAutoConfiguration {

    private final ApiProperties apiProperties;

    @Bean
    @ConditionalOnMissingBean(ApiPathEnhancer.class)
    ApiPathEnhancer apiPathEnhancer() {
        return new ApiPathEnhancer(apiProperties.pathPrefix());
    }

    @Bean
    @Primary
    EmptyPathNamingProvider emptyPathNamingProvider(ApplicationContext context) {
        var provider = BeanUtils.getBean(context, EmptyPathNamingProvider.class);
        return provider.orElseGet(BasePathNamingProvider::new);
    }

    @Bean
    @ConditionalOnMissingBean(ContextPathEnhancer.class)
    ContextPathEnhancer contextPathEnhancer() {
        return new ContextPathEnhancer(apiProperties.baseContext());
    }

    @Configuration
    @ConditionalOnServletStack
    @AutoConfigureAfter(ServletWebServerFactoryAutoConfiguration.class)
    class ServletApiConfiguration {

        @Bean
        ServletApiRequestMappingHandlerMapping servletApiRequestMappingHandlerMapping(List<ApiDecorator> apiDecorators, EmptyPathNamingProvider pathNamingProvider) {
            return new ServletApiRequestMappingHandlerMapping(apiDecorators, pathNamingProvider, apiProperties.pathPrefix(), apiProperties.versioning().versionPrefix(),
                                                              apiProperties.versioning().contentTypeVnd());
        }
    }

    @Configuration
    @ConditionalOnReactiveStack
    @AutoConfigureAfter(WebFluxAutoConfiguration.class)
    class ReactiveApiConfiguration {

        @Bean
        ReactiveApiRequestMappingHandlerMapping reactiveApiRequestMappingHandlerMapping(List<ApiDecorator> apiDecorators, EmptyPathNamingProvider pathNamingProvider) {
            return new ReactiveApiRequestMappingHandlerMapping(apiDecorators, pathNamingProvider, apiProperties.pathPrefix(), apiProperties.versioning().versionPrefix(),
                                                               apiProperties.versioning().contentTypeVnd());
        }
    }

    @Configuration
    @ConditionalOnProperty(value = "spring.api.versioning.enabled", havingValue = "true", matchIfMissing = true)
    class ApiVersioningConfiguration {
        @Bean
        @ConditionalOnMissingBean(ApiVersionNamingProvider.class)
        ApiVersionNamingProvider apiVersionNamingProvider() {
            return new ApiVersionNamingProvider(apiProperties.versioning().versionPrefix());
        }

        @Bean
        @ConditionalOnProperty(value = "spring.api.versioning.versionInContentType", havingValue = "false", matchIfMissing = true)
        @ConditionalOnMissingBean(ApiVersionStrategy.class)
        @ConditionalOnServletStack
        ApiVersionStrategy servletApiVersioningPathStrategy(ApiVersionNamingProvider namingProvider) {
            return new ServletApiVersioningPathStrategy(namingProvider, apiProperties.versioning().versionPrefix());
        }

        @Bean
        @ConditionalOnProperty(value = "spring.api.versioning.versionInContentType", havingValue = "false", matchIfMissing = true)
        @ConditionalOnMissingBean(ApiVersionStrategy.class)
        @ConditionalOnReactiveStack
        ApiVersionStrategy reactiveApiVersioningPathStrategy(ApiVersionNamingProvider namingProvider) {
            return new ReactiveApiVersioningPathStrategy(namingProvider, apiProperties.versioning().versionPrefix());
        }

        @Bean
        @ConditionalOnProperty(value = "spring.api.versioning.versionInContentType", havingValue = "true")
        @ConditionalOnMissingBean(ApiVersionStrategy.class)
        @ConditionalOnServletStack
        ApiVersionStrategy servletVersioningContentTypeStrategy(ApiVersionNamingProvider namingProvider) {
            return new ServletApiVersioningContentTypeStrategy(namingProvider, apiProperties.versioning().contentTypeVnd(), apiProperties.versioning().versionPrefix());
        }

        @Bean
        @ConditionalOnProperty(value = "spring.api.versioning.versionInContentType", havingValue = "true")
        @ConditionalOnMissingBean(ApiVersionStrategy.class)
        @ConditionalOnReactiveStack
        ApiVersionStrategy reactiveVersioningContentTypeStrategy(ApiVersionNamingProvider namingProvider) {
            return new ServletApiVersioningContentTypeStrategy(namingProvider, apiProperties.versioning().contentTypeVnd(), apiProperties.versioning().versionPrefix());
        }
    }
}
