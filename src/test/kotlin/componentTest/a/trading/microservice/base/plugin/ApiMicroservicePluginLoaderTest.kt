package componentTest.a.trading.microservice.base.plugin

import a.trading.microservice.base.Application
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = [Application::class])

class ApiMicroservicePluginLoaderTest(
    @Autowired val webTestClient: WebTestClient
) {

    @Test
    fun testHelloEndpoint() {
        val testComponent = TestComponent(23, "hi")
        webTestClient.post().uri("/v1/test")
            .bodyValue(testComponent)
            .exchange()
            .expectStatus().isOk
            .expectBody(TestComponent::class.java)
            .isEqualTo(testComponent)
    }

    @Test
    fun testHelloEndpoint3() {
        webTestClient.get().uri("/hello4")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("hello")
    }
}