package io.github.filipowm.api;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.filipowm.api.annotations.ApiVersion.UNVERSIONED;

class ApiUtilsTest {

    private static Stream<Arguments> versionedPaths() {
        return Stream.of(
            Arguments.of("/test/", UNVERSIONED),
            Arguments.of(ApiTestHelper.PATH_PREFIX, UNVERSIONED),
            Arguments.of(null, UNVERSIONED),
            Arguments.of(ApiTestHelper.VERSION_PREFIX + "-1", -1),
            Arguments.of(ApiTestHelper.VERSION_PREFIX + "0", 0),
            Arguments.of(ApiTestHelper.VERSION_PREFIX + "99", 99),
            Arguments.of("/" + ApiTestHelper.PATH_PREFIX + "/" + ApiTestHelper.VERSION_PREFIX + "99/test", 99),
            Arguments.of("/" + ApiTestHelper.PATH_PREFIX + "/vtest/" + ApiTestHelper.VERSION_PREFIX + "99/v1/test", 99)
        );
    }

    private static Stream<Arguments> versionedContentTypes() {
        return Stream.of(
            Arguments.of("text/html", UNVERSIONED),
            Arguments.of(ApiTestHelper.VERSION_PREFIX + "1+json", UNVERSIONED),
            Arguments.of(null, UNVERSIONED),
            Arguments.of("", UNVERSIONED),
            Arguments.of("application/" + ApiTestHelper.CONTENT_TYPE_VND + "+json", UNVERSIONED),
            Arguments.of("application/" + ApiTestHelper.CONTENT_TYPE_VND + ".version1+json", UNVERSIONED),
            Arguments.of("application/" + ApiTestHelper.CONTENT_TYPE_VND + "." + ApiTestHelper.VERSION_PREFIX + "1+json", 1),
            Arguments.of("test/" + ApiTestHelper.CONTENT_TYPE_VND + "." + ApiTestHelper.VERSION_PREFIX + "99/test", 99)
        );
    }

    @ParameterizedTest
    @MethodSource("versionedPaths")
    void shouldExtractVersionFromPath(String path, int expectedVersion) {
        assertThat(ApiUtils.extractVersionFromPath(path, ApiTestHelper.VERSION_PREFIX))
            .isEqualTo(expectedVersion);
    }

    @ParameterizedTest
    @MethodSource("versionedContentTypes")
    void shouldExtractVersionFromContentType(String contentType, int expectedVersion) {
        assertThat(ApiUtils.extractVersionFromContentType(contentType, ApiTestHelper.CONTENT_TYPE_VND, ApiTestHelper.VERSION_PREFIX))
            .isEqualTo(expectedVersion);
    }

    @Test
    void shouldApplyVersionToTarget() {
        // given
        var targets = List.of(new TestVersionTarget(), new TestVersionTarget());

        // when
        ApiUtils.applyVersion(targets, "5");

        // then
        assertThat(targets)
            .map(VersionTarget::getVersion)
            .containsExactly("5", "5");
    }

    @Data
    private static class TestVersionTarget implements VersionTarget {
        private String version;
    }
}
