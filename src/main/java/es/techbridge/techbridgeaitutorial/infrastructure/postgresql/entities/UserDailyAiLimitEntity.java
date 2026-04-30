package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities;

import es.techbridge.techbridgeaitutorial.domain.model.UserDailyAiLimit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_user_daily_ai_limit")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDailyAiLimitEntity {

    @Id
    @Column(updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @Column(nullable = false)
    private LocalDate lastCallDate;

    private int callsToday;
    @Column(nullable = false)
    private int maxLimit;

    public UserDailyAiLimitEntity(UserDailyAiLimit userDailyAiLimit){
        this.userId = userDailyAiLimit.getUserId();
        this.lastCallDate = userDailyAiLimit.getLastCallDate();
        this.callsToday = userDailyAiLimit.getCallsToday();
        this.maxLimit = userDailyAiLimit.getMaxLimit();
    }

    public UserDailyAiLimit toUserDailyAiLimit(){
        return UserDailyAiLimit.builder()
                .userId(this.userId)
                .lastCallDate(this.lastCallDate)
                .callsToday(this.callsToday)
                .maxLimit(this.maxLimit)
                .build();
    }
}
