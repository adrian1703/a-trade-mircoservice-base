package unitTest.a.trading.microservice.base.utils

import a.trade.microservice.runtime_api.RuntimeApi
import a.trading.microservice.base.concurrent.DefaultExecutorService
import a.trading.microservice.base.kafka.KafkaClientConfigsImpl
import a.trading.microservice.base.runtime_api.ProdRuntimeApi
import org.springframework.stereotype.Component

@Component
class DummyRuntimeApi(
    kafkaConfigs: KafkaClientConfigsImpl = KafkaClientConfigsImpl(),
    executorService: DefaultExecutorService = DefaultExecutorService()
) : ProdRuntimeApi(kafkaConfigs,
                   executorService), RuntimeApi {
}