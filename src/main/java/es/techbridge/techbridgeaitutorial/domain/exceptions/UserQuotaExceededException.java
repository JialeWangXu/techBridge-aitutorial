package es.techbridge.techbridgeaitutorial.domain.exceptions;

public class UserQuotaExceededException extends RuntimeException {
    public UserQuotaExceededException(String message) {
        super(message);
    }
}
