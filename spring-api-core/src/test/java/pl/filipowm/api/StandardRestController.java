package pl.filipowm.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
class StandardRestController {

    @GetMapping("aaa")
    String doAction() {
        return "test";
    }
}
