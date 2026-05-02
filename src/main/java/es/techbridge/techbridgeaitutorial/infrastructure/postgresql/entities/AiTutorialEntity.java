package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities;

import es.techbridge.techbridgeaitutorial.domain.mapper.StepsConverter;
import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.Step;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_ai_tutorial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AiTutorialEntity extends BaseAuditEntity{

    @Id
    @Column(updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String generalDescription;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = StepsConverter.class) // Aquí ocurre la magia
    private List<Step> steps;

    public AiTutorialEntity(AiTutorial dto){
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.generalDescription = dto.getGeneralDescription();
        this.steps = dto.getSteps();
    }

    public AiTutorial toAiTutorial(){
        return AiTutorial.builder()
                .id(this.id)
                .title(this.title)
                .generalDescription(this.generalDescription)
                .steps(this.steps)
                .build();
    }
}
