package pl.filipowm.api;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PathHolderTest {

    private static final String API_PREFIX = "api";
    private static final String API_VERSION = "v7";

    private static Stream<Arguments> args() {
        return Stream.of(
            Arguments.of("resource/test", false, null),
            Arguments.of("api/resource/test", true, null),
            Arguments.of("v7/resource/test", false, API_VERSION),
            Arguments.of("api/v7/resource/test", true, API_VERSION)
        );
    }

    @ParameterizedTest
    @MethodSource("args")
    void shouldPathHolderDealWithPathProperly(String path, boolean isApi, String version) {
        // when:
        var holder = PathHolder.with(path, ApiTestHelper.PATH_PREFIX, ApiTestHelper.VERSION_PREFIX);

        // then:
        assertThat(holder.isApi()).isEqualTo(isApi);
        assertThat(holder.getApiPrefix()).isEqualTo(isApi ? API_PREFIX : null);
        assertThat(holder.getVersion()).isEqualTo(version);
        assertThat(holder.toPath()).isEqualTo(path);

        // when:
        holder.setApi(API_PREFIX);

        // then:
        assertThat(holder.isApi()).isTrue();
        assertThat(holder.getApiPrefix()).isEqualTo(API_PREFIX);
        assertThat(holder.getVersion()).isEqualTo(version);
        assertThat(holder.toPath()).isEqualTo(getPath(true, version, null, path));

        // when:
        holder.setContext("testContext");

        // then:
        assertThat(holder.isApi()).isTrue();
        assertThat(holder.getApiPrefix()).isEqualTo(API_PREFIX);
        assertThat(holder.getVersion()).isEqualTo(version);
        assertThat(holder.getContext()).isEqualTo("testContext");
        assertThat(holder.toPath()).isEqualTo(getPath(true, version, "testContext", path));

        // when:
        holder.setVersion("v99");

        // then:
        assertThat(holder.isApi()).isTrue();
        assertThat(holder.getApiPrefix()).isEqualTo(API_PREFIX);
        assertThat(holder.getVersion()).isEqualTo(version != null ? version : "v99");
        assertThat(holder.getContext()).isEqualTo("testContext");
        assertThat(holder.toPath()).isEqualTo(getPath(true, version != null ? version : "v99", "testContext", path));
    }

    private String getPath(boolean isApi, String version, String context, String path) {
        var sb = new StringBuilder(path);
        if (isApi && !path.startsWith(API_PREFIX)) {
            sb.insert(0, API_PREFIX + '/');
        }
        if (version != null && !path.contains(version + '/')) {
            int position = 0;
            if (isApi) {
                position += API_PREFIX.length() + 1;
            }
            sb.insert(position, version + '/');
        }

        if (context != null && !path.contains(context + "/")) {
            int position = 0;
            if (isApi) {
                position += API_PREFIX.length() + 1;
            }
            if (version != null) {
                position += version.length() + 1;
            }
            sb.insert(position, context + '/');
        }
        return sb.toString();
    }
}
