package a.trading.microservice.base.runtime_api

import a.trade.microservice.runtime_api.KafkaConfigs
import a.trade.microservice.runtime_api.RuntimeApi
import a.trading.microservice.base.kafka.KafkaClientConfigsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class ProdRuntimeApi : RuntimeApi {

    @Autowired
    lateinit var kafkaConfigs: KafkaClientConfigsImpl

    override fun getKafkaConfigs(): KafkaConfigs? {
        return kafkaConfigs
    }
}