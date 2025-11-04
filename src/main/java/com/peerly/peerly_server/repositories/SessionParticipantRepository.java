package com.peerly.peerly_server.repositories;

import com.peerly.peerly_server.models.SessionParticipant;
import com.peerly.peerly_server.models.SessionParticipantKey;
import org.springframework.data.repository.CrudRepository;

public interface SessionParticipantRepository extends CrudRepository<SessionParticipant, SessionParticipantKey> {
    boolean existsBySessionIdAndUserId(String sessionId, String userId);
    long countBySessionId(String sessionId);
}
