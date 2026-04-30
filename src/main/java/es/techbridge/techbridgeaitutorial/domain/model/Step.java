package es.techbridge.techbridgeaitutorial.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step {

    private int number;
    private String instruction;
    private String advice;
}
