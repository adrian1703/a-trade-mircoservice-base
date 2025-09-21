package componentTest.a.trading.microservice.base.testendpoints

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestEndpointsApplication

fun main(args: Array<String>) {
    runApplication<TestEndpointsApplication>(*args)
}
