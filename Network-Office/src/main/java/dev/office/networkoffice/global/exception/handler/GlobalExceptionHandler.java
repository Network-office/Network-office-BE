package dev.office.networkoffice.global.exception.handler;

import dev.office.networkoffice.global.exception.ExternalServiceException;
import dev.office.networkoffice.global.exception.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ExceptionResponse bodyData = ExceptionResponse.of(exception.getMessage());
        log.error("IllegalArgumentException: {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(bodyData);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException exception) {
        ExceptionResponse bodyData = ExceptionResponse.of(exception.getMessage());
        log.error("IllegalStateException: {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(bodyData);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ExceptionResponse> handleExternalServiceException(ExternalServiceException exception) {
        ExceptionResponse bodyData = ExceptionResponse.of(exception.getMessage());
        log.error("ExternalServiceException: {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(bodyData);
    }
}
