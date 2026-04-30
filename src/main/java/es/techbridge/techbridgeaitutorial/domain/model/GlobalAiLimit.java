package es.techbridge.techbridgeaitutorial.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalAiLimit {

    private LocalDate date;
    private int totalCalls;
    private int maxLimit;
}
