package componentTest.a.trading.microservice.base.plugin

import a.trading.microservice.base.MainApplication
import a.trading.microservice.base.plugin.DirectoryClassLoader
import a.trading.microservice.base.plugin.DirectoryClassLoaderConfig
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [MainApplication::class])
class DirectoryClassLoaderComponentTest(
    @Autowired val directoryClassLoader: DirectoryClassLoader,
) {
    @Autowired
    lateinit var classLoaderConfig: DirectoryClassLoaderConfig

    @Test
    fun `directoryClassLoader should be autowired`() {
        assertNotNull(directoryClassLoader, "DirectoryClassLoader must be autowired by Spring")
        assertNotNull(directoryClassLoader.classLoaderConfig,
                      "classLoaderConfig must be autowired into DirectoryClassLoader")
    }

    @Test
    fun `classLoaderConfig should have a default value`() {
        assertNotNull(classLoaderConfig)
        assert(classLoaderConfig.pluginDirectory == "src/test/resources",
               { "${classLoaderConfig.pluginDirectory} is wrong" })
    }
}