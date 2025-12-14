package com.peerly.peerly_server.models.dto;

import com.peerly.peerly_server.models.Message;

import java.time.format.DateTimeFormatter;

public class MessageDto {

    private String id;
    private String sessionId;
    private String senderId;
    private String type;
    private String content;
    private String createdAt;

    public static MessageDto fromEntity(Message m) {
        MessageDto dto = new MessageDto();
        dto.id = m.getId();
        dto.sessionId = m.getSessionId();
        dto.senderId = m.getSenderId();
        dto.type = m.getType();
        dto.content = m.getContent();
        dto.createdAt = m.getCreatedAt() != null
                ? m.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null;
        return dto;
    }

 

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
