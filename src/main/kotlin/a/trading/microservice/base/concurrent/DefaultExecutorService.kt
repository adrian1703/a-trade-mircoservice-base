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
    }

    fun getExecutorService(context: ExecutorContext): ExecutorService {
        val result = executorServices[ExecutorContext.COMPUTE]
        if (result == null) {
            throw IllegalArgumentException("Executor context not found:${context}")
        }
        return result
    }

    private fun getComputeExecutor(): ThreadPoolExecutor {
        val namedThreadFactory = createThreadFactoryFor(ExecutorContext.COMPUTE)
        return ThreadPoolExecutor(1, 16, 60, TimeUnit.SECONDS, LinkedBlockingQueue(), namedThreadFactory)
    }

    private fun getIOExecutor(): ThreadPoolExecutor {
        val namedThreadFactory = createThreadFactoryFor(ExecutorContext.IO)
        return ThreadPoolExecutor(0, 100, 20, TimeUnit.SECONDS, LinkedBlockingQueue(), namedThreadFactory)
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

