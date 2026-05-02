package es.techbridge.techbridgeaitutorial.domain.persistence;

import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.AiTutorialEntity;
import java.util.UUID;

public interface AiTutorialPersistence {

    AiTutorialEntity create(AiTutorial aiTutorial);

    AiTutorialEntity getById(UUID id);
}
