package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.persistence;

import es.techbridge.techbridgeaitutorial.application.port.out.persistence.GlobalAiLimitPersistence;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.GlobalAiLimitEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.GlobalAiLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Repository
public class GlobalAiLimitPersistencePostgres implements GlobalAiLimitPersistence {

    private final GlobalAiLimitRepository globalAiLimitRepository;

    @Value("${app.ai.limits.global-max}")
    private int globalMaxLimit;

    @Autowired
    public GlobalAiLimitPersistencePostgres(GlobalAiLimitRepository globalAiLimitRepository) {
        this.globalAiLimitRepository = globalAiLimitRepository;
    }


    @Override
    public GlobalAiLimitEntity getByDate(LocalDate date) {
        Optional<GlobalAiLimitEntity> globalAiLimit = this.globalAiLimitRepository.findByDate(date);
        if(globalAiLimit.isPresent()){
            return globalAiLimit.get();
        }else{
            LocalDate today = LocalDate.now(ZoneId.of("UTC"));
            GlobalAiLimitEntity globalAiLimitEntity = GlobalAiLimitEntity.builder()
                    .date(today)
                    .totalCalls(0)
                    .maxLimit(globalMaxLimit).build();
            this.globalAiLimitRepository.save(globalAiLimitEntity);
            return globalAiLimitEntity;
        }
    }

    @Override
    public void incrementTotalCalls(LocalDate date) {
        GlobalAiLimitEntity globalAiLimit = this.getByDate(date);
        globalAiLimit.setTotalCalls(globalAiLimit.getTotalCalls()+1);
        this.globalAiLimitRepository.save(globalAiLimit);
    }
}
