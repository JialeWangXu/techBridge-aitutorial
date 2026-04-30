package es.techbridge.techbridgeaitutorial.domain.exceptions;

public class GlobalQuotaExceededException extends RuntimeException {
    public GlobalQuotaExceededException(String message) {
        super(message);
    }
}
