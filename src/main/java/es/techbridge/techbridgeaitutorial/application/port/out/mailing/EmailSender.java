package es.techbridge.techbridgeaitutorial.application.port.out.mailing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

public interface EmailSender {

    @Value("${app.ai.limits.admin-email}")
    @Async
    void sendEmail(String emailTo,String subject,String content);
}
