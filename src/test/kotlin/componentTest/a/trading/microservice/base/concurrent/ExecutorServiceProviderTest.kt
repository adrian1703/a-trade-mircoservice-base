import a.trade.microservice.runtime_api.ExecutorContext
import a.trading.microservice.base.MainApplication
import a.trading.microservice.base.concurrent.ExecutorServiceProvider
import a.trading.microservice.base.runtime_api.ProdRuntimeApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@SpringBootTest(classes = [MainApplication::class])
class ExecutorServiceProviderTest {

    @Autowired
    lateinit var runtimeApi: ProdRuntimeApi

    @Test
    fun `compute`() {
        val executor = runtimeApi.getExecutorService(ExecutorContext.COMPUTE)
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get(2, TimeUnit.SECONDS)
        assertEquals("test", result, "Single-thread executor did not execute task correctly")
        executor.shutdown()
    }

    @Test
    fun `single thread executor runs task successfully`() {
        val executor = Executors.newSingleThreadExecutor()
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get(2, TimeUnit.SECONDS)
        assertEquals("test", result, "Single-thread executor did not execute task correctly")
        executor.shutdown()
    }

    @Test
    fun `virtual runs task successfully`() {
        val executor = Executors.newVirtualThreadPerTaskExecutor()
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get(2, TimeUnit.SECONDS)
        assertEquals("test", result, "Single-thread executor did not execute task correctly")
        executor.shutdown()
    }

    @Test
    fun `virtual2 runs task successfully`() {
        val executor = ExecutorServiceProvider().getExecutorService(ExecutorContext.IO)
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get(2, TimeUnit.SECONDS)
        assertEquals("test", result, "Single-thread executor did not execute task correctly")
        executor.shutdown()
    }

    @Test
    fun `virtual3 runs task successfully`() {
        val executor = ExecutorServiceProvider().getExecutorService(ExecutorContext.COMPUTE)
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get(2, TimeUnit.SECONDS)
        assertEquals("test", result, "Single-thread executor did not execute task correctly")
        executor.shutdown()
    }

    @Test
    fun `fixed thread pool executor runs task successfully`() {
        val executor = Executors.newFixedThreadPool(2)
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get()
        assertEquals("test", result, "Fixed-thread-pool executor did not execute task correctly")
        executor.shutdown()
    }

    @Test
    fun `cached thread pool executor runs task successfully`() {
        val executor = Executors.newCachedThreadPool()
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get(2, TimeUnit.SECONDS)
        assertEquals("test", result, "Cached-thread-pool executor did not execute task correctly")
        executor.shutdown()
    }

    @Test
    fun `scheduled thread pool executor schedules and runs task successfully`() {
        val executor = Executors.newScheduledThreadPool(1) as ScheduledExecutorService
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get(2, TimeUnit.SECONDS)
        assertEquals("test",
                     result,
                     "Scheduled-thread-pool executor did not execute task correctly")
        executor.shutdown()
    }

    @Test
    fun `virtual thread per task executor runs task successfully`() {
        val executor = Executors.newVirtualThreadPerTaskExecutor()
        val future = executor.submit(Callable { return@Callable "test" })
        val result = future.get(2, TimeUnit.SECONDS)
        assertEquals("test", result, "Virtual-thread-per-task executor did not execute task correctly")
        executor.shutdown()
    }
}