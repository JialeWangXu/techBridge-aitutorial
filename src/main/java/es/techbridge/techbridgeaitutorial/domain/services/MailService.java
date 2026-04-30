package es.techbridge.techbridgeaitutorial.domain.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.ai.limits.admin-email}")
    private String adminEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWarningEmail(int currentCalls) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject("⚠️ Alerta de Consumo IA - TechBridge");
            message.setText("El sistema ha alcanzado " + currentCalls +
                    " llamadas hoy. Revisa el consumo en Google Cloud.");

            mailSender.send(message);
            log.info("Email de advertencia enviado a {}", adminEmail);
        } catch (Exception e) {
            log.error("Error al enviar el email de aviso: {}", e.getMessage());
        }
    }
}
