package a.trading.microservice.base.plugin

import a.trade.microservice.runtime_api.RestApiPlugin
import a.trade.microservice.runtime_api.RuntimeApi
import net.jcip.annotations.NotThreadSafe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.util.*

@NotThreadSafe
@Configuration
class ApiMicroservicePluginLoader : PluginLoader {

    @Autowired
    lateinit var classLoader: DirectoryClassLoader

    @Autowired
    lateinit var runtimeApi: RuntimeApi

    override fun loadPlugins(): List<RestApiPlugin> {
        val plugins = mutableListOf<RestApiPlugin>()
        val classloaders = classLoader.createClassLoaderForEachJarInDirectory()
        classloaders.forEach { loader ->
            val loadedPlugins = ServiceLoader.load(RestApiPlugin::class.java, loader)
            plugins.addAll(loadedPlugins)
        }
        return plugins.toList()
    }

    fun getRouter(): RouterFunction<ServerResponse> {
        val plugins = loadPlugins()
        val routers = plugins.map { it.getRouter(runtimeApi) }
        val combinedRouter =
            routers
                .reduce { acc: RouterFunction<ServerResponse>, router: RouterFunction<ServerResponse> ->
                    acc.and(router)
                } ?: router { }
        return combinedRouter
    }

    @Bean
    fun serveFunction() = getRouter()
}