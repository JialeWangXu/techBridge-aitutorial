package es.techbridge.techbridgeaitutorial.infrastructure.resources;


import es.techbridge.techbridgeaitutorial.application.port.in.AiUseCases;
import es.techbridge.techbridgeaitutorial.application.port.in.UserDailyAiLimitUseCases;
import es.techbridge.techbridgeaitutorial.domain.model.aiLimit.AiLimitCheck;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.CreateAiTutorialDto;
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
    public static final String CHECK = "/check";

    private final AiUseCases aiTutorialService;
    private final UserDailyAiLimitUseCases userDailyAiLimitUseCases;

    @Autowired
    public AiTutorialResource(AiUseCases aiTutorialService, UserDailyAiLimitUseCases userDailyAiLimitUseCases) {
        this.aiTutorialService = aiTutorialService;
        this.userDailyAiLimitUseCases = userDailyAiLimitUseCases;
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

    @GetMapping(CHECK)
    @PreAuthorize("hasRole('SENIOR')")
    public AiLimitCheck aiLimitCheck(@AuthenticationPrincipal Jwt jwt){
        return this.userDailyAiLimitUseCases.checkAiLimit(jwt.getSubject());
    }
}
