package es.techbridge.techbridgeaitutorial.infrastructure.resource;

import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.AiTutorial;
import es.techbridge.techbridgeaitutorial.domain.model.aiTutorial.Step;
import es.techbridge.techbridgeaitutorial.application.port.out.webclients.HelpRequestWebClient;
import es.techbridge.techbridgeaitutorial.application.port.out.webclients.UserWebClient;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.entities.AiTutorialEntity;
import es.techbridge.techbridgeaitutorial.infrastructure.postgresql.repositories.AiTutorialRepository;
import es.techbridge.techbridgeaitutorial.infrastructure.resources.AiTutorialResource;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "management.health.mail.enabled=false")
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AiTutorialResourceIT {

    private static final UUID SENIOR_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID HELP_REQUEST_ID = UUID.fromString("11111111-2222-3333-4444-555566660001");
    private static final String SENIOR_EMAIL = "manolo@gmail.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AiTutorialRepository aiTutorialRepository;

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
        BDDMockito.given(this.callResponseSpec.entity(AiTutorial.class))
                .willReturn(AiTutorial.builder()
                        .title("Enviar fotos")
                        .generalDescription("Guia para WhatsApp")
                        .steps(List.of(
                                new Step(1, "Abre la app", "Icono verde"),
                                new Step(2, "Selecciona chat", null)
                        ))
                        .build());
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
    void whenCreateAiTutorialAsSenior_thenReturns200() throws Exception {
        String jsonBody = """
            {
              "title": "Enviar fotos",
              "description": "Guia para WhatsApp",
              "helpRequestId": "11111111-2222-3333-4444-555566660001"
            }
            """;

        this.mockMvc.perform(post(AiTutorialResource.AITUTORIAL)
                        .with(jwt().jwt(jwt -> jwt.subject(SENIOR_EMAIL))
                                .authorities(() -> "ROLE_SENIOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Enviar fotos"))
                .andExpect(jsonPath("$.generalDescription").value("Guia para WhatsApp"))
                .andExpect(jsonPath("$.steps.length()").value(2));

        List<AiTutorialEntity> createdTutorials = this.aiTutorialRepository.findAll().stream()
                .filter(aiTutorialEntity -> aiTutorialEntity.getTitle().equals("Enviar fotos"))
                .toList();
        assertThat(createdTutorials).hasSize(1);
    }

    @Test
    void whenCreateAiTutorialWithoutSeniorRole_thenReturns403() throws Exception {
        String jsonBody = """
            {
              "title": "Enviar fotos",
              "description": "Guia para WhatsApp",
              "helpRequestId": "11111111-2222-3333-4444-555566660001"
            }
            """;

        this.mockMvc.perform(post(AiTutorialResource.AITUTORIAL)
                        .with(jwt().jwt(jwt -> jwt.subject(SENIOR_EMAIL))
                                .authorities(() -> "ROLE_VOLUNTEER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenGetAiLimit_thenReturnResult() throws Exception {
        this.mockMvc.perform(get(AiTutorialResource.AITUTORIAL+AiTutorialResource.CHECK)
                .with(jwt().jwt(jwt-> jwt.subject(SENIOR_EMAIL))
                        .authorities(() -> "ROLE_SENIOR"))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.globalLimitReached").value(false));

    }
}
