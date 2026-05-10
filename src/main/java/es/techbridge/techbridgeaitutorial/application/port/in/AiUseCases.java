package es.techbridge.techbridgeaitutorial.application.port.in;

import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.CreateAiTutorialDto;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.RequestContentValidation;

import java.util.UUID;

public interface AiUseCases {

    AiTutorial create(String email, CreateAiTutorialDto aiTutorial);
    AiTutorial getById(UUID id);
    RequestContentValidation requestContentValidation(CreateAiTutorialDto request);
}
