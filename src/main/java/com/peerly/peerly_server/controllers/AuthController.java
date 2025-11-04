package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.User;
import com.peerly.peerly_server.repositories.UserRepository;
import com.peerly.peerly_server.models.dto.LoginRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // POST /api/auth/login  (sem BCrypt, compara texto simples)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        if (body == null || body.getEmail() == null || body.getPassword() == null
                || body.getEmail().isBlank() || body.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email e password são obrigatórios."));
        }

        Optional<User> opt = userRepository.findByEmail(body.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciais inválidas."));
        }

        User user = opt.get();

        // ATENÇÃO: apenas para projeto iniciante/demonstrativo (sem hashing).
        if (!body.getPassword().equals(user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciais inválidas."));
        }

        // Monta resposta sem expor a password
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", user.getId());
        resp.put("email", user.getEmail());
        resp.put("fullName", user.getFullName());
        resp.put("role", user.getRole().name());
        resp.put("avatarUrl", user.getAvatarUrl());
        resp.put("language", user.getLanguage());

        return ResponseEntity.ok(resp);
    }
}
