package pl.filipowm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
@RequiredArgsConstructor
class SpringApiAutoConfiguration {

    private final ApiProperties apiProperties;

    @Bean
    @ConditionalOnMissingBean(ApiPathEnhancer.class)
    ApiPathEnhancer apiPathEnhancer() {
        return new ApiPathEnhancer(apiProperties.getPathPrefix());
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
        return new ContextPathEnhancer(apiProperties.getBaseContext());
    }

    @Bean
    ApiRequestMappingHandlerMapping apiRequestMappingHandlerMapping(List<ApiDecorator> apiDecorators, EmptyPathNamingProvider pathNamingProvider) {
        return new ApiRequestMappingHandlerMapping(apiDecorators, pathNamingProvider, apiProperties.getPathPrefix(), apiProperties.getVersioning().getVersionPrefix(), apiProperties.getVersioning().getContentTypeVnd());
    }

    @Configuration
    @ConditionalOnProperty(value = "spring.api.versioning.enabled", havingValue = "true")
    class ApiVersioningConfiguration {
        @Bean
        @ConditionalOnMissingBean(ApiVersionNamingProvider.class)
        ApiVersionNamingProvider apiVersionNamingProvider() {
            return new ApiVersionNamingProvider(apiProperties.getVersioning().getVersionPrefix());
        }

        @Bean
        @ConditionalOnProperty(value = "spring.api.versioning.versionInContentType", havingValue = "false", matchIfMissing = true)
        @ConditionalOnMissingBean(ApiVersionStrategy.class)
        ApiVersionStrategy apiVersioningPathStrategy(ApiVersionNamingProvider namingProvider) {
            return new ApiVersioningPathStrategy(namingProvider, apiProperties.getVersioning().getVersionPrefix());
        }

        @Bean
        @ConditionalOnProperty(value = "spring.api.versioning.versionInContentType", havingValue = "true")
        @ConditionalOnMissingBean(ApiVersionStrategy.class)
        ApiVersionStrategy apiVersioningContentTypeStrategy(ApiVersionNamingProvider namingProvider) {
            return new ApiVersioningContentTypeStrategy(namingProvider, apiProperties.getVersioning().getContentTypeVnd(), apiProperties.getVersioning().getVersionPrefix());
        }
    }
}
