package a.trading.microservice.base.plugin

interface InterfaceLoader<T> {
    fun loadImplementations(): List<T>
}