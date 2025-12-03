package com.peerly.peerly_server.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
    public String email;
    public String passwordHash;
    public String fullName;
    public String role;       
    public String avatarUrl;
    public String language;   
}
