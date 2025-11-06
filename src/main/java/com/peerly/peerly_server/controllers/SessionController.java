package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.Session;
import com.peerly.peerly_server.models.Tutor;
import com.peerly.peerly_server.models.dto.CreateSessionRequest;
import com.peerly.peerly_server.models.dto.SessionDto;
import com.peerly.peerly_server.models.dto.TutorSlimDto;
import com.peerly.peerly_server.repositories.TutorRepository;
import com.peerly.peerly_server.services.SessionService;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sessions")
@CrossOrigin
public class SessionController {

  private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private final SessionService service;
  private final TutorRepository tutorRepo;

  public SessionController(SessionService service, TutorRepository tutorRepo) {
    this.service = service;
    this.tutorRepo = tutorRepo;
  }

  @PostMapping
  public SessionDto create(@RequestBody CreateSessionRequest data) {
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
    Tutor t = saved.getTutorId() != null ? tutorRepo.findById(saved.getTutorId()).orElse(null) : null;
    return toDto(saved, t);
  }

  @GetMapping
  public List<SessionDto> list() {
    List<Session> sessions = service.findAll();
    Set<String> tutorIds = sessions.stream()
        .map(Session::getTutorId).filter(Objects::nonNull).collect(Collectors.toSet());

    Map<String, Tutor> tutors = new HashMap<>();
    tutorRepo.findAllById(tutorIds).forEach(t -> tutors.put(t.getId(), t));

    return sessions.stream().map(s -> toDto(s, tutors.get(s.getTutorId()))).toList();
  }

  @GetMapping("/{id}")
  public SessionDto get(@PathVariable String id) {
    Session s = service.findById(id).orElseThrow();
    Tutor t = s.getTutorId() != null ? tutorRepo.findById(s.getTutorId()).orElse(null) : null;
    return toDto(s, t);
  }

  private SessionDto toDto(Session s, Tutor t) {
    SessionDto dto = new SessionDto();
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
    if (t != null) dto.setTutor(new TutorSlimDto(t.getId(), t.getName(), t.getAvatarUrl()));
    return dto;
  }
}
