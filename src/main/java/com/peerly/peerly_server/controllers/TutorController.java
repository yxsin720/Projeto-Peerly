// controllers/TutorController.java
package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.Tutor;
import com.peerly.peerly_server.repositories.TutorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tutors")
@CrossOrigin
public class TutorController {

  private final TutorRepository repo;
  public TutorController(TutorRepository repo){ this.repo = repo; }

  @PostMapping("/{id}/avatar")
  public ResponseEntity<?> uploadAvatar(
      @PathVariable String id,
      @RequestParam("file") MultipartFile file,
      HttpServletRequest req
  ) {
    Optional<Tutor> opt = repo.findById(id);
    if (opt.isEmpty()) return ResponseEntity.notFound().build();

    try {
  
      File dir = new File("uploads/avatars/tutors");
      dir.mkdirs();

      String ext = switch (file.getContentType()) {
        case "image/png" -> ".png";
        case "image/jpeg", "image/jpg" -> ".jpg";
        default -> ".bin";
      };
      String filename = id + ext;
      File dest = new File(dir, filename);
      file.transferTo(dest);

      Tutor t = opt.get();
     
      String rel = "/uploads/avatars/tutors/" + filename;
      t.setAvatarUrl(rel);
      repo.save(t);

     
      String base = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
      String absolute = base + rel;

      return ResponseEntity.ok(Map.of("avatarUrl", absolute));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
    }
  }
}
