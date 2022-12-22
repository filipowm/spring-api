package io.github.filipowm.api;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.service.OpenAPIService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ConditionalOnClass(name = "org.springdoc.core.service.OpenAPIService")
@ConditionalOnBean(OpenAPIService.class)
@AutoConfigureOrder(Integer.MAX_VALUE)
public class SpringDocOpenApiAutoConfiguration implements InitializingBean {

    @Bean
    OpenApiBuilderCustomizer springApiCustomizer(ApplicationContext context) {
        return new SpringApiOpenApiCustomizer(context);
    }

    @Override
    public void afterPropertiesSet() {
        log.info("Customizing SpringDoc OpenAPI to generate documentation based on Spring API");
    }

}
