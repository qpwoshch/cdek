package com.example.timetracker.service;

import com.example.timetracker.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final String authUsername;
    private final String authPassword;

    public AuthService(
            JwtService jwtService,
            @Value("${app.auth.username}") String authUsername,
            @Value("${app.auth.password}") String authPassword
    ) {
        this.jwtService = jwtService;
        this.authUsername = authUsername;
        this.authPassword = authPassword;
    }

    public String login(String username, String password) {
        if (!authUsername.equals(username) || !authPassword.equals(password)) {
            throw new UnauthorizedException("Invalid username or password");
        }
        return jwtService.generateToken(username);
    }
}
