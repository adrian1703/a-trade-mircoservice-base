package a.trading.microservice.base.plugin

import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.net.URLClassLoader

@Component
class DirectoryClassLoader {
    val logger = getLogger(this::class.java)

    @Autowired
    lateinit var classLoaderConfig: DirectoryClassLoaderConfig

    /**
     * Creates a list of [ClassLoader] instances, each corresponding to a JAR file
     * present in the specified directory.
     *
     * <p>This method scans the directory located at the given path, identifies all JAR files,
     * and creates class loaders for each JAR using [URLClassLoader].</p>
     *
     * @return A list of [ClassLoader] instances, each capable of loading classes from
     *         a specific JAR file in the directory.
     */
    fun createClassLoaderForEachJarInDirectory(): List<ClassLoader> {
        return createClassLoaderForEachJarInDirectory(classLoaderConfig.pluginDirectory)
    }

    /**
     * Creates a list of [ClassLoader] instances, each corresponding to a JAR file
     * present in the specified directory.
     *
     * <p>This method scans the directory located at the given path, identifies all JAR files,
     * and creates class loaders for each JAR using [URLClassLoader].</p>
     *
     * @param directoryPath The path to the directory containing JAR files.
     *                      This parameter must represent a valid directory; otherwise,
     *                      an empty list or exceptions may be produced.
     *
     * @return A list of [ClassLoader] instances, each capable of loading classes from
     *         a specific JAR file in the directory.
     */
    fun createClassLoaderForEachJarInDirectory(directoryPath: String): List<ClassLoader> {
        val result = mutableListOf<ClassLoader>()
        val directory = File(directoryPath)
        directory
            .listFiles()
            ?.filter { file -> file.extension == "jar" }
            ?.forEach { file ->
                logger.info("Loading plugin from ${file.absolutePath}")
                val classLoader = URLClassLoader.newInstance(arrayOf(file.toURI().toURL()),
                                                             this.javaClass.classLoader)
                result.add(classLoader)
            }
        return result
    }
}