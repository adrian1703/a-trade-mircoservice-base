package a.trading.microservice.base.runtime_api

import a.trade.microservice.runtime_api.ExecutorContext
import a.trade.microservice.runtime_api.KafkaConfigs
import a.trade.microservice.runtime_api.RuntimeApi
import a.trading.microservice.base.concurrent.DefaultExecutorService
import a.trading.microservice.base.kafka.KafkaClientConfigsImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService

@Configuration
class ProdRuntimeApi(
    private val kafkaConfigs: KafkaClientConfigsImpl,
    private val executorService: DefaultExecutorService,
) : RuntimeApi {

    override fun getKafkaConfigs(): KafkaConfigs {
        return kafkaConfigs
    }

    override fun getExecutorService(context: ExecutorContext): ExecutorService {
        return executorService.getExecutorService(context)
    }

    @Bean
    override fun getExecutorService(): ExecutorService {
        return this.getExecutorService(ExecutorContext.DEFAULT)
    }
}