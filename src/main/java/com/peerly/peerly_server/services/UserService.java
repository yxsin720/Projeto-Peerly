package com.peerly.peerly_server.services;

import com.peerly.peerly_server.models.User;
import com.peerly.peerly_server.models.dto.LoginRequest;
import com.peerly.peerly_server.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) { this.repo = repo; }

    public List<User> findAll() { return repo.findAll(); }

    public Optional<User> findById(String id) { return repo.findById(id); }

    public User create(User u) {
        if (u.getId() == null || u.getId().isBlank()) u.setId(UUID.randomUUID().toString());
        return repo.save(u);
    }

    public User update(String id, User patch) {
        User u = repo.findById(id).orElseThrow();
        if (patch.getFullName() != null) u.setFullName(patch.getFullName());
        if (patch.getAvatarUrl() != null) u.setAvatarUrl(patch.getAvatarUrl());
        return repo.save(u);
    }

    public void delete(String id) { repo.deleteById(id); }

    public Optional<User> authenticate(LoginRequest req) {
        if (req == null || req.getEmail() == null || req.getPassword() == null) return Optional.empty();

        Optional<User> found = repo.findByEmailIgnoreCase(req.getEmail());
        if (found.isEmpty()) found = repo.findByEmail(req.getEmail());

        final String plain = req.getPassword();
        return found.filter(u -> {
            String stored = u.getPasswordHash();
            return stored != null && stored.equals(plain); 
        });
    }
}
