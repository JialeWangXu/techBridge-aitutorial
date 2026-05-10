package es.techbridge.techbridgeaitutorial.domain.model.aiTutorial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestContentValidation {

    private boolean valid;
    private String reason;
}
