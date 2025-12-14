package com.peerly.peerly_server.models.dto;

public class SendMessageRequest {

    private String senderId;
    private String content;
    private String type; 

    public SendMessageRequest() {
    }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

