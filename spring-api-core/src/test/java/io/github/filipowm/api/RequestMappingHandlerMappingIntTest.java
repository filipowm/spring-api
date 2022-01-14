package io.github.filipowm.api;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Map;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@WebAppConfiguration
@ContextConfiguration(classes = {TestConfiguration.class, ApiTestController.class, TestController.class})
@ExtendWith(SpringExtension.class)
class RequestMappingHandlerMappingIntTest {

    @Autowired
    private ApiRequestMappingHandlerMapping mapping;

    @Test
    void shouldManageApiHandlerMappings() {
        assertThat(mapping.getHandlerMethods())
            .hasSize(2)
            .hasEntrySatisfying(expectedPathInController(TestController.class, "/api/v2/mytest/aaa"))
            .hasEntrySatisfying(expectedPathInController(ApiTestController.class, "/api/v1/apiTest"));
    }

    private static Condition<Map.Entry<RequestMappingInfo, HandlerMethod>> expectedPathInController(Class controllerClass, String path) {
        Predicate<Map.Entry<RequestMappingInfo, HandlerMethod>> predicate = entry -> verify(entry.getKey(), entry.getValue(), controllerClass, path);
        return new Condition<>(predicate, "expected controller and path does not exist");
    }

    private static boolean verify(RequestMappingInfo info, HandlerMethod method, Class controllerClass, String path) {
        var beanType = method.getBeanType();
        var actualPath = info.getPatternsCondition().getPatterns().stream().findFirst().get();
        return beanType == controllerClass && path.equals(actualPath);
    }
}
