package a.trading.microservice.base.plugin

import a.trade.microservice.runtime_api.test.TestInterface
import a.trading.microservice.base.LocalTestConfig
import org.junit.jupiter.api.Test
import java.util.*

class LocalClassLoaderTest {

    val cf = LocalTestConfig()

    @Test
    fun `test loading of local jar test interface`() {
        // given
        val localClassLoader = LocalClassLoader()
        // when
        val classLoaders = localClassLoader.createClassLoaderForEachJarInDirectory(cf.pathToJarDirectory)
        // then
        assert(classLoaders.size == 1) { "There has should be exactly 1 Jar in dir" }
        // when
        val testInterfaces = ServiceLoader.load(TestInterface::class.java,
                                                classLoaders.first())
        assert(testInterfaces.count() == 1) { "There should be exactly 1 TestInterface in Jar" }
        assert(testInterfaces.first()
                   .test() == "Test") { "The test function should return the expected string 'Test'." }
    }
}