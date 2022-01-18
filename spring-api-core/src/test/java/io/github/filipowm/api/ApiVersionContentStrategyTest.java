package io.github.filipowm.api;

import io.github.filipowm.api.servlet.ServletApiVersioningContentTypeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.MediaTypeExpression;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import io.github.filipowm.api.annotations.Api;
import io.github.filipowm.api.annotations.ApiVersion;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiVersionContentStrategyTest extends BaseApiTest {

    private static final String VERSION_1 = "v1";
    
    private ApiVersionNamingProvider provider = mock(ApiVersionNamingProvider.class);

    private final ServletApiVersioningContentTypeStrategy strategy = new ServletApiVersioningContentTypeStrategy(provider, ApiTestHelper.CONTENT_TYPE_VND, ApiTestHelper.VERSION_PREFIX);

    private static Stream<Arguments> contentTypes() {
        return Stream.of(
            Arguments.of("application/json", "application/" + ApiTestHelper.CONTENT_TYPE_VND + ".v1+json"),
            Arguments.of("text/html", "text/" + ApiTestHelper.CONTENT_TYPE_VND + ".v1+html"),
            Arguments.of("application/" + ApiTestHelper.CONTENT_TYPE_VND + ".v1+json", "application/" + ApiTestHelper.CONTENT_TYPE_VND + ".v1+json"),
            Arguments.of("application/" + ApiTestHelper.CONTENT_TYPE_VND + ".v2+json", "application/" + ApiTestHelper.CONTENT_TYPE_VND + ".v2+json")
        );
    }

    @BeforeEach
    void setup() {
        when(provider.nameForVersion(ArgumentMatchers.anyInt())).thenReturn(VERSION_1);
        when(provider.nameForVersion(ArgumentMatchers.anyInt(), ArgumentMatchers.anyBoolean())).thenReturn(VERSION_1);
    }

    @ParameterizedTest
    @MethodSource("contentTypes")
    void shouldApplyVersionToContentType(String contentType, String expectedContentType) {
        // given
        var consumer = new ConsumesRequestCondition(contentType);
        var producer = new ProducesRequestCondition(contentType);
        var mappingInfo = createRequestMappingInfo(consumer, producer);
        var version = mock(ApiVersion.class);
        when(version.value()).thenReturn(1);
        var api = mock(Api.class);
        when(api.version()).thenReturn(version);
        var builder = createBuilder(mappingInfo, api);

        // when
        strategy.decorate(builder);
        mappingInfo = builder.build();

        // then
        var cons = mappingInfo.getConsumesCondition().getExpressions();
        var prod = mappingInfo.getProducesCondition().getExpressions();
        var conditions = new ArrayList<MediaTypeExpression>();
        conditions.addAll(cons);
        conditions.addAll(prod);
        assertThat(conditions)
            .map(MediaTypeExpression::getMediaType)
            .map(MediaType::toString)
            .allMatch(ct -> ct.equals(expectedContentType));
    }

    @Test
    void shouldNotApplyVersionIfNotContentTypeDefined() {
        // given
        var mappingInfo = createRequestMappingInfo();
        var builder = createBuilder(mappingInfo);

        // when
        strategy.decorate(builder);
        mappingInfo = builder.build();

        // then
        assertThat(mappingInfo.getConsumesCondition().getExpressions()).isEmpty();
        assertThat(mappingInfo.getProducesCondition().getExpressions()).isEmpty();
    }
}
