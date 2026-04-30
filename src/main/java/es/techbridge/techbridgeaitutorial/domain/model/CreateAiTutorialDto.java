package es.techbridge.techbridgeaitutorial.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAiTutorialDto {

    private String title;
    private String description;
    private UUID helpRequestId;
}
