package es.techbridge.techbridgeaitutorial.configurations;

import es.techbridge.techbridgeaitutorial.domain.exceptions.FailedCreateAiTutorialException;
import es.techbridge.techbridgeaitutorial.domain.exceptions.GlobalQuotaExceededException;
import es.techbridge.techbridgeaitutorial.domain.exceptions.UserQuotaExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerConfig {

    @ExceptionHandler(FailedCreateAiTutorialException.class)
    public ResponseEntity<Map<String, Object>> handleAiError(FailedCreateAiTutorialException ex) {
        return new ResponseEntity<>(generateErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GlobalQuotaExceededException.class)
    public  ResponseEntity<Map<String, Object>> handleGlobalLimiteError(GlobalQuotaExceededException ex){
        return new ResponseEntity<>(generateErrorResponse(ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserQuotaExceededException.class)
    public  ResponseEntity<Map<String, Object>> handleUserLimiteError(UserQuotaExceededException ex){
        return new ResponseEntity<>(generateErrorResponse(ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String,Object> generateErrorResponse(String message){
        Map<String,Object> response = new HashMap<>();
        response.put("timestamp",LocalDateTime.now());
        response.put("status",HttpStatus.INTERNAL_SERVER_ERROR);
        response.put("message",message);
        return response;
    }
}
