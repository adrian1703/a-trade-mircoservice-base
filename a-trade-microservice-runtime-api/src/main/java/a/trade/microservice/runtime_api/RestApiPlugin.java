package a.trade.microservice.runtime_api;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface RestApiPlugin {
    RouterFunction<ServerResponse> getRouter(RuntimeApi runtimeApi);
}
