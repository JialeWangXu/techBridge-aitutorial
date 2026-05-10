package es.techbridge.techbridgeaitutorial.application.port.in;

import es.techbridge.techbridgeaitutorial.domain.model.aiLimit.AiLimitCheck;
import es.techbridge.techbridgeaitutorial.domain.model.aiLimit.UserDailyAiLimit;

import java.util.UUID;

public interface UserDailyAiLimitUseCases {
    void incrementUserDailyTotalCalls(UUID userId);
    void checkUserAiLimit(UUID userId);
    UserDailyAiLimit getByUserId(UUID id);
    AiLimitCheck checkAiLimit(String email);
}
