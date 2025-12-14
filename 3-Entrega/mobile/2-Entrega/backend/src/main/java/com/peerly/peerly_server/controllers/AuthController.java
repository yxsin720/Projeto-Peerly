package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.User;
import com.peerly.peerly_server.models.dto.LoginRequest;
import com.peerly.peerly_server.models.dto.UserPublicDto;
import com.peerly.peerly_server.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

  private final UserRepository userRepository;

  public AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest body) {
    if (body == null || body.getEmail() == null || body.getPassword() == null
        || body.getEmail().isBlank() || body.getPassword().isBlank()) {
      return ResponseEntity.badRequest().body(java.util.Map.of("error", "Email e password são obrigatórios."));
    }

    final String email = body.getEmail().trim();
    final String password = body.getPassword().trim();

    Optional<User> opt = userRepository.findByEmailIgnoreCase(email);
    if (opt.isEmpty()) return unauthorized();

    User user = opt.get();
    String stored = user.getPasswordHash();
    if (stored == null || !stored.equals(password)) return unauthorized();

   
    UserPublicDto dto = new UserPublicDto(
        user.getId(),
        user.getEmail(),
        user.getFullName(),
        user.getRole() != null ? user.getRole().name() : "student",
        user.getAvatarUrl()
    );
    return ResponseEntity.ok(dto);
  }

  private static ResponseEntity<?> unauthorized() {
    return ResponseEntity.status(401).body(java.util.Map.of("error", "Credenciais inválidas."));
  }
}
