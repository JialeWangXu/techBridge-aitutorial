package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories;

import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.UserDailyAiLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDailyAiLimitRepository extends JpaRepository<UserDailyAiLimitEntity, UUID> {

    Optional<UserDailyAiLimitEntity> findByUserId(UUID userId);

}
