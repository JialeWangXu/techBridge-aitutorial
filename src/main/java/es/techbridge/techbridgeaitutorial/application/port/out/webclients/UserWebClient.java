package es.techbridge.techbridgeaitutorial.application.port.out.webclients;

import es.techbridge.techbridgeaitutorial.configurations.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = UserWebClient.TECHBRIDGE_USER, configuration = FeignConfig.class)
public interface UserWebClient {
    String TECHBRIDGE_USER = "techbridge-user";
    String USERS = "/users";
    String EMAIL_ID = "/email/{email}/id";

    @GetMapping(USERS+EMAIL_ID)
    UUID getIdByEmail(@PathVariable String email);
}
