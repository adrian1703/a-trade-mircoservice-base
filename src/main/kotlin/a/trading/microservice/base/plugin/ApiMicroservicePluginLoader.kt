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

/**
 * Loader for API microservice plugins implementing [RestApiPlugin].
 * Only to be used on startup!
 * <p>
 * This class scans a predefined directory for plugin JAR files, dynamically loads them
 * using [DirectoryClassLoader], and integrates their routing logic into the application.
 * </p>
 *
 * <h2>Thread Safety</h2>
 * This class is <strong>not thread-safe</strong>. Ensure usage in a controlled, single-threaded environment.
 *
 * @constructor Creates an instance of the plugin loader.
 *
 * @see [RestApiPlugin]
 * @see [DirectoryClassLoader]
 */
@NotThreadSafe
@Configuration
class ApiMicroservicePluginLoader : InterfaceLoader<RestApiPlugin> {

    @Autowired
    lateinit var classLoader: DirectoryClassLoader

    @Autowired
    lateinit var runtimeApi: RuntimeApi

    override fun loadImplementations(): List<RestApiPlugin> {
        val plugins = mutableListOf<RestApiPlugin>()
        val classloaders = classLoader.createClassLoaderForEachJarInDirectory()
        classloaders.forEach { loader ->
            val loadedPlugins = ServiceLoader.load(RestApiPlugin::class.java, loader)
            plugins.addAll(loadedPlugins)
        }
        return plugins.toList()
    }

    fun getRouter(): RouterFunction<ServerResponse> {
        val plugins = loadImplementations()
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