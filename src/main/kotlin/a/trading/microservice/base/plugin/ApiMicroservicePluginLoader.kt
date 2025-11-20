package a.trading.microservice.base.plugin

import a.trade.microservice.runtime_api.RestApiPlugin
import a.trade.microservice.runtime_api.RuntimeApi
import net.jcip.annotations.NotThreadSafe
import org.slf4j.LoggerFactory.getLogger
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
class ApiMicroservicePluginLoader(val classLoader: DirectoryClassLoader, val runtimeApi: RuntimeApi) :
    InterfaceLoader<RestApiPlugin> {

    val logger = getLogger(this::class.java)

    override fun loadImplementations(): List<RestApiPlugin> {
        val plugins = mutableListOf<RestApiPlugin>()
        val classloaders = classLoader.createClassLoaderForEachJarInDirectory()
        logger.info("Loaded ${classloaders.size} classloaders")
        classloaders.forEach { loader ->
            val loadedPlugins = ServiceLoader.load(RestApiPlugin::class.java, loader)
            plugins.addAll(loadedPlugins)
        }
        return plugins.toList()
    }

    fun getRouter(): RouterFunction<ServerResponse> {
        val plugins = loadImplementations()
        logger.info("Loaded ${plugins.size} plugins")
        val routers = plugins.map { it.init(runtimeApi); it.getRouter() }
        if (routers.isEmpty()) return router { GET("/ping") { ServerResponse.ok().build() } }
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