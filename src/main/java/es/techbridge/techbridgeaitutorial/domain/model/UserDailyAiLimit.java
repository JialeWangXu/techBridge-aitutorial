package es.techbridge.techbridgeaitutorial.domain.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDailyAiLimit {

    private UUID userId;
    private LocalDate lastCallDate;
    private int callsToday;
    private int maxLimit;
}
