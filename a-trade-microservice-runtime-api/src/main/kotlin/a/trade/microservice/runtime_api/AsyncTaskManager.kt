package a.trade.microservice.runtime_api

import net.jcip.annotations.ThreadSafe
import org.springframework.context.Lifecycle
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future
import kotlin.coroutines.cancellation.CancellationException

/**
 * Manages the execution of an asynchronous task in a thread-safe manner, allowing a task to be started, stopped,
 * and queried for its running status. The [start] method returns control to the caller immediately while starting
 * background execution.
 *
 * This class is intended to be used as a delegate for asynchronously executing a task and managing its lifecycle.
 *
 * @constructor Creates a new instance of AsyncTaskManager.
 * @param runtimeApi The runtime API used for accessing the executor service.
 *
 * Implements:
 * - [Lifecycle] to provide standardized start/stop lifecycle management.
 */
@ThreadSafe
class AsyncTaskManager(private val runtimeApi: RuntimeApi) : Lifecycle {

    companion object {
        private val openTasks = ConcurrentHashMap<String, Future<*>>()
        private val finishedTasks = ConcurrentHashMap<String, Any>()
        private val failedTasks = ConcurrentHashMap<String, Throwable>()

        fun getOpenTasks(): Map<String, Future<*>> = openTasks
        fun getFinishedTasks(): Map<String, Any> = finishedTasks
        fun getFailedTasks(): Map<String, Throwable> = failedTasks

        private val updateLoopInterval = 100L
        private val updateLoop = Thread {
            openTasks.forEach { (taskId, task) ->
                if (task.isDone) {
                    try {
                        finishedTasks[taskId] = task.get()
                    } catch (e: Exception) {
                        failedTasks[taskId] = e
                    }
                    openTasks.remove(taskId)
                }
                Thread.sleep(updateLoopInterval)
            }
        }

        init {
            updateLoop.start()
        }

        fun removeTask(taskId: String) {
            openTasks.remove(taskId)
            finishedTasks.remove(taskId)
            failedTasks.remove(taskId)
        }
    }

    private var isRunning = false
    var taskFuture: Future<*>? = null
        @Synchronized get
    var task: Callable<*>? = null
        @Synchronized set

    @Synchronized
    override fun start() {
        if (isRunning) {
            return
        }
        if (task == null) {
            throw kotlin.Exception("Task is null.")
        }
        taskFuture = runtimeApi.getExecutorService(ExecutorContext.DEFAULT).submit(task)
    }

    @Synchronized
    override fun stop() {
        taskFuture?.cancel(true)
        try {
            taskFuture?.get()
        } catch (ignored: CancellationException) { // this is ok
        }
        isRunning = false
    }

    @Synchronized
    override fun isRunning(): Boolean {
        if (taskFuture != null) {
            if (taskFuture?.isDone == true || taskFuture?.isCancelled == true) {
                isRunning = false
            }
        }
        return isRunning
    }
}
