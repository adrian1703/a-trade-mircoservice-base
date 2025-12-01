package a.trading.microservice.base.kafka

import a.trade.microservice.runtime_api.KafkaConfigs
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.stereotype.Component

@Component
class KafkaClientConfigsImpl : KafkaConfigs {

    private val kafkaUrl = "kafka1:9092"
    private val schemaRegistryUrl = "http://schema-registry:8081"

    fun createStringProducer(config: MutableMap<String, Any>): KafkaProducer<String, String> {
        return KafkaProducer<String, String>(config)
    }

    override fun getStringProducerConfig(): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        result[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        result[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        result[ProducerConfig.LINGER_MS_CONFIG] = 50
        return result
    }

    override fun getStringConsumerConfig(groupId: String): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        result[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        result[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        result[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        result[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "true"
        result[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        return result
    }

    override fun getAvroProducerConfig(): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        result[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        result[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java
        result[ProducerConfig.LINGER_MS_CONFIG] = "50"
        result[KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistryUrl
        return result
    }

    override fun getAvroConsumerConfig(groupId: String): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        result[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        result[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        result[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaAvroDeserializer::class.java
        result[KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistryUrl
        result[KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG] = true
        result[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "true"
        result[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        return result
    }

    override fun getAdminClientConfig(): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        return result
    }
}
