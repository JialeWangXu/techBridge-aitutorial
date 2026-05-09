package es.techbridge.techbridgeaitutorial.application.port.in;

import org.springframework.scheduling.annotation.Async;

public interface MailUseCases {
    @Async
    void sendWarningEmail(int currentCalls);
}
