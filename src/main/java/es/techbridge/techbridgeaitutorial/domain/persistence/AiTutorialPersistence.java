package es.techbridge.techbridgeaitutorial.domain.persistence;

import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.AiTutorialEntity;

public interface AiTutorialPersistence {

    AiTutorialEntity create(AiTutorial aiTutorial);
}
