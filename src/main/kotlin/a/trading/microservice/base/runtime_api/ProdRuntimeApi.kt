package a.trading.microservice.base.runtime_api

import a.trade.microservice.runtime_api.ExecutorContext
import a.trade.microservice.runtime_api.KafkaConfigs
import a.trade.microservice.runtime_api.MessageApi
import a.trade.microservice.runtime_api.RuntimeApi
import a.trading.microservice.base.concurrent.ExecutorServiceProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService

@Configuration
class ProdRuntimeApi(
    private val kafkaConfigs: KafkaConfigs,
    private val messageApi: MessageApi,
    private val executorServiceProvider: ExecutorServiceProvider,
) : RuntimeApi {

    override fun getKafkaConfigs(): KafkaConfigs {
        return kafkaConfigs
    }

    override fun getExecutorService(context: ExecutorContext): ExecutorService {
        return executorServiceProvider.getExecutorService(context)
    }

    override fun getMessageApi(): MessageApi? {
        return messageApi
    }

    @Bean
    override fun getExecutorService(): ExecutorService {
        return this.getExecutorService(ExecutorContext.DEFAULT)
    }
}