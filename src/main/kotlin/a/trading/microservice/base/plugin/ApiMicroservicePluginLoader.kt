package a.trading.microservice.base.plugin

import jakarta.inject.Singleton
import net.jcip.annotations.NotThreadSafe

@Singleton
@NotThreadSafe
class ApiMicroservicePluginLoader : PluginLoader {

    val classLoader by lazy { LocalClassLoader() }

    override fun loadPlugins() {
        TODO("Not yet implemented")
    }


}