package general;

import a.trade.microservice.runtime_api.test.TestInterface;

public class TestInterfaceJavaImpl implements TestInterface {

    @Override
    public String test() {
        return "test";
    }
}
