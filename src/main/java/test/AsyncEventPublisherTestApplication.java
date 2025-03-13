package test;

import cupid.CupidApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackageClasses = {
        CupidApplication.class,
        AsyncEventPublisherTestApplication.class
})
public class AsyncEventPublisherTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncEventPublisherTestApplication.class, args);
    }
}
