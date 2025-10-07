package a.trading.microservice.base.plugin

import a.trade.microservice.runtime_api.RestApiPlugin

interface PluginLoader {
    fun loadPlugins(): MutableList<RestApiPlugin>
}