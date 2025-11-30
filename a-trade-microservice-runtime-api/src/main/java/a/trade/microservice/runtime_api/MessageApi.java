package a.trade.microservice.runtime_api;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Collection;

/**
 * Provides a convenient abstraction for Kafka administration and client operations.
 *
 * <p>
 * This interface allows streamlined creation and deletion of Kafka topics, and convenient
 * factory methods for constructing Kafka producers and consumers for both String and
 * Avro serialization formats.
 * </p>
 *
 * <h3>Usage Example:</h3>
 * <pre>{@code
 * MessageApi messageApi = ...; // obtain implementation, e.g., from DI
 *
 * // Create a topic
 * messageApi.createTopic(Arrays.asList("orders"), 3, 2);
 *
 * // Delete topics
 * messageApi.deleteTopic(Arrays.asList("orders"));
 *
 * // Use AdminClient
 * messageApi.withAdminClient(adminClient -> {
 *     System.out.println("Cluster info: " + adminClient.describeCluster().nodes().get());
 *     return null;
 * });
 *
 * // Create a String producer
 * try (Producer<String, String> producer = messageApi.createStringProducer()) {
 *     // Use producer.send(...) as needed
 * }
 *
 * // Create an Avro producer (substitute MyAvroType with your Avro type)
 * try (Producer<String, MyAvroType> producer = messageApi.createAvroProducer()) {
 *     // Use producer.send(new ProducerRecord<>("topic", "key", avroValue));
 * }
 *
 * // Create a String consumer
 * Consumer<String, String> consumer = messageApi.createStringConsumer("my-group");
 *
 * // Create an Avro consumer (substitute MyAvroType with your Avro type)
 * Consumer<String, MyAvroType> avroConsumer = messageApi.createAvroConsumer("my-group");
 * }</pre>
 */
public interface MessageApi {

    /**
     * Tests client connections by creating producer, consumer and admin client.
     */
    void clientSmokeTest();

    /**
     * Creates an {@link AdminClient} instance.
     * The AdminClient is NOT automatically closed after the block.
     *
     * @return The admin client.
     */
    AdminClient createAdminClient();

    /**
     * Create one or more Kafka topics with the specified names, partitions, and
     * replication factor.
     * Use the following config: partitons=1 ; replicationFactor=1.
     *
     * @param topics Collection of topic names to create.
     */
    void createTopic(Collection<String> topics);


    /**
     * Create one or more Kafka topics with the specified names, partitions, and
     * replication factor.
     *
     * @param topics            Collection of topic names to create.
     * @param partitions        Number of partitions per topic (default 1).
     * @param replicationFactor Replication factor per topic (default 1).
     */
    void createTopic(Collection<String> topics, int partitions, int replicationFactor);

    /**
     * Delete one or more Kafka topics by name.
     *
     * @param topics Collection of topic names to delete.
     */
    void deleteTopic(Collection<String> topics);

    /**
     * Recreates one or more Kafka topics by deleting and then recreating them with
     * default
     * configurations. This operation ensures the topics are reset to their initial state.
     *
     * @param topics A {@link Collection} of topic names to recreate.
     */
    void recreateTopic(Collection<String> topics);
//    void recreateTopic(Collection<String> topics, int partitions, int
//    replicationFactor);


    /**
     * Deletes one or more Kafka consumer groups identified by their group IDs.
     *
     * @param groupIds A {@link Collection} of consumer group IDs to delete.
     */
    void deleteConsumerGroups(Collection<String> groupIds);

    /**
     * Determines if the consumer has reached the last record in the topic partition it
     * is subscribed to.
     *
     * @return {@code true} if the last record has been reached; {@code false} otherwise.
     */
    <T> boolean lastRecordReached(Consumer<String, T> consumer);


    /**
     * Create a Kafka producer for String keys and values.
     *
     * @return Producer instance for String keys and values.
     */
    Producer<String, String> createStringProducer();

    /**
     * Create a Kafka producer for String keys and Avro-serialized values.
     *
     * @param <T> Avro value type.
     * @return Producer instance for String keys and Avro values.
     */
    <T> Producer<String, T> createAvroProducer();

    /**
     * Create a Kafka consumer for String keys and values.
     * Consumers do not autocommit.
     *
     * @param groupId Kafka consumer group ID.
     * @return Consumer instance for String keys and values.
     */
    Consumer<String, String> createStringConsumer(String groupId);

    /**
     * Create a Kafka consumer for String keys and Avro-serialized values.
     * Consumers do not autocommit.
     *
     * @param groupId Kafka consumer group ID.
     * @param <T>     Avro value type.
     * @return Consumer instance for String keys and Avro values.
     */
    <T> Consumer<String, T> createAvroConsumer(String groupId);
}
