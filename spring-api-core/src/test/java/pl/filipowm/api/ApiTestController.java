package pl.filipowm.api;

import org.springframework.web.bind.annotation.GetMapping;
import pl.filipowm.api.annotations.Api;

@Api
class ApiTestController {

    @GetMapping
    String get() {
        return "test";
    }
}
