package com.peerly.peerly_server.models.dto;

public class StudentSlimDto {
    private String id;
    private String name;
    private String avatarUrl;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAvatarUrl() { return avatarUrl; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
