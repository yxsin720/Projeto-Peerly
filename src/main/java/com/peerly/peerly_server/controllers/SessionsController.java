package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.Session;
import com.peerly.peerly_server.repositories.SessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionsController {

    private final SessionRepository sessionRepository;

    public SessionsController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // >>> ESTE É O GET que falta e resolve o 405 <<<
    @GetMapping
    public List<Session> listAll() {
        return sessionRepository.findAll();
    }

    // extra útil: GET /api/sessions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Session> getOne(@PathVariable String id) {
        return sessionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
