package io.github.filipowm.api.servlet;

import io.github.filipowm.api.annotations.Api;
import io.github.filipowm.api.annotations.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;

@Api("tests")
@ApiVersion(2)
class TestController {

    @GetMapping("first")
    String test1() {
        return "test1";
    }

    @GetMapping("second")
    @ApiVersion(3)
    String test2() {
        return "test2";
    }

}
