package es.techbridge.techbridgeaitutorial.domain.services;

import es.techbridge.techbridgeaitutorial.application.services.GlobalAiLimitService;
import es.techbridge.techbridgeaitutorial.application.services.MailService;
import es.techbridge.techbridgeaitutorial.domain.exceptions.GlobalQuotaExceededException;
import es.techbridge.techbridgeaitutorial.domain.model.aiLimit.GlobalAiLimit;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.GlobalAiLimitEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.GlobalAiLimitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class GlobalAiLimitServiceIT {

    @Autowired
    private GlobalAiLimitService globalAiLimitService;

    @Autowired
    private GlobalAiLimitRepository globalAiLimitRepository;

    @MockitoBean
    private MailService mailService;

    @Test
    void getByDate() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        GlobalAiLimit result = this.globalAiLimitService.getByDate(today);

        assertThat(result).isNotNull();
        assertThat(result.getDate()).isEqualTo(today);
        assertThat(result.getTotalCalls()).isEqualTo(1);
        assertThat(result.getMaxLimit()).isEqualTo(200);
    }

    @Test
    void checkGlobalAiLimit() {
        this.globalAiLimitService.checkGlobalAiLimit();

        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        assertThat(this.globalAiLimitRepository.findByDate(today))
                .get()
                .satisfies(entity -> {
                    assertThat(entity.getTotalCalls()).isEqualTo(1);
                    assertThat(entity.getMaxLimit()).isEqualTo(200);
                });
    }

    @Test
    void checkGlobalAiLimitWhenWarningThreshold_thenSendWarningEmail() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        this.globalAiLimitRepository.save(GlobalAiLimitEntity.builder()
                .date(today)
                .totalCalls(2)
                .maxLimit(200)
                .build());

        this.globalAiLimitService.checkGlobalAiLimit();

        verify(this.mailService).sendWarningEmail(2);
    }

    @Test
    void checkGlobalAiLimitWhenLimitExceeded_thenThrowGlobalQuotaExceededException() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        this.globalAiLimitRepository.save(GlobalAiLimitEntity.builder()
                .date(today)
                .totalCalls(200)
                .maxLimit(200)
                .build());

        assertThatThrownBy(() -> this.globalAiLimitService.checkGlobalAiLimit())
                .isInstanceOf(GlobalQuotaExceededException.class)
                .hasMessageContaining("Cupo del sistema agotado");
    }

    @Test
    void incrementGlobalTotalCalls() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        this.globalAiLimitService.incrementGlobalTotalCalls();

        assertThat(this.globalAiLimitRepository.findByDate(today))
                .get()
                .extracting(GlobalAiLimitEntity::getTotalCalls)
                .isEqualTo(2);
    }

    @Test
    void checkIfGlobalLimitReached() {
        var resul = this.globalAiLimitService.getIfGlobalLimitReached();
        assertThat(resul).isFalse();
    }
}
