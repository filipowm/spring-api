package io.github.filipowm.api.reactive;

import io.github.filipowm.api.servlet.ServletApiRequestMappingHandlerMapping;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.OpenAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Locale;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReactiveApiRequestMappingHandlerMapping reactiveApiRequestMappingHandlerMapping;

    @Autowired
    private OpenAPIService openAPIService;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void shouldSuccessfullyUseReactiveEndpoints() {
        when()
            .get("/api/v2/tests/first")
            .then()
            .statusCode(200);
    }

    @Test
    void shouldSuccessfullyUseStandardEndpoints() {
        when()
            .get("/api/v2/tests/second")
            .then()
            .statusCode(200);
    }

    @Test
    void shouldCreateProperBeans() {
        assertThat(reactiveApiRequestMappingHandlerMapping).isNotNull();
    }

    @Test
    void shouldGenerateApiDocs() {
        // when
        openAPIService.build(Locale.US);

        // then
        var mappings = openAPIService.getMappingsMap();
        assertThat(mappings).containsKey("testController");
        assertThat(mappings.get("testController").getClass()).isEqualTo(TestController.class);
    }
}
