package a.trade.microservice.runtime_api.events.observer

open class EventSource<T> {
    private val observers = mutableSetOf<Observer<T>>()

    fun addObserver(observer: Observer<T>) {
        observers += observer
    }

    protected fun notifyObservers(event: T) {
        observers.forEach { it.onUpdate(event) }
    }
}
