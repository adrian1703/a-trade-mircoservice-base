package componentTest.a.trading.microservice.base.testendpoints

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleResourceTest(
    @Autowired val webTestClient: WebTestClient
) {

    @Test
    fun testHelloEndpoint() {
        webTestClient.get().uri("/hello")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("hello")
    }

    @Test
    fun testHelloEndpoint2() {
        webTestClient.get().uri("/hello2")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("hello")
    }

    @Test
    fun testHelloEndpoint3() {
        webTestClient.get().uri("/hello3")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("hello")
    }


    @Test
    fun testHelloEndpoint4() {
        webTestClient.get().uri("/hello4")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("hello")
    }
}
