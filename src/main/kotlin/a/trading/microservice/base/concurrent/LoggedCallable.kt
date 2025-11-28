package a.trading.microservice.base.concurrent

import java.util.concurrent.Callable

class LoggedCallable<T>(val callable: Callable<T>) : Callable<T> {
    private val logger = org.slf4j.LoggerFactory.getLogger(callable::class.java)
    override fun call(): T {
        try {
            return callable.call()
        } catch (e: Exception) {
            logger.error("Error in callable", e)
            throw e
        } finally {
        }
    }
}