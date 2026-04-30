package es.techbridge.techbridgeaitutorial.domain.services;

import es.techbridge.techbridgeaitutorial.domain.exceptions.UserQuotaExceededException;
import es.techbridge.techbridgeaitutorial.domain.model.UserDailyAiLimit;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.UserDailyAiLimitEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.UserDailyAiLimitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserDailyAiLimitServiceIT {

    private static final UUID SENIOR_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Autowired
    private UserDailyAiLimitService userDailyAiLimitService;

    @Autowired
    private UserDailyAiLimitRepository userDailyAiLimitRepository;

    @Test
    void getByUserId() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        UserDailyAiLimit result = this.userDailyAiLimitService.getByUserId(SENIOR_ID);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(SENIOR_ID);
        assertThat(result.getLastCallDate()).isEqualTo(today);
        assertThat(result.getCallsToday()).isEqualTo(0);
        assertThat(result.getMaxLimit()).isEqualTo(3);
    }

    @Test
    void checkUserAiLimit() {
        this.userDailyAiLimitService.checkUserAiLimit(SENIOR_ID);

        assertThat(this.userDailyAiLimitRepository.findByUserId(SENIOR_ID))
                .get()
                .extracting(UserDailyAiLimitEntity::getCallsToday)
                .isEqualTo(0);
    }

    @Test
    void checkUserAiLimitWhenLimitExceeded_thenThrowUserQuotaExceededException() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        this.userDailyAiLimitRepository.save(UserDailyAiLimitEntity.builder()
                .userId(SENIOR_ID)
                .lastCallDate(today)
                .callsToday(3)
                .maxLimit(3)
                .build());

        assertThatThrownBy(() -> this.userDailyAiLimitService.checkUserAiLimit(SENIOR_ID))
                .isInstanceOf(UserQuotaExceededException.class)
                .hasMessageContaining(SENIOR_ID.toString());
    }

    @Test
    void incrementUserDailyTotalCalls() {
        this.userDailyAiLimitService.incrementUserDailyTotalCalls(SENIOR_ID);

        assertThat(this.userDailyAiLimitRepository.findByUserId(SENIOR_ID))
                .get()
                .satisfies(entity -> {
                    assertThat(entity.getCallsToday()).isEqualTo(1);
                    assertThat(entity.getLastCallDate()).isEqualTo(LocalDate.now(ZoneId.of("UTC")));
                });
    }
}
