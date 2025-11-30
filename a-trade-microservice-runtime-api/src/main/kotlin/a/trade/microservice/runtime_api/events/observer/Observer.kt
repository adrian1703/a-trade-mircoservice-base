package a.trade.microservice.runtime_api.events.observer

fun interface Observer<T> {
    fun onUpdate(event: T)
}