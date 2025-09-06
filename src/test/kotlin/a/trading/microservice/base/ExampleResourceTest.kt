package a.trading.microservice.base

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class ExampleResourceTest {

    @Test
    fun testHelloEndpoint() {
        given()
            .`when`().get("/hello")
            .then()
            .statusCode(200)
            .body(`is`("Hello from Quarkus REST"))
    }

    @Test
    fun testHelloEndpoint2() {
        given()
            .`when`().get("/hello2")
            .then()
            .statusCode(200)
            .body(`is`("Hello from Quarkus REST"))
    }

}