package pl.filipowm.api;

import org.springframework.web.bind.annotation.GetMapping;
import pl.filipowm.api.annotations.Api;
import pl.filipowm.api.annotations.ApiVersion;

@Api("mytest")
@ApiVersion(2)
class TestController {

    @GetMapping("aaa")
    String doAction() {
        return "test";
    }
}
