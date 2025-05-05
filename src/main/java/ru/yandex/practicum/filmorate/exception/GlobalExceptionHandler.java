package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.warn("Validation errors: {}", errors);
        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("errors", List.of(ex.getMessage())));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(Map.of("errors", List.of(ex.getMessage())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Constraint violation: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("errors", List.of(ex.getMessage())));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("Ошибка DuplicateKey: {}", e.getMessage());
        return buildErrorResponse("Like has already been set.", HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<ApiErrorResponse> buildErrorResponse(String errorMessage, HttpStatus status) {
        ApiErrorResponse response = new ApiErrorResponse(
                errorMessage,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(DataMappingException.class)
    public ResponseEntity<ApiErrorResponse> handleDataMappingException(DataMappingException ex) {
        log.error("Ошибка маппинга данных: {}", ex.getMessage(), ex);
        return buildErrorResponse("Ошибка при обработке данных фильма: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}