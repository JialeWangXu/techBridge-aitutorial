package es.techbridge.techbridgeaitutorial.domain.services;

import es.techbridge.techbridgeaitutorial.domain.exceptions.UserQuotaExceededException;
import es.techbridge.techbridgeaitutorial.domain.model.UserDailyAiLimit;
import es.techbridge.techbridgeaitutorial.domain.persistence.UserDailyAiLimitPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDailyAiLimitService {

    private final UserDailyAiLimitPersistence userDailyAiLimitPersistence;

    @Autowired
    public UserDailyAiLimitService(UserDailyAiLimitPersistence userDailyAiLimitPersistence) {
        this.userDailyAiLimitPersistence = userDailyAiLimitPersistence;
    }

    public UserDailyAiLimit getByUserId(UUID id){
        return this.userDailyAiLimitPersistence.getByUserId(id).toUserDailyAiLimit();
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
