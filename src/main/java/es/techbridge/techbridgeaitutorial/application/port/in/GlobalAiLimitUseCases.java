package es.techbridge.techbridgeaitutorial.application.port.in;

import es.techbridge.techbridgeaitutorial.domain.model.GlobalAiLimit;

import java.time.LocalDate;

public interface GlobalAiLimitUseCases {
    void checkGlobalAiLimit();
    GlobalAiLimit getByDate(LocalDate date);
    void incrementGlobalTotalCalls();
}
