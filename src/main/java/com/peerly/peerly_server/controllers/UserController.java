package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.User;
import com.peerly.peerly_server.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin // permite chamadas do app
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    // GET /api/users
    @GetMapping
    public List<User> getAll() {
        return repo.findAll();
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable String id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/users  (criar)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        try {
            String email = asString(body.get("email"));
            String fullName = asString(body.get("fullName"));
            if (email == null || email.isBlank() || fullName == null || fullName.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Campos obrigatórios: email, fullName"));
            }

            if (repo.findByEmail(email).isPresent()) {
                return ResponseEntity.status(409).body(Map.of("error", "Email já existe"));
            }

            User u = new User();
            u.setEmail(email);
            u.setFullName(fullName);
            u.setPasswordHash(asString(body.get("passwordHash")));
            u.setAvatarUrl(asString(body.get("avatarUrl")));
            u.setLanguage(defaultOr(asString(body.get("language")), "pt"));

            String roleStr = asString(body.get("role"));
            if (roleStr != null && !roleStr.isBlank()) {
                try {
                    u.setRole(User.Role.valueOf(roleStr));
                } catch (IllegalArgumentException ex) {
                    return ResponseEntity.badRequest().body(Map.of("error", "role inválido. Use: student | tutor | both | admin"));
                }
            }

            User saved = repo.save(u);
            return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", ex.getMessage()));
        }
    }

    // POST /api/users/{id}/update  (update parcial)
    @PostMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> body) {
        Optional<User> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        User u = opt.get();

        if (body.containsKey("email")) {
            String newEmail = asString(body.get("email"));
            if (newEmail == null || newEmail.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "email não pode ser vazio"));
            }
            repo.findByEmail(newEmail).filter(x -> !x.getId().equals(id)).ifPresent(x -> {
                throw new RuntimeException("Email já usado por outro utilizador");
            });
            u.setEmail(newEmail);
        }

        if (body.containsKey("fullName")) u.setFullName(asString(body.get("fullName")));
        if (body.containsKey("passwordHash")) u.setPasswordHash(asString(body.get("passwordHash")));
        if (body.containsKey("avatarUrl")) u.setAvatarUrl(asString(body.get("avatarUrl")));
        if (body.containsKey("language")) u.setLanguage(defaultOr(asString(body.get("language")), "pt"));

        if (body.containsKey("role")) {
            String roleStr = asString(body.get("role"));
            if (roleStr != null && !roleStr.isBlank()) {
                try {
                    u.setRole(User.Role.valueOf(roleStr));
                } catch (IllegalArgumentException ex) {
                    return ResponseEntity.badRequest().body(Map.of("error", "role inválido. Use: student | tutor | both | admin"));
                }
            }
        }

        try {
            User saved = repo.save(u);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(409).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", ex.getMessage()));
        }
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // >>>>> NOVO: POST /api/users/{id}/avatar (multipart) <<<<<
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        try {
            var opt = repo.findById(id);
            if (opt.isEmpty()) return ResponseEntity.notFound().build();
            if (file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "Ficheiro vazio."));

            // diretório base configurável (default: "uploads")
            String root = System.getProperty("peerly.upload-dir", "uploads");
            Path uploadRoot = Paths.get(root).toAbsolutePath().normalize();
            Path avatarsDir = uploadRoot.resolve("avatars");
            Files.createDirectories(avatarsDir);

            String original = file.getOriginalFilename();
            String ext = (original != null && original.contains(".")) ? original.substring(original.lastIndexOf('.')) : ".jpg";
            String filename = "avatar_" + id + "_" + System.currentTimeMillis() + ext;

            Path dest = avatarsDir.resolve(filename);
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

            // URL público (servido por /files/** – ver config abaixo)
            String publicUrl = String.format("%s://%s:%d/files/avatars/%s",
                    request.getScheme(), request.getServerName(), request.getServerPort(), filename);

            User u = opt.get();
            u.setAvatarUrl(publicUrl);
            repo.save(u);

            return ResponseEntity.ok(Map.of("avatarUrl", publicUrl));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    private static String asString(Object v) { return v == null ? null : String.valueOf(v); }
    private static String defaultOr(String v, String d) { return (v == null || v.isBlank()) ? d : v; }
}
