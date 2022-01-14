package pl.filipowm.api;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BasePathNamingProviderTest {

    private final BasePathNamingProvider provider = new BasePathNamingProvider();

    private static Stream<Arguments> handlers() {
        return Stream.of(
            Arguments.of(TestController.class, "test"),
            Arguments.of(ApiTestController.class, "apiTest")
        );
    }

    @ParameterizedTest
    @MethodSource("handlers")
    void shouldCreateBasePathName(Class handler, String expectedName) {
        var actualName = provider.getNameForHandlerType(handler);
        assertThat(actualName).isEqualTo(expectedName);
    }
}