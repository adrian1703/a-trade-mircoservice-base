package testendpoints

import a.trading.microservice.base.plugin.RestApiPluginTest
import a.trading.microservice.base.utils.DummyRuntimeApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ExampleResourcePlugin {

    @Bean
    fun servePlugin(): RouterFunction<ServerResponse> {
        val restApiPluginTest = RestApiPluginTest()
        val plugin = restApiPluginTest.getPlugin()
        return plugin.getRouter(DummyRuntimeApi())
    }
}