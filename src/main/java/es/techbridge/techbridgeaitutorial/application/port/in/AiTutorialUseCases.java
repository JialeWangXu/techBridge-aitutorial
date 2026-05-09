package es.techbridge.techbridgeaitutorial.application.port.in;

import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.CreateAiTutorialDto;

import java.util.UUID;

public interface AiTutorialUseCases {

    AiTutorial create(String email, CreateAiTutorialDto aiTutorial);
    AiTutorial getById(UUID id);
}
