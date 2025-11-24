package a.trading.microservice.base.kafka

import a.trade.microservice.runtime_api.KafkaConfigs
import a.trade.microservice.runtime_api.MessageApi
import kafka_message.StockAggregate
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.common.errors.TopicExistsException
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException

@Component
class KafkaWrapper(val kafkaConfigs: KafkaConfigs) : MessageApi {

    private val logger = LoggerFactory.getLogger(KafkaWrapper::class.java)

    fun <R> withAdminClient(block: (AdminClient) -> R): R {
        val adminClientConfig = kafkaConfigs.getAdminClientConfig()
        AdminClient.create(adminClientConfig).use { adminClient ->
            return block(adminClient)
        }
    }

    override fun clientSmokeTest() {
        logger.info("Running client smoke test")
        createAdminClient().close()
        createStringProducer().close()
        createAvroProducer<StockAggregate>().close()
        createStringConsumer("smoke-test-consumer").close()
        createAvroConsumer<StockAggregate>("smoke-test-consumer").close()
        logger.info("Smoke test completed")
    }

    override fun createAdminClient(): AdminClient {
        logger.info("Creating AdminClient")
        return AdminClient.create(kafkaConfigs.getAdminClientConfig())
    }

    override fun createTopic(topics: Collection<String>) {
        return createTopic(topics, 1, 1)
    }

    override fun createTopic(topics: Collection<String>, partitions: Int, replicationFactor: Int) {
        val topicConfig = mapOf(
            TopicConfig.CLEANUP_POLICY_CONFIG to TopicConfig.CLEANUP_POLICY_DELETE,
            TopicConfig.RETENTION_MS_CONFIG to "-1",
            TopicConfig.RETENTION_BYTES_CONFIG to "-1"
        )
        logger.info("Creating topics: {} (partitions={}, replicationFactor={})", topics, partitions, replicationFactor)
        withAdminClient { adminClient ->
            val configuredTopics = topics.map { topic ->
                NewTopic(
                    topic,
                    partitions,
                    replicationFactor.toShort()
                ).configs(topicConfig)
            }
            val createTopics = adminClient.createTopics(configuredTopics)
            try {
                createTopics.all().get() // ensure creation
                logger.info("Topics created: {}", topics)
            } catch (e: ExecutionException) {
                if (e.cause is TopicExistsException) {
                    logger.warn("Attempted to create topics that already exist: {}", topics, e)
                } else {
                    logger.error("Error creating topics: {}", topics, e)
                    throw e
                }
            }
        }
    }

    override fun deleteTopic(topics: Collection<String>) {
        logger.info("Deleting topics: {}", topics)
        withAdminClient { adminClient ->
            try {
                val existingTopics = adminClient.listTopics().names().get().intersect(topics)
                logger.debug("Topics to delete after intersection: {}", existingTopics)
                adminClient.deleteTopics(existingTopics).all().get()
                logger.info("Deleted topics: {}", existingTopics)
            } catch (e: UnknownTopicOrPartitionException) {
                logger.warn("Attempted to delete unknown topic or partition: {}", topics)
            }
        }
    }

    override fun createStringProducer(): Producer<String, String> {
        logger.info("Creating string KafkaProducer")
        return KafkaProducer(kafkaConfigs.getStringProducerConfig())
    }

    override fun <T> createAvroProducer(): Producer<String, T> {
        logger.info("Creating Avro KafkaProducer")
        return KafkaProducer(kafkaConfigs.getAvroProducerConfig())
    }

    override fun createStringConsumer(groupId: String): Consumer<String, String> {
        logger.info("Creating string KafkaConsumer for group: {}", groupId)
        return KafkaConsumer(kafkaConfigs.getStringConsumerConfig(groupId))
    }

    override fun <T> createAvroConsumer(groupId: String): Consumer<String, T> {
        logger.info("Creating Avro KafkaConsumer for group: {}", groupId)
        return KafkaConsumer(kafkaConfigs.getAvroConsumerConfig(groupId))
    }
}