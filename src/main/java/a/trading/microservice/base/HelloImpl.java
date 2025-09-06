package a.trading.microservice.base;

import a.trade.microservice.runtime_api.HelloInterface;

public class HelloImpl implements HelloInterface {

    @Override
    public String hello() {
        return "hello";
    }
}
