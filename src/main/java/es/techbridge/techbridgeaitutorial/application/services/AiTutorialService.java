package es.techbridge.techbridgeaitutorial.application.services;

import es.techbridge.techbridgeaitutorial.application.port.in.AiTutorialUseCases;
import es.techbridge.techbridgeaitutorial.application.port.in.GlobalAiLimitUseCases;
import es.techbridge.techbridgeaitutorial.application.port.in.UserDailyAiLimitUseCases;
import es.techbridge.techbridgeaitutorial.application.port.out.aiModel.AiModelFacade;
import es.techbridge.techbridgeaitutorial.domain.exceptions.FailedCreateAiTutorialException;
import es.techbridge.techbridgeaitutorial.application.port.out.webclients.UserWebClient;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.CreateAiTutorialDto;
import es.techbridge.techbridgeaitutorial.application.port.out.persistence.AiTutorialPersistence;
import es.techbridge.techbridgeaitutorial.application.port.out.webclients.HelpRequestWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AiTutorialService implements AiTutorialUseCases {

    private final AiTutorialPersistence aiTutorialPersistence;
    private final HelpRequestWebClient helpRequestWebClient;
    private final GlobalAiLimitUseCases globalAiLimitUseCases;
    private final UserDailyAiLimitUseCases userDailyAiLimitUseCases;
    private final UserWebClient userWebClient;
    private final AiModelFacade aiModelFacade;

    @Autowired
    public AiTutorialService(AiTutorialPersistence aiTutorialPersistence,
                             HelpRequestWebClient helpRequestWebClient,
                             GlobalAiLimitUseCases globalAiLimitUseCases,
                             UserDailyAiLimitUseCases userDailyAiLimitUseCases,
                             UserWebClient userWebClient, AiModelFacade aiModelFacade) {
        this.aiTutorialPersistence = aiTutorialPersistence;
        this.helpRequestWebClient = helpRequestWebClient;
        this.globalAiLimitUseCases = globalAiLimitUseCases;
        this.userDailyAiLimitUseCases = userDailyAiLimitUseCases;
        this.userWebClient = userWebClient;
        this.aiModelFacade = aiModelFacade;
    }

    public AiTutorial create(String email,CreateAiTutorialDto aiTutorial) {

        UUID userId = this.userWebClient.getIdByEmail(email);
        // Ai usage limit controls
        this.globalAiLimitUseCases.checkGlobalAiLimit();
        this.userDailyAiLimitUseCases.checkUserAiLimit(userId);
        AiTutorial tutorial = this.aiModelFacade.generateAiTutorial(email,aiTutorial);

        if(tutorial!=null){
            tutorial.setId(UUID.randomUUID());
            AiTutorial result = this.aiTutorialPersistence.create(tutorial).toAiTutorial();
            this.helpRequestWebClient.saveAiTutorialId(aiTutorial.getHelpRequestId(),result.getId());
            this.globalAiLimitUseCases.incrementGlobalTotalCalls();
            this.userDailyAiLimitUseCases.incrementUserDailyTotalCalls(userId);
            return result;
        }else{
            String errorMessage = "HelpRequest Id: "+aiTutorial.getHelpRequestId();
            throw new FailedCreateAiTutorialException(errorMessage);
        }
    }

    public AiTutorial getById(UUID id){
        return this.aiTutorialPersistence.getById(id).toAiTutorial();
    }
}
