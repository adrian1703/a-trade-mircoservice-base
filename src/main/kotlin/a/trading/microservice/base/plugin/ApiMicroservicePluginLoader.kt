package a.trading.microservice.base.plugin

import a.trade.microservice.runtime_api.RestApiPlugin
import net.jcip.annotations.NotThreadSafe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@NotThreadSafe
@Component
class ApiMicroservicePluginLoader : PluginLoader {

    @Autowired
    lateinit var classLoader: DirectoryClassLoader

    override fun loadPlugins(): MutableList<RestApiPlugin> {
        val plugins = mutableListOf<RestApiPlugin>()
        val classloaders = classLoader.createClassLoaderForEachJarInDirectory()
        classloaders.forEach { loader ->
            val loadedPlugins = ServiceLoader.load(RestApiPlugin::class.java,
                                                   loader)
            plugins.addAll(loadedPlugins)
        }
        return plugins
    }


}