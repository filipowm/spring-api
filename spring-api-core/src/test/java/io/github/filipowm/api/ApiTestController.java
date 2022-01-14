package io.github.filipowm.api;

import org.springframework.web.bind.annotation.GetMapping;
import io.github.filipowm.api.annotations.Api;

@Api
class ApiTestController {

    @GetMapping
    String get() {
        return "test";
    }
}
