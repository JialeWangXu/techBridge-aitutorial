package es.techbridge.techbridgeaitutorial.domain.model.aiTutorial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiTutorial {

    private UUID id;
    private String title;
    private String generalDescription;
    private List<Step> steps;

}
