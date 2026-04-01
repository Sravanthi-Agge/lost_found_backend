package com.lostfound.controller;

import com.lostfound.dto.AuthResponse;
import com.lostfound.dto.LoginRequest;
import com.lostfound.dto.RegisterRequest;
import com.lostfound.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            System.out.println("Registration attempt for: " + request.getEmail());
            AuthResponse response = authService.register(request);
            System.out.println("Registration successful for: " + request.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("Login attempt for: " + request.getEmail());
            AuthResponse response = authService.login(request);
            System.out.println("Login successful for: " + request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
