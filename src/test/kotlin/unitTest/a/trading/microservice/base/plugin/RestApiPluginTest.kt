package unitTest.a.trading.microservice.base.plugin

import a.trade.microservice.runtime_api.RestApiPlugin
import org.junit.jupiter.api.Test
import java.util.*

class RestApiPluginTest {

    val cf = _root_ide_package_.unitTest.a.trading.microservice.base.utils.LocalTestConfig()

    @Test
    fun `test rest hookin of test endpoint`() {
        //given
        val directoryClassLoader = _root_ide_package_.a.trading.microservice.base.plugin.DirectoryClassLoader()
        val classLoaders = directoryClassLoader.createClassLoaderForEachJarInDirectory(cf.pathToJarDirectory)
        assert(classLoaders.size == 1) { "There has should be exactly 1 Jar in dir" }
        val plugin = ServiceLoader.load(RestApiPlugin::class.java,
                                        classLoaders.first())
        assert(plugin.count() == 1) { "There should be exactly 1 RestApiPlugin in Jar" }
    }

    fun getPlugin(): RestApiPlugin {
        val directoryClassLoader = _root_ide_package_.a.trading.microservice.base.plugin.DirectoryClassLoader()
        val classLoaders = directoryClassLoader.createClassLoaderForEachJarInDirectory(cf.pathToJarDirectory)
        val plugin = ServiceLoader.load(RestApiPlugin::class.java,
                                        classLoaders.first())
        return plugin.first()
    }
}