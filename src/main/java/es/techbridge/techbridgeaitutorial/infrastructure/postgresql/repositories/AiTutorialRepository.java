package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories;

import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.AiTutorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AiTutorialRepository extends JpaRepository<AiTutorialEntity, UUID> {

    void deleteAll();

    Optional<AiTutorialEntity> findById(UUID id);
}
