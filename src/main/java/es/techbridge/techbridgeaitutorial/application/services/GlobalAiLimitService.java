package es.techbridge.techbridgeaitutorial.application.services;

import es.techbridge.techbridgeaitutorial.application.port.in.GlobalAiLimitUseCases;
import es.techbridge.techbridgeaitutorial.application.port.in.MailUseCases;
import es.techbridge.techbridgeaitutorial.domain.exceptions.GlobalQuotaExceededException;
import es.techbridge.techbridgeaitutorial.domain.model.GlobalAiLimit;
import es.techbridge.techbridgeaitutorial.application.port.out.persistence.GlobalAiLimitPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class GlobalAiLimitService implements GlobalAiLimitUseCases {

    private final GlobalAiLimitPersistence globalAiLimitPersistence;
    private final MailUseCases mailService;
    @Value("${app.ai.limits.global-warning}")
    private int globalWarningThreshold;

    @Autowired
    public GlobalAiLimitService(GlobalAiLimitPersistence globalAiLimitPersistence, MailService mailService) {
        this.globalAiLimitPersistence = globalAiLimitPersistence;
        this.mailService = mailService;
    }

    public GlobalAiLimit getByDate(LocalDate date){
        return this.globalAiLimitPersistence.getByDate(date).toGlobalAiLimit();
    }

    public void checkGlobalAiLimit(){
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        GlobalAiLimit todayLimit = this.getByDate(today);
        if (todayLimit.getTotalCalls() >= todayLimit.getMaxLimit()) {
            throw new GlobalQuotaExceededException("Cupo del sistema agotado");
        }else if (todayLimit.getTotalCalls()== globalWarningThreshold) {
            this.mailService.sendWarningEmail(todayLimit.getTotalCalls());
        }
    }

    public void incrementGlobalTotalCalls(){
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        this.globalAiLimitPersistence.incrementTotalCalls(today);
    }
}
