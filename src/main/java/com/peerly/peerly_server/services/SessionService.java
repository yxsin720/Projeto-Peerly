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

    public Session create(Session s) {
        return repo.save(s);
    }

    public List<Session> findAll() {
        return repo.findAll();
    }

    public Optional<Session> findById(String id) {
        return repo.findById(id);
    }

    public List<Session> findByTutorId(String tutorId) {
        return repo.findByTutorId(tutorId);
    }

    public List<Session> findByStudentId(String studentId) {
        return repo.findByStudentId(studentId);
    }

    public Session updateStatus(String id, String status) {
        Session s = repo.findById(id).orElseThrow();
        s.setStatus(status);
        return repo.save(s);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
