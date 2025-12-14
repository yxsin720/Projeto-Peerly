package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.User;
import com.peerly.peerly_server.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

  private final UserRepository repo;

  public UserController(UserRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public List<User> getAll() { return repo.findAll(); }

  @GetMapping("/{id}")
  public ResponseEntity<User> getOne(@PathVariable String id) {
    return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @Transactional
  public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
    String email        = asString(body.get("email"));
    String fullName     = asString(body.get("fullName"));
    String passwordHash = asString(body.get("passwordHash"));
    String avatarUrl    = asString(body.get("avatarUrl"));
    String language     = defaultOr(asString(body.get("language")), "pt");
    String roleStr      = asString(body.get("role"));
    String area         = asString(body.get("area"));

    if (isBlank(email) || isBlank(fullName) || isBlank(passwordHash)) {
      return badRequest("Campos obrigatórios: email, fullName, passwordHash");
    }
    if (repo.findByEmailIgnoreCase(email).isPresent()) {
      return conflict("Email já existe");
    }

    User u = new User();
    u.setEmail(email);
    u.setFullName(fullName);
    u.setPasswordHash(passwordHash);
    u.setAvatarUrl(avatarUrl);
    u.setLanguage(language);
    u.setArea(area);

    if (!isBlank(roleStr)) {
      try { u.setRole(User.Role.valueOf(roleStr)); }
      catch (IllegalArgumentException ex) { return badRequest("role inválido. Use: student | tutor | both | admin"); }
    }

    User saved = repo.save(u);
    return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
  }

  @PostMapping("/{id}/update")
  @Transactional
  public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> body) {
    Optional<User> opt = repo.findById(id);
    if (opt.isEmpty()) return ResponseEntity.notFound().build();

    User u = opt.get();

    if (body.containsKey("email")) {
      String newEmail = asString(body.get("email"));
      if (isBlank(newEmail)) return badRequest("email não pode ser vazio");
      var other = repo.findByEmailIgnoreCase(newEmail);
      if (other.isPresent() && !other.get().getId().equals(id)) return conflict("Email já usado por outro utilizador");
      u.setEmail(newEmail);
    }

    if (body.containsKey("fullName"))     u.setFullName(asString(body.get("fullName")));
    if (body.containsKey("passwordHash")) u.setPasswordHash(asString(body.get("passwordHash")));
    if (body.containsKey("avatarUrl"))    u.setAvatarUrl(asString(body.get("avatarUrl")));
    if (body.containsKey("language"))     u.setLanguage(defaultOr(asString(body.get("language")), "pt"));
    if (body.containsKey("area"))         u.setArea(asString(body.get("area")));

    if (body.containsKey("role")) {
      String roleStr = asString(body.get("role"));
      if (!isBlank(roleStr)) {
        try { u.setRole(User.Role.valueOf(roleStr)); }
        catch (IllegalArgumentException ex) { return badRequest("role inválido. Use: student | tutor | both | admin"); }
      }
    }

    return ResponseEntity.ok(repo.save(u));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<?> delete(@PathVariable String id) {
    if (!repo.existsById(id)) return ResponseEntity.notFound().build();
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Transactional
  public ResponseEntity<?> uploadAvatar(
      @PathVariable String id,
      @RequestParam("file") MultipartFile file,
      HttpServletRequest request
  ) {
    var opt = repo.findById(id);
    if (opt.isEmpty()) return ResponseEntity.notFound().build();
    if (file == null || file.isEmpty()) return badRequest("Ficheiro vazio.");

    String root = System.getProperty("peerly.upload-dir", "uploads");
    Path uploadRoot = Paths.get(root).toAbsolutePath().normalize();
    Path avatarsDir = uploadRoot.resolve("avatars");

    try {
      Files.createDirectories(avatarsDir);
      String ext = safeExt(file.getOriginalFilename());
      String filename = "avatar_" + id + "_" + System.currentTimeMillis() + ext;
      Path dest = avatarsDir.resolve(filename);
      Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
      String publicUrl = ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/files/avatars/")
          .path(filename)
          .toUriString();

      User u = opt.get();
      u.setAvatarUrl(publicUrl);
      repo.save(u);

      return ResponseEntity.ok(Map.of("avatarUrl", publicUrl));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
    }
  }

  private static String asString(Object v) { return v == null ? null : String.valueOf(v); }
  private static String defaultOr(String v, String d) { return isBlank(v) ? d : v; }
  private static boolean isBlank(String s) { return s == null || s.isBlank(); }

  private static String safeExt(String original) {
    if (original == null) return ".jpg";
    String lower = original.toLowerCase();
    if (lower.endsWith(".png"))  return ".png";
    if (lower.endsWith(".jpeg")) return ".jpeg";
    if (lower.endsWith(".jpg"))  return ".jpg";
    if (lower.endsWith(".webp")) return ".webp";
    return ".jpg";
  }

  private static ResponseEntity<Map<String, Object>> badRequest(String msg) {
    return ResponseEntity.badRequest().body(Map.of("error", msg));
  }

  private static ResponseEntity<Map<String, Object>> conflict(String msg) {
    return ResponseEntity.status(409).body(Map.of("error", msg));
  }
}
