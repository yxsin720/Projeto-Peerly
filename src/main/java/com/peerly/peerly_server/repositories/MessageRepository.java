package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findBySessionIdOrderByCreatedAtAsc(String sessionId);
}
