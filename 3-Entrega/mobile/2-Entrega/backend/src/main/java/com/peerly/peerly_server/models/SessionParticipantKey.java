package com.peerly.peerly_server.models;

import java.io.Serializable;
import java.util.Objects;

public class SessionParticipantKey implements Serializable {
    private String sessionId;
    private String userId;
    public SessionParticipantKey() {}
    public SessionParticipantKey(String s, String u){ this.sessionId=s; this.userId=u; }
    @Override public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof SessionParticipantKey)) return false;
        SessionParticipantKey that = (SessionParticipantKey) o;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(userId, that.userId);
    }
    @Override public int hashCode(){ return Objects.hash(sessionId, userId); }
}
