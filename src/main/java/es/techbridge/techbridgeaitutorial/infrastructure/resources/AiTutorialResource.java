package es.techbridge.techbridgeaitutorial.infrastructure.resources;


import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.CreateAiTutorialDto;
import es.techbridge.techbridgeaitutorial.domain.services.AiTutorialService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping(AiTutorialResource.AITUTORIAL)
public class AiTutorialResource {

    public static final String AITUTORIAL = "/aitutorial";

    private final AiTutorialService aiTutorialService;

    @Autowired
    public AiTutorialResource(AiTutorialService aiTutorialService) {
        this.aiTutorialService = aiTutorialService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SENIOR')")
    public AiTutorial create(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateAiTutorialDto createAiTutorialDto){
        String email = jwt.getSubject();
        return this.aiTutorialService.create(email,createAiTutorialDto);
    }
}
