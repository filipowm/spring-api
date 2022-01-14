package pl.filipowm.api;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ApiVersionNamingProviderTest {
    private static final ApiVersionNamingProvider provider = new ApiVersionNamingProvider(ApiTestHelper.VERSION_PREFIX);

    @ParameterizedTest
    @CsvSource({"-1,''", "0,''", "1,v1"})
    void shouldCreateVersionString(int version, String expectedVersion) {
        var actualVersion = provider.nameForVersion(version);
        assertThat(actualVersion).isEqualTo(expectedVersion);
    }
}
