package es.techbridge.techbridgeaitutorial.domain.persistence;

import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.GlobalAiLimitEntity;

import java.time.LocalDate;

public interface GlobalAiLimitPersistence {

    GlobalAiLimitEntity getByDate(LocalDate date);

    void incrementTotalCalls(LocalDate date);
}
