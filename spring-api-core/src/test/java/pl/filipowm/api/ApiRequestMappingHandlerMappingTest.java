package pl.filipowm.api;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.filipowm.api.annotations.Api;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApiRequestMappingHandlerMappingTest {

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
        var mapping = new ApiRequestMappingHandlerMapping(List.of(decorator), namingProvider, ApiTestHelper.PATH_PREFIX, ApiTestHelper.VERSION_PREFIX, ApiTestHelper.CONTENT_TYPE_VND);
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
        @GetMapping
        public void test() {

        }
    }

    @RestController
    private static class Test2 {
        @GetMapping
        public void test() {

        }
    }
}
