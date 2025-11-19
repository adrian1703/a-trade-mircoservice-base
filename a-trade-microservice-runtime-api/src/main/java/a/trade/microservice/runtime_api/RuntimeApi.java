package a.trade.microservice.runtime_api;

import java.util.concurrent.ExecutorService;

public interface RuntimeApi {
    KafkaConfigs getKafkaConfigs();

    ExecutorService getExecutorService(ExecutorContext context);
}
