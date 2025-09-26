package a.trading.microservice.base.kafka

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer


class KafkaClientConfigs {

    private val kafkaUrl = "kafka1:9092"
    private val schemaRegistryUrl = "http://schema-registry:8081"

    fun createStringProducer(config: MutableMap<String, Any>): KafkaProducer<String, String> {
        return KafkaProducer<String, String>(config)
    }

    /**
     * Constructs and returns a configuration map for a Kafka producer to produce messages
     * with `String` keys and values.
     *
     * <p>This method configures essential Kafka producer settings such as the bootstrap
     * server URL and the serializers for both keys and values.</p>
     *
     * @return A mutable map containing the configuration for a `String` producer.
     */
    fun getStringProducerConfig(): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        result[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        result[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return result
    }

    /**
     * Constructs and returns a configuration map for a Kafka consumer
     * to consume messages with `String` keys and values.
     *
     * <p>This method configures the essential Kafka consumer settings,
     * including the bootstrap server URL and serializers for keys and values.</p>
     *
     * @return A mutable map containing the configuration for a `String` consumer.
     */
    fun getStringConsumerConfig(groupId: String): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        result[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        result[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        result[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return result
    }

    /**
     * Constructs and returns a configuration map for an Avro producer.
     *
     * <p>This method configures the necessary settings for a Kafka producer to publish messages
     * serialized in Avro format using Confluent's KafkaAvroSerializer. The configuration includes
     * Kafka bootstrap servers and schema registry URL, along with appropriate serializers for keys
     * and values.</p>
     *
     * @return A mutable map containing the configuration for an Avro producer.
     */
    fun getAvroProducerConfig(): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        result[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        result[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java
        result[KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistryUrl
        return result
    }

    /**
     * Constructs and returns a configuration map for an Avro consumer, customized with specified group ID.
     *
     * <p>This method leverages Kafka's and Confluent's Avro deserialization mechanisms to create a
     * consumer configuration map that can be utilized to consume messages serialized in Avro format.
     * It ensures specific customization such as enabling the Specific Avro Reader feature.
     *
     * @param groupId The consumer group ID to be used for message consumption. Cannot be null or empty.
     * @return A mutable map containing the configuration for an Avro consumer.
     */
    fun getAvroConsumerConfig(groupId: String): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaUrl
        result[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        result[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        result[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaAvroDeserializer::class.java
        result[KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistryUrl
        result[KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG] = true
        return result
    }
}