package es.techbridge.techbridgeaitutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class TechbridgeAitutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechbridgeAitutorialApplication.class, args);
    }

}
