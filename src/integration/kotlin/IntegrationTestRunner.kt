package integrationTest.a.trading.microservice.base

import integrationTest.a.trading.microservice.base.kafka.KafkaClientFactoryTest

fun main() {
    val kafkaClientFactory = KafkaClientFactoryTest()
    kafkaClientFactory.`Test publishing to live setup`()
}