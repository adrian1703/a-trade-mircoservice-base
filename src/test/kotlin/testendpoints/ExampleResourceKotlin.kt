package testendpoints

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class ExampleResourceKotlin {
    @GetMapping(produces = ["text/plain"])
    fun hello(): String {
        return "hello"
    }
}

