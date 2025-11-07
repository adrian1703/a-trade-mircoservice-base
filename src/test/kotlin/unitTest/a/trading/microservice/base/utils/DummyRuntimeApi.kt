package unitTest.a.trading.microservice.base.utils

import a.trade.microservice.runtime_api.RuntimeApi
import a.trading.microservice.base.runtime_api.ProdRuntimeApi
import org.springframework.stereotype.Component

@Component
class DummyRuntimeApi : ProdRuntimeApi(), RuntimeApi {
}