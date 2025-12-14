package com.peerly.peerly_server.models.dto;

public class UserPublicDto {
  public String id;
  public String email;
  public String fullName;
  public String role;
  public String avatarUrl;

  public UserPublicDto(String id, String email, String fullName, String role, String avatarUrl) {
    this.id = id;
    this.email = email;
    this.fullName = fullName;
    this.role = role;
    this.avatarUrl = avatarUrl;
  }
}
