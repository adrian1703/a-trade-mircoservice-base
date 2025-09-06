package a.trading.microservice.base;

import a.trade.microservice.runtime_api.HelloInterface;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test class for HelloImpl.
 * <p>
 * This class contains tests for the `hello` method, which is expected to return the string "hello".
 */
class HelloImplTest {

    /**
     * Test to verify that the `hello` method returns "hello".
     */
    @Test
    void testHelloMethodReturnsHello() {
        // Arrange
        HelloInterface helloImpl = new HelloImpl();

        // Act
        String result = helloImpl.hello();

        // Assert
        assertEquals("hello", result, "The hello method should return 'hello'");
    }
}