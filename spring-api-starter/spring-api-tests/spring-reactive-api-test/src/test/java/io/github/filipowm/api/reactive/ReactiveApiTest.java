package io.github.filipowm.api.reactive;

import io.github.filipowm.api.servlet.ServletApiRequestMappingHandlerMapping;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReactiveApiRequestMappingHandlerMapping reactiveApiRequestMappingHandlerMapping;

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
}
