package co.istad.mbanking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Mobile Banking API",description = "API documentation for the Mobile Banking application",version = "1.0"))
public class Sbc10MbankingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sbc10MbankingApiApplication.class, args);
    }

}
