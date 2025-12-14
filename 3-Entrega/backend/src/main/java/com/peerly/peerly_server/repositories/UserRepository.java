package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

 
    List<User> findByRoleIn(Collection<User.Role> roles);
}
