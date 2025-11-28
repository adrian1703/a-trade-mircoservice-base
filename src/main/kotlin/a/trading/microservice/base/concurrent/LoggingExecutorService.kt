package a.trading.microservice.base.concurrent

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class LoggingExecutorService(
    private val delegate: ExecutorService,
) : ExecutorService by delegate {

    override fun <T> submit(task: Callable<T>): Future<T> = delegate.submit(LoggedCallable(task))

    override fun submit(task: Runnable): Future<*> = delegate.submit(LoggedRunnable(task))

    override fun close() {
        delegate.close()
    }

    override fun <T> submit(task: Runnable, result: T): Future<T> = delegate.submit(LoggedRunnable(task), result)

    override fun execute(command: Runnable) = delegate.execute(LoggedRunnable(command))
}