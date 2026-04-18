package com.example.timetracker.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        String message,
        Map<String, String> validationErrors
) {
    public static ApiErrorResponse of(String message) {
        return new ApiErrorResponse(LocalDateTime.now(), message, null);
    }

    public static ApiErrorResponse ofValidation(String message, Map<String, String> validationErrors) {
        return new ApiErrorResponse(LocalDateTime.now(), message, validationErrors);
    }
}
