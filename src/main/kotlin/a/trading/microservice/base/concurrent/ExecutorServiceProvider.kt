package a.trading.microservice.base.concurrent

import a.trade.microservice.runtime_api.ExecutorContext
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Component
import java.util.concurrent.*

@Component
class ExecutorServiceProvider {
    val logger = getLogger(ExecutorServiceProvider::class.java)
    val executorServices: MutableMap<ExecutorContext, ExecutorService> = ConcurrentHashMap()

    init {
        executorServices[ExecutorContext.COMPUTE] = getComputeExecutor()
        executorServices[ExecutorContext.IO] = getIOExecutor()
        executorServices[ExecutorContext.DEFAULT] = getDefaultExecutor()
    }

    fun getExecutorService(context: ExecutorContext): ExecutorService {
        val result = executorServices[context]
        if (result == null) {
            throw IllegalArgumentException("Executor context not found:${context}")
        }
        return result
    }

    private fun getDefaultExecutor(): ExecutorService {
        return newExecutor(ExecutorContext.DEFAULT,
                           1,
                           Runtime.getRuntime().availableProcessors(),
                           10,
                           LinkedBlockingQueue(100))
    }

    private fun getComputeExecutor(): ExecutorService {
        return newExecutor(ExecutorContext.COMPUTE,
                           3,
                           Runtime.getRuntime().availableProcessors(),
                           60,
                           LinkedBlockingQueue())
    }

    private fun getIOExecutor(): ExecutorService {
        return Executors.newVirtualThreadPerTaskExecutor()
    }

    fun newExecutor(
        context: ExecutorContext,
        minThreadCount: Int,
        maxThreadCount: Int,
        keepAlive: Long,
        workQueue: BlockingQueue<Runnable>,
    ): ThreadPoolExecutor {
        val namedThreadFactory = createThreadFactoryFor(context)
        val result = ThreadPoolExecutor(minThreadCount,
                                        maxThreadCount,
                                        keepAlive,
                                        TimeUnit.SECONDS,
                                        workQueue,
                                        namedThreadFactory,
                                        ThreadPoolExecutor.AbortPolicy()
        )
        val threadsStarted = result.prestartAllCoreThreads()
        logger.info("{}: Prestarted {} threads", context, threadsStarted)
        return result
    }

    private fun createThreadFactoryFor(context: ExecutorContext): ThreadFactory {
        return ThreadFactory { runnable ->
            val thread = Thread(runnable)
            thread.name = "${context}-Thread-${thread.threadId()}"
            thread.isDaemon = false // or true, as needed
            return@ThreadFactory thread
        }
    }
}

