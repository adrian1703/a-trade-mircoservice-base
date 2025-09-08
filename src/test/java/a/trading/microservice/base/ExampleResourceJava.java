package a.trading.microservice.base;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello2")
public class ExampleResourceJava {

        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public String hello2() {
            return "Hello from Quarkus REST";
        }
}
