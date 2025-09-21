package integrationTest.a.trading.microservice.base.kafka

import a.trading.microservice.base.kafka.KafkaClientFactory
import kafka_message.StockAggregate
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test


class KafkaClientFactoryTest {

    companion object {
        lateinit var factory: KafkaClientFactory

        @BeforeAll
        @JvmStatic
        fun init() {
            factory = KafkaClientFactory()
        }
    }

    @Test
    fun `Test publishing to live setup`() {
        val config = factory.getDefaultConfig()
        config[ProducerConfig.CLIENT_ID_CONFIG] = "test-producer"
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9094"

        val producer = factory.createProducer(config)
        producer.send(
            ProducerRecord("test-topic-basic", "hello", "world")
        )
        producer.close()
    }

    fun getTestObject(): StockAggregate {
        val result = StockAggregate
            .newBuilder()
            .setTicker("TEST")
            .setLow(0.1)
            .setHigh(1.0)
            .setOpen(0.5)
            .setClose(0.5)
            .setWindowStart(111)
            .setVolume(10)
            .build()
        return result
    }
}