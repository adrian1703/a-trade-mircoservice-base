package integrationTest.a.trading.microservice.base

import integrationTest.a.trading.microservice.base.kafka.KafkaClientFactoryTest

fun main() {
    val kafkaClientFactory = KafkaClientFactoryTest()
    println("############### Test publishing to live setup ###############")
    kafkaClientFactory.`Test publishing to live setup`()
    println("############################################################")
    println("############### Test publishing Stockaggregate to live setup ###############")
    kafkaClientFactory.`Test publishing Stockaggregate to live setup`()
    println("############################################################")
    println("############### Test reading from Stockaggregate live setup ###############")
    kafkaClientFactory.`Test reading from Stockaggregate live setup`()
    println("############################################################")
}