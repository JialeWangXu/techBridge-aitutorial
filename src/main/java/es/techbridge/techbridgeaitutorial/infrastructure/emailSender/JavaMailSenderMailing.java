package es.techbridge.techbridgeaitutorial.infrastructure.emailSender;

import es.techbridge.techbridgeaitutorial.application.port.out.mailing.EmailSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JavaMailSenderMailing implements EmailSender {

    private final JavaMailSender mailSender;

    public JavaMailSenderMailing(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendEmail(String emailTo, String subject, String content) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            log.info("Email de advertencia enviado a {}", emailTo);
        } catch (Exception e) {
            log.error("Error al enviar el email de aviso: {}", e.getMessage());
        }
    }
}
