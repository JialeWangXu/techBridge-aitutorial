package es.techbridge.techbridgeaitutorial.application.port.out.persistence;

import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.UserDailyAiLimitEntity;

import java.util.UUID;

public interface UserDailyAiLimitPersistence {

    UserDailyAiLimitEntity getByUserId(UUID id);
    void incrementTotalCalls(UUID id);
}
