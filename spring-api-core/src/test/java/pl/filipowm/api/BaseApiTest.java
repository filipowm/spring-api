package pl.filipowm.api;

import org.mockito.Mockito;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import pl.filipowm.api.annotations.Api;

import java.lang.reflect.Method;
import java.util.List;

class BaseApiTest {

    static final String BASE_PATH = "/service/resource";

    PatternsRequestCondition createPatternConditions(List<String> patterns) {
        return new PatternsRequestCondition(patterns.toArray(new String[patterns.size()]));
    }

    RequestMappingInfo createRequestMappingInfo(ConsumesRequestCondition consumes,
                                                ProducesRequestCondition produces,
                                                PatternsRequestCondition patterns,
                                                RequestMethodsRequestCondition methods,
                                                ParamsRequestCondition params,
                                                HeadersRequestCondition headers) {
        return new RequestMappingInfo(patterns, methods, params, headers, consumes, produces, null);
    }

    RequestMappingInfo createRequestMappingInfo(ConsumesRequestCondition consumes,
                                                ProducesRequestCondition produces,
                                                PatternsRequestCondition patterns) {
        return createRequestMappingInfo(consumes, produces, patterns, new RequestMethodsRequestCondition(), new ParamsRequestCondition(), new HeadersRequestCondition());
    }

    RequestMappingInfo createRequestMappingInfo(ConsumesRequestCondition consumes,
                                                ProducesRequestCondition produces) {
        return createRequestMappingInfo(consumes, produces, new PatternsRequestCondition(), new RequestMethodsRequestCondition(), new ParamsRequestCondition(), new HeadersRequestCondition());
    }

    RequestMappingInfo createRequestMappingInfo() {
        return new RequestMappingInfo(new PatternsRequestCondition(),
                                      new RequestMethodsRequestCondition(),
                                      new ParamsRequestCondition(),
                                      new HeadersRequestCondition(),
                                      new ConsumesRequestCondition(),
                                      new ProducesRequestCondition(),
                                      null);
    }

    RequestMappingInfo createRequestMappingInfo(PatternsRequestCondition patterns) {
        return createRequestMappingInfo(new ConsumesRequestCondition(), new ProducesRequestCondition(), patterns);
    }

    RequestMappingInfo createRequestMappingInfo(String consumes, String produces, String path) {
        return createRequestMappingInfo(new ConsumesRequestCondition(consumes), new ProducesRequestCondition(produces), new PatternsRequestCondition(path));
    }

    RequestMappingInfo createRequestMappingInfo(String consumes, String produces) {
        return createRequestMappingInfo(consumes, produces, BASE_PATH);
    }

    ApiBuilder createBuilder(RequestMappingInfo info, Api api, Method method, Class handlerType) {
        return new ApiBuilder(api, method, handlerType, info, ApiTestHelper.PATH_PREFIX, ApiTestHelper.VERSION_PREFIX, ApiTestHelper.CONTENT_TYPE_VND);
    }

    ApiBuilder createBuilder() {
        return createBuilder(createRequestMappingInfo(), Mockito.mock(Api.class));
    }

    ApiBuilder createBuilder(RequestMappingInfo info) {
        return createBuilder(info, Mockito.mock(Api.class));
    }

    ApiBuilder createBuilder(Api api) {
        return createBuilder(createRequestMappingInfo(), api);
    }

    ApiBuilder createBuilder(RequestMappingInfo info, Api api) {
        return createBuilder(info, api, ApiTestController.class.getDeclaredMethods()[0], ApiTestController.class);
    }

}
