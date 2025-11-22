package componentTest.a.trading.microservice.base.runtime_api

import a.trade.microservice.runtime_api.ExecutorContext
import a.trading.microservice.base.MainApplication
import a.trading.microservice.base.concurrent.DefaultExecutorService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.ExecutorService

@SpringBootTest(classes = [MainApplication::class])
class ProdRuntimeApiTest(
    @Autowired val executorService: ExecutorService,
    @Autowired val executorServiceImpl: DefaultExecutorService,
) {

    @Test
    fun defaultExecutorIsConfigured() {
        val actual = executorServiceImpl.getExecutorService(ExecutorContext.DEFAULT)
        assert(
            executorService == actual
        ) {
            """
        Default ExecutorService is not configured as expected!
        Expected: $executorService (hash: ${executorService.hashCode()})
        Actual: $actual (hash: ${actual.hashCode()})
        """.trimIndent()
        }
    }

}