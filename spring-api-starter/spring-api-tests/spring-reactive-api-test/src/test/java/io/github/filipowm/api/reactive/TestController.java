package io.github.filipowm.api.reactive;

import io.github.filipowm.api.annotations.Api;
import io.github.filipowm.api.annotations.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Api("tests")
@ApiVersion(2)
class TestController {

    @GetMapping("first")
    Mono<String> test1() {
        return Mono.just("test1");
    }

    @GetMapping("second")
    String test2() {
        return "test2";
    }

}
