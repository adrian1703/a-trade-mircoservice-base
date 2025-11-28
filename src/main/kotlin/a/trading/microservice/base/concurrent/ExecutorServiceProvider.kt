package a.trading.microservice.base.concurrent

import a.trade.microservice.runtime_api.ExecutorContext
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Component
import java.util.concurrent.*

@Component
class ExecutorServiceProvider {
    val logger = getLogger(this::class.java)
    val executorServices: MutableMap<ExecutorContext, ExecutorService> = ConcurrentHashMap()

    init {
        // @formatter:off
        executorServices[ExecutorContext.COMPUTE]   = LoggingExecutorService(getComputeExecutor())
        executorServices[ExecutorContext.IO]        = LoggingExecutorService(getIOExecutor())
        executorServices[ExecutorContext.DEFAULT]   = LoggingExecutorService(getUnboundedExecutor()) //LoggingExecutorService(getDefaultExecutor())
        executorServices[ExecutorContext.UNBOUNDED] = LoggingExecutorService(getUnboundedExecutor())
        // @formatter:on
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
                           10,
                           10,
                           LinkedBlockingQueue(100),
                           ThreadPoolExecutor.AbortPolicy())
    }

    private fun getComputeExecutor(): ExecutorService {
        return newExecutor(ExecutorContext.COMPUTE,
                           Runtime.getRuntime().availableProcessors(),
                           Runtime.getRuntime().availableProcessors(),
                           20,
                           LinkedBlockingQueue(),
                           ThreadPoolExecutor.CallerRunsPolicy())
    }

    private fun getUnboundedExecutor(): ExecutorService {
        return newExecutor(ExecutorContext.UNBOUNDED,
                           1,
                           Int.MAX_VALUE,
                           5,
                           SynchronousQueue(),
                           ThreadPoolExecutor.AbortPolicy())
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
        policy: RejectedExecutionHandler,
    ): ThreadPoolExecutor {
        val namedThreadFactory = createThreadFactoryFor(context)
        val result = ThreadPoolExecutor(minThreadCount,
                                        maxThreadCount,
                                        keepAlive,
                                        TimeUnit.SECONDS,
                                        workQueue,
                                        namedThreadFactory,
                                        policy)
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

