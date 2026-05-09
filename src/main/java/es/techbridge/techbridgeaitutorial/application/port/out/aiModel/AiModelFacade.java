package es.techbridge.techbridgeaitutorial.application.port.out.aiModel;

import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.CreateAiTutorialDto;

public interface AiModelFacade {
    AiTutorial generateAiTutorial(String email, CreateAiTutorialDto aiTutorial);
}
