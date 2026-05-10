package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities;

import es.techbridge.techbridgeaitutorial.domain.model.aiLimit.GlobalAiLimit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "tb_global_ai_limit")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalAiLimitEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private LocalDate date;

    private int totalCalls;
    @Column(nullable = false)
    private int maxLimit;

    public GlobalAiLimitEntity(GlobalAiLimit globalAiLimit){
        this.date = globalAiLimit.getDate();
        this.totalCalls = globalAiLimit.getTotalCalls();
        this.maxLimit=globalAiLimit.getMaxLimit();
    }

    public GlobalAiLimit toGlobalAiLimit(){
        return GlobalAiLimit.builder()
                .date(this.date)
                .totalCalls(this.totalCalls)
                .maxLimit(this.maxLimit)
                .build();
    }

}
