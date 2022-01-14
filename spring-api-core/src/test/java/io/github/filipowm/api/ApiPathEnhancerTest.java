package io.github.filipowm.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ApiPathEnhancerTest extends BaseApiTest {

    private final ApiPathEnhancer enhancer = new ApiPathEnhancer(ApiTestHelper.PATH_PREFIX);

    @Test
    void shouldAddApiPrefixToPath() {
        // given
        var paths = List.of("/api/test",
                            "api/test",
                            "/test",
                            "test",

                            "/api/test55",

                            "test/test2",
                            "/test/test2"
        );
        var mappingInfo = createRequestMappingInfo(createPatternConditions(paths));
        var builder = createBuilder(mappingInfo);

        // when
        enhancer.decorate(builder);
        mappingInfo = builder.build();

        // then
        Assertions.assertThat(mappingInfo.getPatternsCondition().getPatterns())
                  .hasSize(3)
                  .containsExactly("/api/test", "/api/test55", "/api/test/test2");
    }
}
