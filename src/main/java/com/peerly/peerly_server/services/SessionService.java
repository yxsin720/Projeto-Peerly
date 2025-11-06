package com.peerly.peerly_server.services;

import com.peerly.peerly_server.models.Session;
import com.peerly.peerly_server.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository repo;

    public SessionService(SessionRepository repo) {
        this.repo = repo;
    }

    public Session create(Session session) {
        return repo.save(session);
    }

    public List<Session> findAll() {
        return repo.findAll();
    }

    public Optional<Session> findById(String id) {
        return repo.findById(id);
    }
}
