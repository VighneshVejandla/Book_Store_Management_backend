//package com.cts.exceptions;
//
//import jakarta.validation.ConstraintViolationException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, Object> errors = new HashMap<>();
//
//        // Get field-specific errors
//        Map<String, String> fieldErrors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .collect(Collectors.toMap(
//                        FieldError::getField,
//                        FieldError::getDefaultMessage,
//                        (existing, replacement) -> existing // Keep first error if multiple
//                ));
//
//        errors.put("error", "Validation failed");
//        errors.put("fieldErrors", fieldErrors);
//
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Object> handleConstraintViolationException(
//            ConstraintViolationException ex) {
//
//        Map<String, Object> errors = new HashMap<>();
//        Map<String, String> fieldErrors = new HashMap<>();
//
//        ex.getConstraintViolations().forEach(violation -> {
//            String fieldName = violation.getPropertyPath().toString();
//            String errorMessage = violation.getMessage();
//            fieldErrors.put(fieldName, errorMessage);
//        });
//
//        errors.put("error", "Validation failed");
//        errors.put("fieldErrors", fieldErrors);
//
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
//        Map<String, Object> errors = new HashMap<>();
//        errors.put("error", "Validation failed");
//        errors.put("message", ex.getMessage());
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//
//
//}
//




package com.cts.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        // Get field-specific errors
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing // Keep first error if multiple
                ));

        errors.put("error", "Validation failed");
        errors.put("fieldErrors", fieldErrors);

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {

        Map<String, Object> errors = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        errors.put("error", "Validation failed");
        errors.put("fieldErrors", fieldErrors);

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", "Validation failed");
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handle WebClient exceptions from downstream services
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> handleWebClientResponseException(WebClientResponseException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        try {
            // Get the error response body from the service
            String responseBody = ex.getResponseBodyAsString();

            if (responseBody != null && !responseBody.isEmpty()) {
                try {
                    // Try to parse as JSON
                    JsonNode errorJson = objectMapper.readTree(responseBody);

                    // Handle structured error response from service (ErrorDetails format)
                    if (errorJson.has("message")) {
                        errorResponse.put("error", "Service Error");
                        errorResponse.put("message", errorJson.get("message").asText());

                        // Include additional fields from service response
                        if (errorJson.has("errorCode")) {
                            errorResponse.put("errorCode", errorJson.get("errorCode").asText());
                        }
                        if (errorJson.has("timeStamp")) {
                            errorResponse.put("timeStamp", errorJson.get("timeStamp").asText());
                        }
                        if (errorJson.has("path")) {
                            errorResponse.put("path", errorJson.get("path").asText());
                        }
                    }
                    // Handle validation errors (field-based errors)
                    else if (errorJson.isObject()) {
                        Map<String, String> fieldErrors = new HashMap<>();
                        boolean hasFieldErrors = false;

                        errorJson.fields().forEachRemaining(entry -> {
                            // Check if it's a field error (not a system field)
                            String key = entry.getKey();
                            if (!key.equals("timestamp") && !key.equals("status") && !key.equals("error")) {
                                fieldErrors.put(key, entry.getValue().asText());
                            }
                        });

                        if (!fieldErrors.isEmpty()) {
                            errorResponse.put("error", "Validation failed");
                            errorResponse.put("fieldErrors", fieldErrors);
                            hasFieldErrors = true;
                        }

                        // If no field errors found, treat as general error
                        if (!hasFieldErrors) {
                            errorResponse.put("error", "Service Error");
                            errorResponse.put("message", responseBody);
                        }
                    } else {
                        // Not a structured JSON response
                        errorResponse.put("error", "Service Error");
                        errorResponse.put("message", responseBody);
                    }
                } catch (Exception jsonParseException) {
                    // Not valid JSON, use raw response
                    errorResponse.put("error", "Service Error");
                    errorResponse.put("message", responseBody);
                }
            } else {
                // Empty response body
                errorResponse.put("error", "Service Error");
                errorResponse.put("message", "Service returned an error without details");
            }
        } catch (Exception parseException) {
            // Fallback error handling
            errorResponse.put("error", "Service Error");
            errorResponse.put("message", "Failed to process service error response");
        }

        // Map HTTP status codes appropriately
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handle generic RuntimeExceptions (including custom service exceptions)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Error");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Generic exception handler for any unhandled exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unexpected Error");
        errorResponse.put("message", "An unexpected error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
