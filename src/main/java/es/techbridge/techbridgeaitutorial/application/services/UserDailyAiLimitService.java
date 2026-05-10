package es.techbridge.techbridgeaitutorial.application.services;

import es.techbridge.techbridgeaitutorial.application.port.in.GlobalAiLimitUseCases;
import es.techbridge.techbridgeaitutorial.application.port.in.UserDailyAiLimitUseCases;
import es.techbridge.techbridgeaitutorial.application.port.out.webclients.UserWebClient;
import es.techbridge.techbridgeaitutorial.domain.exceptions.UserQuotaExceededException;
import es.techbridge.techbridgeaitutorial.domain.model.aiLimit.AiLimitCheck;
import es.techbridge.techbridgeaitutorial.domain.model.aiLimit.UserDailyAiLimit;
import es.techbridge.techbridgeaitutorial.application.port.out.persistence.UserDailyAiLimitPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDailyAiLimitService implements UserDailyAiLimitUseCases {

    private final UserDailyAiLimitPersistence userDailyAiLimitPersistence;
    private final GlobalAiLimitUseCases globalAiLimitUseCases;
    private final UserWebClient userWebClient;

    @Autowired
    public UserDailyAiLimitService(UserDailyAiLimitPersistence userDailyAiLimitPersistence, GlobalAiLimitUseCases globalAiLimitUseCases, UserWebClient userWebClient) {
        this.userDailyAiLimitPersistence = userDailyAiLimitPersistence;
        this.globalAiLimitUseCases = globalAiLimitUseCases;
        this.userWebClient = userWebClient;
    }

    public UserDailyAiLimit getByUserId(UUID id){
        return this.userDailyAiLimitPersistence.getByUserId(id).toUserDailyAiLimit();
    }

    @Override
    public AiLimitCheck checkAiLimit(String email) {
        UUID userId = this.userWebClient.getIdByEmail(email);
        UserDailyAiLimit limit = getByUserId(userId);
        boolean globalLimit = this.globalAiLimitUseCases.getIfGlobalLimitReached();
        return new AiLimitCheck(globalLimit, limit.getMaxLimit()- limit.getCallsToday());
    }

    public void checkUserAiLimit(UUID userId){
        UserDailyAiLimit userDailyAiLimit = this.getByUserId(userId);
        if(userDailyAiLimit.getCallsToday()>=userDailyAiLimit.getMaxLimit()){
            throw new UserQuotaExceededException("Cupo del usuario agotado. UserId: "+userId);
        }
    }

    public void incrementUserDailyTotalCalls(UUID userId){
        this.userDailyAiLimitPersistence.incrementTotalCalls(userId);
    }
}
