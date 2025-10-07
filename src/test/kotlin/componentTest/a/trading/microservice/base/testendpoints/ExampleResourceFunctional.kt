package componentTest.a.trading.microservice.base.testendpoints

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
class ExampleResourceFunctional {

    fun hello3(request: ServerRequest): Mono<ServerResponse> {
        val result = ServerResponse
            .ok()
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValue("hello")
        return result
    }

    fun getRouter(): RouterFunction<ServerResponse> {
        val route = router {
            GET("/hello3", accept(APPLICATION_JSON), ::hello3)
        }
        return route
    }

    @Bean
    fun serveFunction() = getRouter()
}
