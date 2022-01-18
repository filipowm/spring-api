package io.github.filipowm.api.servlet;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServletApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ServletApiRequestMappingHandlerMapping servletApiRequestMappingHandlerMapping;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    public void shouldSuccessfullyUseStandardEndpoints(){
        when()
            .get("/api/v2/tests/first")
            .then()
            .statusCode(200);
    }

    @Test
    public void shouldSuccessfullyUseStandardsEndpointsWithVersionedIndividualEndpoint(){
        when()
            .get("/api/v3/tests/second")
            .then()
            .statusCode(200);
    }

    @Test
    void shouldCreateProperBeans() {
        assertThat(servletApiRequestMappingHandlerMapping).isNotNull();
    }
}
