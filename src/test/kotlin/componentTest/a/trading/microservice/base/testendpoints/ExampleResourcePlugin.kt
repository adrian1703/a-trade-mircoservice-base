package componentTest.a.trading.microservice.base.testendpoints

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import unitTest.a.trading.microservice.base.plugin.RestApiPluginTest
import unitTest.a.trading.microservice.base.utils.DummyRuntimeApi

@Configuration
class ExampleResourcePlugin(private val api: DummyRuntimeApi = DummyRuntimeApi()) {

    @Bean
    fun servePlugin(): RouterFunction<ServerResponse> {
        val restApiPluginTest = RestApiPluginTest()
        val plugin = restApiPluginTest.getPlugin()
        plugin.init(api)
        return plugin.getRouter()
    }
}