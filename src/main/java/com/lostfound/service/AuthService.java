package com.lostfound.service;

import com.lostfound.dto.AuthResponse;
import com.lostfound.dto.LoginRequest;
import com.lostfound.dto.RegisterRequest;
import com.lostfound.model.User;
import com.lostfound.repository.UserRepository;
import com.lostfound.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        System.out.println("Registering user: " + request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            System.out.println("Email already exists: " + request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Plain text for now
        user.setRole(User.Role.USER);

        user = userRepository.save(user);
        System.out.println("User saved with ID: " + user.getId());

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        System.out.println("Login attempt for: " + request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Simple password check (plain text for now)
        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
