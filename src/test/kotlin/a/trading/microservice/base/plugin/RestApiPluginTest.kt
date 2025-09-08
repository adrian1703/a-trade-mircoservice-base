package a.trading.microservice.base.plugin

import a.trade.microservice.runtime_api.RestApiPlugin
import a.trading.microservice.base.LocalTestConfig
import jakarta.enterprise.inject.spi.CDI
import org.jboss.resteasy.reactive.server.core.startup.RuntimeResourceDeployment
import org.junit.jupiter.api.Test
import java.util.*

class RestApiPluginTest {

    val cf = LocalTestConfig()

    @Test
    fun `test rest hookin of test endpoint`() {
        //given
        val localClassLoader = LocalClassLoader()
        val classLoaders = localClassLoader.createClassLoaderForEachJarInDirectory(cf.pathToJarDirectory)
        assert(classLoaders.size == 1) { "There has should be exactly 1 Jar in dir" }
        val plugin = ServiceLoader.load(RestApiPlugin::class.java,
                                        classLoaders.first())
        assert(plugin.count() == 1) { "There should be exactly 1 RestApiPlugin in Jar" }
        //when
        val restResources = plugin.first().getRestResources()

        val deployment = CDI.current().select(RuntimeResourceDeployment::class.java).get()
    }
}