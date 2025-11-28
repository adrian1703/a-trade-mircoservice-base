package a.trading.microservice.base.concurrent

class LoggedRunnable(val runnable: Runnable) : Runnable {
    private val logger = org.slf4j.LoggerFactory.getLogger(runnable::class.java)
    override fun run() {
        try {
            runnable.run()
        } catch (e: Exception) {
            logger.error("Error in runnable", e)
            throw e
        }
    }
}