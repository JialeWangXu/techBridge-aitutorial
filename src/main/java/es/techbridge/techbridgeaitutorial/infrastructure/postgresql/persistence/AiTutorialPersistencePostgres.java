package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.persistence;

import es.techbridge.techbridgeaitutorial.domain.exceptions.NotFoundException;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.AiTutorial;
import es.techbridge.techbridgeaitutorial.application.port.out.persistence.AiTutorialPersistence;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.AiTutorialEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.AiTutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public class AiTutorialPersistencePostgres implements AiTutorialPersistence{

    private final AiTutorialRepository aiTutorialRepository;

    @Autowired
    public AiTutorialPersistencePostgres(AiTutorialRepository aiTutorialRepository) {
        this.aiTutorialRepository = aiTutorialRepository;
    }


    @Override
    public AiTutorialEntity create(AiTutorial aiTutorial) {

        AiTutorialEntity aiTutorialEntity = new AiTutorialEntity(aiTutorial);
        this.aiTutorialRepository.save(aiTutorialEntity);
        return aiTutorialEntity;
    }

    @Override
    public AiTutorialEntity getById(UUID id) {
        return this.aiTutorialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No ai tutorial found with corresponding ID: "+id));
    }
}
