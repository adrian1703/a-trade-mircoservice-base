package componentTest.a.trading.microservice.base.testendpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello2")
public class ExampleResourceJava {

    @GetMapping(produces = "text/plain")
    public String hello2() {
        return "hello";
    }
}
