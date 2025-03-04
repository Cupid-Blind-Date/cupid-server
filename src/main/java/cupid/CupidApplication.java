package cupid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CupidApplication {

    public static void main(String[] args) {
        SpringApplication.run(CupidApplication.class, args);
    }
}
