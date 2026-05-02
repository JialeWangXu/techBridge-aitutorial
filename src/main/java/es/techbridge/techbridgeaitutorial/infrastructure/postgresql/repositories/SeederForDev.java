package es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories;

import es.techbridge.techbridgeaitutorial.domain.model.Step;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.AiTutorialEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.GlobalAiLimitEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.UserDailyAiLimitEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Log4j2
@Repository
@Profile({"dev", "test"})
public class SeederForDev {

    private final AiTutorialRepository aiTutorialRepository;
    private final GlobalAiLimitRepository globalAiLimitRepository;
    private final UserDailyAiLimitRepository userDailyAiLimitRepository;

    @Autowired
    public SeederForDev(AiTutorialRepository aiTutorialRepository, GlobalAiLimitRepository globalAiLimitRepository, UserDailyAiLimitRepository userDailyAiLimitRepository) {
        this.aiTutorialRepository = aiTutorialRepository;
        this.globalAiLimitRepository = globalAiLimitRepository;
        this.userDailyAiLimitRepository = userDailyAiLimitRepository;
        this.seedDatabase();
    }

    private void seedDatabase() {
        log.warn("------- 🎲 Seeding TechBridge Data (Dev Profile) -----------");
        this.aiTutorialRepository.deleteAll();

        AiTutorialEntity whatsapp = new AiTutorialEntity();
        whatsapp.setId(UUID.fromString("33333333-bbbb-cccc-dddd-eeeeffff0001"));
        whatsapp.setTitle("Videollamada con familiares");
        whatsapp.setGeneralDescription("Quiero aprender a usar Zoom para hablar con mis nietos en el extranjero.");
        
        whatsapp.setSteps(List.of(
                new Step(1, "Abre la app", "Icono Azul"),
                new Step(2, "Elegir el botón de Entrar a una reunión", null),
                new Step(3, "Introducir el ID de la reunión", null),
                new Step(4, "Pulsar al botón de Entrar", null),
                new Step(5, "Felicidades ya estás dentro de la reunión", null)
        ));
        this.aiTutorialRepository.save(whatsapp);
        
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        GlobalAiLimitEntity globalAiLimit = GlobalAiLimitEntity
                .builder()
                .date(today)
                .totalCalls(1)
                .maxLimit(200)
                .build();
        this.globalAiLimitRepository.save(globalAiLimit);

        UserDailyAiLimitEntity userDailyAiLimitEntity = 
                UserDailyAiLimitEntity.builder()
                        .userId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                        .lastCallDate(LocalDate.of(2026,5,1))
                        .callsToday(0)
                        .maxLimit(5)
                        .build();
        this.userDailyAiLimitRepository.save(userDailyAiLimitEntity);
        log.warn("------- ✅ Seed Complete -----------");
    }
}
