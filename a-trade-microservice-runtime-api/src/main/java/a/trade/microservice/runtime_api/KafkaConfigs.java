package a.trade.microservice.runtime_api;

import java.util.Map;

public interface KafkaConfigs {

    /**
     * Constructs and returns a configuration map for a Kafka producer to produce messages
     * with `String` keys and values.
     *
     * <p>This method configures essential Kafka producer settings such as the bootstrap
     * server URL and the serializers for both keys and values.</p>
     *
     * @return A mutable map containing the configuration for a `String` producer.
     */
    Map<String, Object> getStringProducerConfig();

    /**
     * Constructs and returns a configuration map for a Kafka consumer
     * to consume messages with `String` keys and values.
     *
     * <p>This method configures the essential Kafka consumer settings,
     * including the bootstrap server URL and serializers for keys and values.</p>
     *
     * @return A mutable map containing the configuration for a `String` consumer.
     */
    Map<String, Object> getStringConsumerConfig(String groupId);

    /**
     * Constructs and returns a configuration map for an Avro producer.
     *
     * <p>This method configures the necessary settings for a Kafka producer to publish
     * messages
     * serialized in Avro format using Confluent's KafkaAvroSerializer. The
     * configuration includes
     * Kafka bootstrap servers and schema registry URL, along with appropriate
     * serializers for keys
     * and values.</p>
     *
     * @return A mutable map containing the configuration for an Avro producer.
     */
    Map<String, Object> getAvroProducerConfig();


    /**
     * Constructs and returns a configuration map for an Avro consumer, customized with
     * specified group ID.
     *
     * <p>This method leverages Kafka's and Confluent's Avro deserialization mechanisms
     * to create a
     * consumer configuration map that can be utilized to consume messages serialized
     * in Avro format.
     * It ensures specific customization such as enabling the Specific Avro Reader
     * feature.
     *
     * @param groupId The consumer group ID to be used for message consumption. Cannot
     *                be null or empty.
     * @return A mutable map containing the configuration for an Avro consumer.
     */
    Map<String, Object> getAvroConsumerConfig(String groupId);
}
