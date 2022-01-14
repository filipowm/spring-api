package io.github.filipowm.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringApiAutoConfigurationTest {

    @Autowired
    private ApiRequestMappingHandlerMapping apiRequestMappingHandlerMapping;

    @Test
    void testContextIsUp() {
        Assertions.assertThat(apiRequestMappingHandlerMapping).isNotNull();
    }

}
