package a.trading.microservice.base.concurrent

import a.trade.microservice.runtime_api.ExecutorContext
import org.springframework.stereotype.Component
import java.util.concurrent.*

@Component
class DefaultExecutorService {
    val executorServices: MutableMap<ExecutorContext, ExecutorService> = mutableMapOf()

    init {
        executorServices[ExecutorContext.COMPUTE] = getComputeExecutor()
        executorServices[ExecutorContext.IO] = getIOExecutor()
        executorServices[ExecutorContext.DEFAULT] = getDefaultExecutor()
    }

    private fun getDefaultExecutor(): ExecutorService {
        return Executors.newVirtualThreadPerTaskExecutor()
    }

    fun getExecutorService(context: ExecutorContext): ExecutorService {
        val result = executorServices[ExecutorContext.COMPUTE]
        if (result == null) {
            throw IllegalArgumentException("Executor context not found:${context}")
        }
        return result
    }

    private fun getComputeExecutor(): ExecutorService {
        val namedThreadFactory = createThreadFactoryFor(ExecutorContext.COMPUTE)
        return ThreadPoolExecutor(1,
                                  Runtime.getRuntime().availableProcessors(),
                                  60,
                                  TimeUnit.SECONDS,
                                  LinkedBlockingQueue(),
                                  namedThreadFactory)
    }

    private fun getIOExecutor(): ExecutorService {
        return Executors.newVirtualThreadPerTaskExecutor()
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

