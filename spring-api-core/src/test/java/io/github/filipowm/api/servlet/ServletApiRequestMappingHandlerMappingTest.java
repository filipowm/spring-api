package io.github.filipowm.api.servlet;

import io.github.filipowm.api.ApiDecorator;
import io.github.filipowm.api.ApiTestHelper;
import io.github.filipowm.api.EmptyPathNamingProvider;
import io.github.filipowm.api.servlet.ServletApiRequestMappingHandlerMapping;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.filipowm.api.annotations.Api;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServletApiRequestMappingHandlerMappingTest {

    @Test
    void shouldDecorateApiWhenClassAnnotatedWithApi() {
        verifyApiDecoration(Test1.class, true);
    }

    @Test
    void shouldNotDecorateApiWhenClassNotAnnotatedWithApi() {
        verifyApiDecoration(Test2.class, false);
    }

    @SneakyThrows
    private void verifyApiDecoration(Class clss, boolean shouldDecorate) {
        // given
        var expectedInvocations = times(shouldDecorate ? 1 : 0);
        var decorator = mock(ApiDecorator.class);
        var namingProvider = mock(EmptyPathNamingProvider.class);
        var mapping = new ServletApiRequestMappingHandlerMapping(List.of(decorator), namingProvider, ApiTestHelper.PATH_PREFIX, ApiTestHelper.VERSION_PREFIX, ApiTestHelper.CONTENT_TYPE_VND);
        when(decorator.supports(any())).thenReturn(true);
        when(namingProvider.getNameForHandlerType(clss)).thenReturn("test");

        // when
        mapping.getMappingForMethod(clss.getMethod("test"), clss);

        // then
        verify(decorator, expectedInvocations).decorate(any());
        verify(decorator, expectedInvocations).supports(any());
        verify(namingProvider, expectedInvocations).getNameForHandlerType(clss);
    }

    @Api
    private static class Test1 {
//        @GetMapping
//        public void test() {
//
//        }

        @GetMapping
        public Mono<String> test() {
            return Mono.just("one");
        }
    }

    @RestController
    private static class Test2 {
        @GetMapping
        public void test() {

        }
    }
}
