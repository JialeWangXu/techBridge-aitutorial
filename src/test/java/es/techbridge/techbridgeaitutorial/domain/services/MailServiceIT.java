package es.techbridge.techbridgeaitutorial.domain.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "management.health.mail.enabled=false")
@Transactional
@ActiveProfiles("test")
class MailServiceIT {

    @Autowired
    private MailService mailService;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Test
    void sendWarningEmail() {
        this.mailService.sendWarningEmail(2);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(this.javaMailSender).send(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getTo()).containsExactly("jiale.wang@alumnos.upm.es");
        assertThat(messageCaptor.getValue().getSubject()).contains("Alerta de Consumo IA");
        assertThat(messageCaptor.getValue().getText()).contains("2 llamadas hoy");
    }
}
