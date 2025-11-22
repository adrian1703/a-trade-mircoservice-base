package a.trading.microservice.base.kafka

import a.trade.microservice.runtime_api.KafkaConfigs
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.common.errors.TopicExistsException
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException

@Component
class KafkaWrapper(val kafkaConfigs: KafkaConfigs) {

    fun createTopic(topics: Collection<String>, partitions: Int = 1, replicationFactor: Int = 1) {
        val adminClientConfig = kafkaConfigs.getAdminClientConfig()
        val topicConfig = mapOf(
            TopicConfig.CLEANUP_POLICY_CONFIG to TopicConfig.CLEANUP_POLICY_DELETE,
            TopicConfig.RETENTION_MS_CONFIG to "-1",
            TopicConfig.RETENTION_BYTES_CONFIG to "-1"
        )
        AdminClient.create(adminClientConfig).use { adminClient ->
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
}