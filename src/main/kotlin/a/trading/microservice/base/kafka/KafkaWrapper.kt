package a.trading.microservice.base.kafka

import a.trade.microservice.runtime_api.KafkaConfigs
import a.trade.microservice.runtime_api.MessageApi
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.common.errors.TopicExistsException
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException

@Component
class KafkaWrapper(val kafkaConfigs: KafkaConfigs) : MessageApi {

    fun <R> withAdminClient(block: (AdminClient) -> R): R {
        val adminClientConfig = kafkaConfigs.getAdminClientConfig()
        AdminClient.create(adminClientConfig).use { adminClient ->
            return block(adminClient)
        }
    }

    override fun createAdminClient(): AdminClient {
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
                createTopics.all().get()//ensure creation
            } catch (e: ExecutionException) {
                if (e.cause is TopicExistsException) {
                    // Topic already exists; safe to ignore or log
                } else {
                    throw e
                }
            }
        }
    }

    override fun deleteTopic(topics: Collection<String>) {
        withAdminClient { adminClient ->
            adminClient.deleteTopics(topics).all().get()
        }
    }

    override fun createStringProducer(): Producer<String, String> {
        return KafkaProducer(kafkaConfigs.getStringProducerConfig())
    }

    override fun <T> createAvroProducer(): Producer<String, T> {
        return KafkaProducer(kafkaConfigs.getAvroProducerConfig())
    }

    override fun createStringConsumer(groupId: String): Consumer<String, String> {
        return KafkaConsumer(kafkaConfigs.getStringConsumerConfig(groupId))
    }

    override fun <T> createAvroConsumer(groupId: String): Consumer<String, T> {
        return KafkaConsumer(kafkaConfigs.getAvroConsumerConfig(groupId))
    }
}