package unitTest.a.trading.microservice.base.utils

import a.trade.microservice.runtime_api.MessageApi
import a.trade.microservice.runtime_api.RuntimeApi
import a.trading.microservice.base.concurrent.ExecutorServiceProvider
import a.trading.microservice.base.kafka.KafkaClientConfigsImpl
import a.trading.microservice.base.kafka.KafkaWrapper
import a.trading.microservice.base.runtime_api.ProdRuntimeApi
import org.springframework.stereotype.Component

/**
 * Not really a "dummy" api yet.
 */
@Component
class DummyRuntimeApi(
    kafkaConfigs: KafkaClientConfigsImpl = KafkaClientConfigsImpl(),
    messageApi: MessageApi = KafkaWrapper(kafkaConfigs),
    executorService: ExecutorServiceProvider = ExecutorServiceProvider(),
) : ProdRuntimeApi(kafkaConfigs,
                   messageApi,
                   executorService), RuntimeApi {
}