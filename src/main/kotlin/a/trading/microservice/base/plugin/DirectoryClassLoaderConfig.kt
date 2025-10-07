package a.trading.microservice.base.plugin

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class DirectoryClassLoaderConfig {
    @Value($$"${plugin.dir}")
    lateinit var pluginDirectory: String
}