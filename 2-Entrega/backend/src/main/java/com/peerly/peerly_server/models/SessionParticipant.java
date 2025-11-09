package com.peerly.peerly_server.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "session_participants")
@IdClass(SessionParticipantKey.class)
public class SessionParticipant {
    @Id @Column(name = "session_id", length = 36) private String sessionId;
    @Id @Column(name = "user_id", length = 36) private String userId;

    @Column(name = "joined_at", insertable = false, updatable = false)
    private Instant joinedAt;

    // getters/setters
    public String getSessionId(){ return sessionId; } public void setSessionId(String s){ this.sessionId=s; }
    public String getUserId(){ return userId; } public void setUserId(String u){ this.userId=u; }
    public Instant getJoinedAt(){ return joinedAt; } public void setJoinedAt(Instant j){ this.joinedAt=j; }
}
