package es.techbridge.techbridgeaitutorial.application.port.out.aiModel;

import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.CreateAiTutorialDto;

public interface AiModelFacade {
    AiTutorial generateAiTutorial(String email, CreateAiTutorialDto aiTutorial);
}
