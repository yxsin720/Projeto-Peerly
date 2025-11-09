package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.Session;
import com.peerly.peerly_server.models.SessionParticipant;
import com.peerly.peerly_server.models.dto.CreateSessionRequest;
import com.peerly.peerly_server.models.dto.SessionDto;
import com.peerly.peerly_server.models.dto.TutorSlimDto;
import com.peerly.peerly_server.repositories.SessionParticipantRepository;
import com.peerly.peerly_server.repositories.TutorRepository;
import com.peerly.peerly_server.services.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin
public class SessionController {

  private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private final SessionService service;
  private final TutorRepository tutorRepo;
  private final SessionParticipantRepository participants;

  public SessionController(SessionService service,
                           TutorRepository tutorRepo,
                           SessionParticipantRepository participants) {
    this.service = service;
    this.tutorRepo = tutorRepo;
    this.participants = participants;
  }

  @PostMapping
  @Transactional
  public ResponseEntity<?> create(@RequestBody CreateSessionRequest data) {
    if (data == null
        || data.tutorId() == null || data.tutorId().isBlank()
        || data.title() == null || data.title().isBlank()
        || data.startsAt() == null
        || data.endsAt() == null) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Campos obrigatórios: tutorId, title, startsAt, endsAt (LocalDateTime)."
      ));
    }
    if (data.endsAt().isBefore(data.startsAt())) {
      return ResponseEntity.badRequest().body(Map.of("error", "endsAt deve ser depois de startsAt"));
    }

    
    Session s = new Session();
    s.setId(UUID.randomUUID().toString());
    s.setTutorId(data.tutorId());
    s.setSubjectId(data.subjectId());
    s.setTitle(data.title());
    s.setDescription(data.description());
    s.setStartsAt(data.startsAt());
    s.setEndsAt(data.endsAt());
    s.setMaxParticipants(data.maxParticipants() != null ? data.maxParticipants() : 1);
    s.setPriceTotalCents(data.priceTotalCents());
    s.setStatus("scheduled");

    Session saved = service.create(s);

   
    if (data.studentId() != null && !data.studentId().isBlank()) {
      if (!participants.existsBySessionIdAndUserId(saved.getId(), data.studentId())) {
        SessionParticipant sp = new SessionParticipant();
        sp.setSessionId(saved.getId());
        sp.setUserId(data.studentId());
        participants.save(sp);
      }
    }

    return ResponseEntity.ok(toDto(saved));
  }

  @GetMapping
  public List<SessionDto> list() {
    List<Session> sessions = service.findAll();
    return sessions.stream().map(this::toDto).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable String id) {
    return service.findById(id)
        .<ResponseEntity<?>>map(s -> ResponseEntity.ok(toDto(s)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  
  private SessionDto toDto(Session s) {
    var dto = new SessionDto();
    dto.setId(s.getId());
    dto.setTutorId(s.getTutorId());
    dto.setSubjectId(s.getSubjectId());
    dto.setTitle(s.getTitle());
    dto.setDescription(s.getDescription());
    dto.setStartsAt(s.getStartsAt() != null ? s.getStartsAt().format(ISO) : null);
    dto.setEndsAt(s.getEndsAt() != null ? s.getEndsAt().format(ISO) : null);
    dto.setCreatedAt(s.getCreatedAt() != null ? s.getCreatedAt().format(ISO) : null);
    dto.setStatus(s.getStatus());
    dto.setMaxParticipants(s.getMaxParticipants());
    dto.setPriceTotalCents(s.getPriceTotalCents());

    if (s.getTutorId() != null) {
      tutorRepo.findById(s.getTutorId()).ifPresent(t ->
          dto.setTutor(new TutorSlimDto(t.getId(), t.getName(), t.getAvatarUrl())));
    }
    return dto;
  }
}
