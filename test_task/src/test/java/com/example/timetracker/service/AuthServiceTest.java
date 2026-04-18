package com.example.timetracker.service;

import com.example.timetracker.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(jwtService, "student", "student123");
    }

    @Test
    void loginShouldReturnTokenForCorrectCredentials() {
        when(jwtService.generateToken("student")).thenReturn("test-token");

        String token = authService.login("student", "student123");

        assertEquals("test-token", token);
    }

    @Test
    void loginShouldThrowForIncorrectCredentials() {
        assertThrows(UnauthorizedException.class, () -> authService.login("student", "wrong-password"));
    }
}
