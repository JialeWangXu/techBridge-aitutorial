package es.techbridge.techbridgeaitutorial.infrastructure.resources;


import es.techbridge.techbridgeaitutorial.application.port.in.AiTutorialUseCases;
import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.CreateAiTutorialDto;
import es.techbridge.techbridgeaitutorial.application.services.AiTutorialService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Log4j2
@RequestMapping(AiTutorialResource.AITUTORIAL)
public class AiTutorialResource {

    public static final String AITUTORIAL = "/aitutorial";
    public static final String ID = "/{id}";

    private final AiTutorialUseCases aiTutorialService;

    @Autowired
    public AiTutorialResource(AiTutorialUseCases aiTutorialService) {
        this.aiTutorialService = aiTutorialService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SENIOR')")
    public AiTutorial create(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateAiTutorialDto createAiTutorialDto){
        String email = jwt.getSubject();
        return this.aiTutorialService.create(email,createAiTutorialDto);
    }

    @GetMapping(ID)
    @PreAuthorize("hasAnyRole('SENIOR', 'VOLUNTEER')")
    public AiTutorial getById(@PathVariable UUID id){
        return this.aiTutorialService.getById(id);
    }
}
