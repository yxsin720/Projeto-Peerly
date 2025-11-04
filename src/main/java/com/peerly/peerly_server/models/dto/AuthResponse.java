package com.peerly.peerly_server.models.dto;

public class AuthResponse {
    private String id;
    private String email;
    private String fullName;
    private String role;
    private String language;
    private String avatarUrl;

    public AuthResponse() {}
    public AuthResponse(String id, String email, String fullName, String role, String language, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.language = language;
        this.avatarUrl = avatarUrl;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getLanguage() { return language; }
    public String getAvatarUrl() { return avatarUrl; }

    public void setId(String id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(String role) { this.role = role; }
    public void setLanguage(String language) { this.language = language; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}

 
    

