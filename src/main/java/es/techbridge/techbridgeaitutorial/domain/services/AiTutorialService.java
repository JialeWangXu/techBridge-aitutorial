package es.techbridge.techbridgeaitutorial.domain.services;

import es.techbridge.techbridgeaitutorial.domain.exceptions.FailedCreateAiTutorialException;
import es.techbridge.techbridgeaitutorial.domain.exceptions.NotFoundException;
import es.techbridge.techbridgeaitutorial.domain.webclients.UserWebClient;
import org.springframework.core.io.Resource;
import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.CreateAiTutorialDto;
import es.techbridge.techbridgeaitutorial.domain.persistence.AiTutorialPersistence;
import es.techbridge.techbridgeaitutorial.domain.webclients.HelpRequestWebClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

@Service
public class AiTutorialService {

    private final AiTutorialPersistence aiTutorialPersistence;
    private final ChatClient chatClient;
    private final HelpRequestWebClient helpRequestWebClient;
    private final GlobalAiLimitService globalAiLimitService;
    private final UserDailyAiLimitService userDailyAiLimitService;
    private final UserWebClient userWebClient;
    @Value("classpath:/prompts/system_prompt.st")
    private Resource systemPromptResource;

    @Autowired
    public AiTutorialService(AiTutorialPersistence aiTutorialPersistence,
                             ChatClient.Builder builder,
                             HelpRequestWebClient helpRequestWebClient,
                             GlobalAiLimitService globalAiLimitService,
                             UserDailyAiLimitService userDailyAiLimitService,
                             UserWebClient userWebClient) {
        this.aiTutorialPersistence = aiTutorialPersistence;
        this.chatClient = builder.build();
        this.helpRequestWebClient = helpRequestWebClient;
        this.globalAiLimitService = globalAiLimitService;
        this.userDailyAiLimitService = userDailyAiLimitService;
        this.userWebClient = userWebClient;
    }

    public AiTutorial create(String email,CreateAiTutorialDto aiTutorial) {

        String systemPrompt;
        try {
             systemPrompt = systemPromptResource.getContentAsString(Charset.defaultCharset());
        }catch (IOException ex){
            throw new NotFoundException("Please revise prompt resource.");
        }
        UUID userId = this.userWebClient.getIdByEmail(email);
        // Ai usage limit controls
        this.globalAiLimitService.checkGlobalAiLimit();
        this.userDailyAiLimitService.checkUserAiLimit(userId);

        AiTutorial tutorial = this.chatClient.prompt()
                .system(s -> s.text(systemPrompt)
                        .params(Map.of("title", aiTutorial.getTitle(),
                                "description", aiTutorial.getDescription())))
                .call()
                .entity(AiTutorial.class);
//        AiTutorial tutorial = AiTutorial.builder()
//                .title("Testing").generalDescription("Only for testing...").steps(
//                        List.of(
//                                new Step(1, "Abre la app", "Icono verde"),
//                                new Step(2, "Selecciona chat", null)
//                        )
//                ).build();
        if(tutorial!=null){
            tutorial.setId(UUID.randomUUID());
            AiTutorial result = this.aiTutorialPersistence.create(tutorial).toAiTutorial();
            this.helpRequestWebClient.saveAiTutorialId(aiTutorial.getHelpRequestId(),result.getId());
            this.globalAiLimitService.incrementGlobalTotalCalls();
            this.userDailyAiLimitService.incrementUserDailyTotalCalls(userId);
            return result;
        }else{
            String errorMessage = "HelpRequest Id: "+aiTutorial.getHelpRequestId();
            throw new FailedCreateAiTutorialException(errorMessage);
        }
    }
}
