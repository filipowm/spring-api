package io.github.filipowm.api;

import org.springframework.web.bind.annotation.GetMapping;
import io.github.filipowm.api.annotations.Api;
import io.github.filipowm.api.annotations.ApiVersion;

@Api("mytest")
@ApiVersion(2)
class TestController {

    @GetMapping("aaa")
    String doAction() {
        return "test";
    }
}
