package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.Session;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SessionRepository extends ListCrudRepository<Session, String> {
    List<Session> findByTutorId(String tutorId);
    List<Session> findByStudentId(String studentId);
}
