package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.Session;
import org.springframework.data.repository.ListCrudRepository;

public interface SessionRepository extends ListCrudRepository<Session, String> {
}
