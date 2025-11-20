package a.trading.microservice.base.plugin

import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile

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
        logger.info("Scanning directory '{}' for JAR files...", directoryPath)
        directory
            .listFiles()
            ?.filter { file -> file.extension == "jar" }
            ?.forEach { file ->
                logger.info("Loading plugin from -{}", file.absolutePath)
                try {
                    // For diagnostics, show all META-INF/services/ entries in the JAR
                    JarFile(file).use { jar ->
                        val servicesDir = "META-INF/services/"
                        val services = jar.entries().toList()
                            .filter { it.name.startsWith(servicesDir) && !it.isDirectory }
                        if (services.isNotEmpty()) {
                            logger.info("Found {} META-INF/services entries in {}:", services.size, file.name)
                            services.forEach { serviceEntry ->
                                logger.info(" - {}", serviceEntry.name)
                                val content = jar.getInputStream(serviceEntry)
                                    .bufferedReader().readLines()
                                content.forEach { line ->
                                    logger.info("    Content: '{}'", line)
                                }
                            }
                        } else {
                            logger.warn("No META-INF/services entries found in {}", file.name)
                        }
                    }
                } catch (e: Exception) {
                    logger.error("Error reading services in {}: {}", file.name, e.message, e)
                }

                val classLoader = URLClassLoader.newInstance(arrayOf(file.toURI().toURL()), this.javaClass.classLoader)
                result.add(classLoader)
                logger.info("Created classloader: {} with URLs: {}", classLoader, classLoader.urLs.joinToString(", "))
            }

        return result
    }


}