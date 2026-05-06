package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.persistence;

import es.techbridge.techbridgeaitutorial.domain.persistence.UserDailyAiLimitPersistence;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.UserDailyAiLimitEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.UserDailyAiLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDailyAiLimitPersistencePostgres implements UserDailyAiLimitPersistence {

    private final UserDailyAiLimitRepository userDailyAiLimitRepository;
    @Value("${app.ai.limits.user-max}")
    private int userMaxLimit;

    @Autowired
    public UserDailyAiLimitPersistencePostgres(UserDailyAiLimitRepository userDailyAiLimitRepository) {
        this.userDailyAiLimitRepository = userDailyAiLimitRepository;
    }

    @Override
    public UserDailyAiLimitEntity getByUserId(UUID id) {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        Optional<UserDailyAiLimitEntity> userDailyAiLimit = this.userDailyAiLimitRepository.findByUserId(id);

        if(userDailyAiLimit.isPresent()){

            if(!userDailyAiLimit.get().getLastCallDate().isEqual(today)){
                userDailyAiLimit.get().setLastCallDate(today);
                userDailyAiLimit.get().setMaxLimit(userMaxLimit);
                userDailyAiLimit.get().setCallsToday(0);
                this.userDailyAiLimitRepository.save(userDailyAiLimit.get());
            }
            return userDailyAiLimit.get();

        }else{
            UserDailyAiLimitEntity userDailyAiLimitEntity = UserDailyAiLimitEntity
                    .builder()
                    .userId(id)
                    .lastCallDate(today)
                    .callsToday(0)
                    .maxLimit(userMaxLimit)
                    .build();
            this.userDailyAiLimitRepository.save(userDailyAiLimitEntity);
            return userDailyAiLimitEntity;
        }
    }

    @Override
    public void incrementTotalCalls(UUID id) {
        UserDailyAiLimitEntity aiLimitEntity = this.getByUserId(id);
        aiLimitEntity.setCallsToday(aiLimitEntity.getCallsToday()+1);
        this.userDailyAiLimitRepository.save(aiLimitEntity);
    }
}
