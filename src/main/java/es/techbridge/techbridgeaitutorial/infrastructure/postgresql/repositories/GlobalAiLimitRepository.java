package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories;

import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.GlobalAiLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GlobalAiLimitRepository extends JpaRepository<GlobalAiLimitEntity, LocalDate> {

    Optional<GlobalAiLimitEntity> findByDate(LocalDate date);
}
