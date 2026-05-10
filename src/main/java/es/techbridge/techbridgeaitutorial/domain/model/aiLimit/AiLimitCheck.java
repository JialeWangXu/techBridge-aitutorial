package es.techbridge.techbridgeaitutorial.domain.model.aiLimit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiLimitCheck {
    private boolean globalLimitReached;
    private int userLimitRemaining;
}
