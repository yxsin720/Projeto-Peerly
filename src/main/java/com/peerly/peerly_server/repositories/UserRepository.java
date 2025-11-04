package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // Busca exata
    Optional<User> findByEmail(String email);

    // Busca que ignora maiúsculas/minúsculas (para login)
    Optional<User> findByEmailIgnoreCase(String email);

    // Filtra por múltiplos papéis (student, tutor, both, admin)
    List<User> findByRoleIn(Collection<User.Role> roles);
}
