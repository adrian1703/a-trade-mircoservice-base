package a.trading.microservice.base.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer

class KafkaClientFactory {

    fun createProducer(): KafkaProducer<String, String> {
        return KafkaProducer<String, String>(getDefaultConfig())
    }

    fun createProducer(config: MutableMap<String, Any>): KafkaProducer<String, String> {
        return KafkaProducer<String, String>(config)
    }

    fun getDefaultConfig(): MutableMap<String, Any> {
        return mutableMapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        )
    }
}