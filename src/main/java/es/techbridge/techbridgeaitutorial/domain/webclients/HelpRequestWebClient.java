package es.techbridge.techbridgeaitutorial.domain.webclients;

import es.techbridge.techbridgeaitutorial.configurations.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = HelpRequestWebClient.TECHBRIDGE_HELPREQUEST, configuration = FeignConfig.class)
public interface HelpRequestWebClient {
    String TECHBRIDGE_HELPREQUEST = "techbridge-helprequest";
    String HELPREQUEST = "/helprequests";
    String SAVEAITUTORIAL_ID = "/saveAiTutorial/{id}";

    @PutMapping(HELPREQUEST+SAVEAITUTORIAL_ID)
    void saveAiTutorialId(@PathVariable UUID id, @RequestBody UUID aiTutorialId);

}
