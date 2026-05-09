package es.techbridge.techbridgeaitutorial.application.services;

import es.techbridge.techbridgeaitutorial.application.port.in.MailUseCases;
import es.techbridge.techbridgeaitutorial.application.port.out.mailing.EmailSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MailService implements MailUseCases {

    private final EmailSender mailSender;

    @Value("${app.ai.limits.admin-email}")
    private String adminEmail;

    public MailService(EmailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWarningEmail(int currentCalls) {
        this.mailSender.sendEmail(adminEmail,
                "⚠️ Alerta de Consumo IA - TechBridge",
                "El sistema ha alcanzado " + currentCalls +
                        " llamadas hoy. Revisa el consumo en Google Cloud.");
    }
}
