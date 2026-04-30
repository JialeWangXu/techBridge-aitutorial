package es.techbridge.techbridgeaitutorial.domain.services;

import es.techbridge.techbridgeaitutorial.domain.exceptions.FailedCreateAiTutorialException;
import es.techbridge.techbridgeaitutorial.domain.model.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.CreateAiTutorialDto;
import es.techbridge.techbridgeaitutorial.domain.model.Step;
import es.techbridge.techbridgeaitutorial.domain.webclients.HelpRequestWebClient;
import es.techbridge.techbridgeaitutorial.domain.webclients.UserWebClient;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.GlobalAiLimitEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.UserDailyAiLimitEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.AiTutorialRepository;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.GlobalAiLimitRepository;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.UserDailyAiLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "management.health.mail.enabled=false")
@Transactional
@ActiveProfiles("test")
class AiTutorialServiceIT {

    private static final UUID SENIOR_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID HELP_REQUEST_ID = UUID.fromString("11111111-2222-3333-4444-555566660001");
    private static final String SENIOR_EMAIL = "manolo@gmail.com";

    @Autowired
    private AiTutorialService aiTutorialService;

    @Autowired
    private AiTutorialRepository aiTutorialRepository;

    @Autowired
    private GlobalAiLimitRepository globalAiLimitRepository;

    @Autowired
    private UserDailyAiLimitRepository userDailyAiLimitRepository;

    @Autowired
    private ChatClient.Builder builder;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Autowired
    private ChatClient.CallResponseSpec callResponseSpec;

    @MockitoBean
    private HelpRequestWebClient helpRequestWebClient;

    @MockitoBean
    private UserWebClient userWebClient;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        Mockito.reset(this.chatClient, this.requestSpec, this.callResponseSpec, this.helpRequestWebClient, this.userWebClient);
        BDDMockito.given(this.chatClient.prompt())
                .willReturn(this.requestSpec);
        BDDMockito.given(this.requestSpec.system(any(Consumer.class)))
                .willReturn(this.requestSpec);
        BDDMockito.given(this.requestSpec.call())
                .willReturn(this.callResponseSpec);
        BDDMockito.given(this.userWebClient.getIdByEmail(SENIOR_EMAIL))
                .willReturn(SENIOR_ID);
    }

    @TestConfiguration
    static class ChatClientTestConfiguration {

        @Bean
        @Primary
        ChatClient chatClient() {
            return Mockito.mock(ChatClient.class);
        }

        @Bean
        @Primary
        ChatClient.Builder testChatClientBuilder(ChatClient chatClient) {
            ChatClient.Builder builder = Mockito.mock(ChatClient.Builder.class);
            BDDMockito.given(builder.build()).willReturn(chatClient);
            return builder;
        }

        @Bean
        ChatClient.ChatClientRequestSpec requestSpec() {
            return Mockito.mock(ChatClient.ChatClientRequestSpec.class);
        }

        @Bean
        ChatClient.CallResponseSpec callResponseSpec() {
            return Mockito.mock(ChatClient.CallResponseSpec.class);
        }
    }

    @Test
    void create() {
        AiTutorial aiTutorial = AiTutorial.builder()
                .title("Enviar fotos")
                .generalDescription("Guia para WhatsApp")
                .steps(List.of(
                        new Step(1, "Abre la app", "Icono verde"),
                        new Step(2, "Selecciona chat", null)
                ))
                .build();
        BDDMockito.given(this.callResponseSpec.entity(AiTutorial.class))
                .willReturn(aiTutorial);
        CreateAiTutorialDto createAiTutorialDto = CreateAiTutorialDto.builder()
                .title("Enviar fotos")
                .description("Guia para WhatsApp")
                .helpRequestId(HELP_REQUEST_ID)
                .build();

        AiTutorial result = this.aiTutorialService.create(SENIOR_EMAIL, createAiTutorialDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Enviar fotos");
        assertThat(result.getGeneralDescription()).isEqualTo("Guia para WhatsApp");
        assertThat(result.getSteps()).hasSize(2);
        assertThat(this.aiTutorialRepository.findById(result.getId()))
                .get()
                .satisfies(entity -> {
                    assertThat(entity.getTitle()).isEqualTo("Enviar fotos");
                    assertThat(entity.getGeneralDescription()).isEqualTo("Guia para WhatsApp");
                    assertThat(entity.getSteps()).hasSize(2);
                });

        ArgumentCaptor<UUID> aiTutorialIdCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(this.helpRequestWebClient).saveAiTutorialId(BDDMockito.eq(HELP_REQUEST_ID), aiTutorialIdCaptor.capture());
        assertThat(aiTutorialIdCaptor.getValue()).isEqualTo(result.getId());

        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        assertThat(this.globalAiLimitRepository.findByDate(today))
                .get()
                .extracting(GlobalAiLimitEntity::getTotalCalls)
                .isEqualTo(2);
        assertThat(this.userDailyAiLimitRepository.findByUserId(SENIOR_ID))
                .get()
                .extracting(UserDailyAiLimitEntity::getCallsToday)
                .isEqualTo(1);
    }

    @Test
    void createWhenChatClientReturnsNull_thenThrowFailedCreateAiTutorialException() {
        BDDMockito.given(this.callResponseSpec.entity(AiTutorial.class))
                .willReturn(null);
        CreateAiTutorialDto createAiTutorialDto = CreateAiTutorialDto.builder()
                .title("Enviar fotos")
                .description("Guia para WhatsApp")
                .helpRequestId(HELP_REQUEST_ID)
                .build();

        assertThatThrownBy(() -> this.aiTutorialService.create(SENIOR_EMAIL, createAiTutorialDto))
                .isInstanceOf(FailedCreateAiTutorialException.class)
                .hasMessageContaining(HELP_REQUEST_ID.toString());

        verify(this.helpRequestWebClient, never()).saveAiTutorialId(any(UUID.class), any(UUID.class));
    }
}
