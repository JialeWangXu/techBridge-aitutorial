package es.techbridge.techbridgeaitutorial.infrastructure.aiModel;

import es.techbridge.techbridgeaitutorial.application.port.out.aiModel.AiModelFacade;
import es.techbridge.techbridgeaitutorial.domain.exceptions.FailedCreateAiTutorialException;
import es.techbridge.techbridgeaitutorial.domain.exceptions.NotFoundException;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.CreateAiTutorialDto;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.RequestContentValidation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

@Component
public class GeminiAiFacade implements AiModelFacade {

    private final ChatClient chatClient;
    @Value("classpath:/prompts/system_prompt.st")
    private Resource systemPromptResource;

    @Value("classpath:/prompts/request_validation_system_propmt.st")
    private Resource requestValidationSystemPromptResource;

    public GeminiAiFacade(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public AiTutorial generateAiTutorial(CreateAiTutorialDto aiTutorial) {
        String systemPrompt;

        RequestContentValidation requestContentValidation = requestContentValidation(aiTutorial);
        if(!requestContentValidation.isValid()){
            throw new FailedCreateAiTutorialException("El contenido no es valido.valido");
        }

        try {
            systemPrompt = systemPromptResource.getContentAsString(Charset.defaultCharset());
        }catch (IOException ex){
            throw new NotFoundException("Please revise prompt resource.");
        }
        return this.chatClient.prompt()
                .system(s -> s.text(systemPrompt)
                        .params(Map.of("title", aiTutorial.getTitle(),
                                "description", aiTutorial.getDescription())))
                .call()
                .entity(AiTutorial.class);
    }


    @Override
    public RequestContentValidation requestContentValidation(CreateAiTutorialDto request) {
        String requestValidationSystemPrompt;
        try {
            requestValidationSystemPrompt = requestValidationSystemPromptResource.getContentAsString(Charset.defaultCharset());
        }catch (IOException ex){
            throw new NotFoundException("Please revise prompt resource.");
        }
        return this.chatClient.prompt()
                .system(s -> s.text(requestValidationSystemPrompt)
                        .params(Map.of("title", request.getTitle(),
                                "description", request.getDescription())))
                .call()
                .entity(RequestContentValidation.class);
    }
}
