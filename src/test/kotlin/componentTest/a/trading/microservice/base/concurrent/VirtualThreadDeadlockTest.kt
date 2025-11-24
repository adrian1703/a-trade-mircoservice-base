package componentTest.a.trading.microservice.base.concurrent

import a.trade.microservice.runtime_api.AsyncTaskManager
import a.trade.microservice.runtime_api.ExecutorContext
import a.trading.microservice.base.MainApplication
import a.trading.microservice.base.runtime_api.ProdRuntimeApi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService

@SpringBootTest(classes = [MainApplication::class])
class VirtualThreadDeadlockTest(
    @Autowired private val runtimeApi: ProdRuntimeApi,
) {

    @Test
    fun `deadlock when V1 submits to V2 and blocks`() {
        val v1: ExecutorService = runtimeApi.getExecutorService(ExecutorContext.DEFAULT)
        val v2: ExecutorService = runtimeApi.getExecutorService(ExecutorContext.IO)

        val a = AsyncTaskManager(runtimeApi)
        a.task = Callable {
            v2.submit(Callable {
                return@Callable "test"
            }).get()
        }
        a.start()
        while (a.taskFuture!!.isDone.not()) {
            println(1)
            Thread.sleep(100)
        }

    }

}