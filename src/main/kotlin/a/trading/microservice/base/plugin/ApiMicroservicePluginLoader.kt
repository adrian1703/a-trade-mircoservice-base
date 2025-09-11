package a.trading.microservice.base.plugin

import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class ApiMicroservicePluginLoader : PluginLoader {

    val classLoader by lazy { LocalClassLoader() }

    override fun loadPlugins() {
        TODO("Not yet implemented")
    }


}