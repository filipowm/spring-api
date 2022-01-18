package io.github.filipowm.api;

import io.github.filipowm.api.servlet.ServletApiVersioningPathStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import io.github.filipowm.api.annotations.Api;
import io.github.filipowm.api.annotations.ApiVersion;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiVersionPathStrategyTest extends BaseApiTest {

    private static final String VERSION_1 = "v1";

    private ApiVersionNamingProvider provider = mock(ApiVersionNamingProvider.class);

    private final ServletApiVersioningPathStrategy strategy = new ServletApiVersioningPathStrategy(provider, ApiTestHelper.VERSION_PREFIX);

    @BeforeEach
    void setup() {
        when(provider.nameForVersion(ArgumentMatchers.anyInt())).thenReturn(VERSION_1);
        when(provider.nameForVersion(ArgumentMatchers.anyInt(), ArgumentMatchers.anyBoolean())).thenReturn(VERSION_1);
    }

    @Test
    void shouldApplyVersionToPath() {
        // given
        var paths = List.of(
            "/api/test",
            "/test",
            "v1/",
            "v1/test",
            "/api/v1/test"
        );
        var mappingInfo = createRequestMappingInfo(createPatternConditions(paths));
        var version = mock(ApiVersion.class);
        when(version.value()).thenReturn(1);
        var api = mock(Api.class);
        when(api.version()).thenReturn(version);
        var builder = createBuilder(mappingInfo, api);

        // when
        strategy.decorate(builder);
        mappingInfo = builder.build();

        // then
        assertThat(mappingInfo.getPatternsCondition().getPatterns())
            .hasSize(3)
            .allMatch(path -> path.contains(VERSION_1));
    }
}
