package com.example.timetracker.dto;

public record TokenResponse(
        String token,
        String tokenType
) {
    public static TokenResponse bearer(String token) {
        return new TokenResponse(token, "Bearer");
    }
}
