package general;

import a.trade.microservice.runtime_api.test.TestInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestInterfaceJavaImplTest {

    @Test
    void testHelloMethodReturnsHello() {
        // Arrangeg
        TestInterface testInterface = new TestInterfaceJavaImpl();

        // Act
        String result = testInterface.test();

        // Assert
        assertEquals("test", result, "The hello method should return 'test'");
    }
}