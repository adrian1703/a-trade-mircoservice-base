package integrationTest.a.trading.microservice.base.kafka

import a.trading.microservice.base.kafka.KafkaClientConfigs
import kafka_message.StockAggregate
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*


class KafkaClientFactoryTest {

    companion object {
        val avroTestTopic = "test-topic-avro"
    }

    fun `Test publishing to live setup`() {
        val configs = KafkaClientConfigs()
        val config = configs.getStringProducerConfig()
        config[ProducerConfig.CLIENT_ID_CONFIG] = "test-producer"
        val producer = configs.createStringProducer(config)
        producer.send(
            ProducerRecord("test-topic-basic", "hello", "world")
        ).get()
        producer.close()
    }

    fun `Test publishing Stockaggregate to live setup`() {
        val configs = KafkaClientConfigs()
        val config = configs.getStringProducerConfig()
        config[ProducerConfig.CLIENT_ID_CONFIG] = "test-producer"

        val producer = KafkaProducer<String, StockAggregate>(configs.getAvroProducerConfig())
        val testObject = getTestObject()
        producer.send(
            ProducerRecord(avroTestTopic, testObject.ticker, testObject)
        ).get()
        producer.close()
    }

    fun `Test reading from Stockaggregate live setup`() {
        val configs = KafkaClientConfigs()
        val config = configs.getAvroConsumerConfig("test-${UUID.randomUUID()}")
//        val config = configs.getStringConsumerConfig("test-${UUID.randomUUID()}")
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        val consumer = KafkaConsumer<String, StockAggregate>(config)
        consumer.subscribe(listOf(avroTestTopic))
        println("##################################################")
        for (i in 1..5) {
            println("Polling")
            val records = consumer.poll(java.time.Duration.ofMillis(2000))
            records.forEach { record ->
                println(record.key())
                println(record.value())
            }
        }
        println("##################################################")
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