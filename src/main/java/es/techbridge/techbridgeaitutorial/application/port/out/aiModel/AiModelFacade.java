package es.techbridge.techbridgeaitutorial.application.port.out.aiModel;

import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.CreateAiTutorialDto;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.RequestContentValidation;

public interface AiModelFacade {
    AiTutorial generateAiTutorial(CreateAiTutorialDto aiTutorial);
    RequestContentValidation requestContentValidation(CreateAiTutorialDto request);
}
