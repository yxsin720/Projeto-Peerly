package com.peerly.peerly_server.models.dto;

public class LoginResponse {
  private String token;
  private String userId;
  private String email;
  private String fullName;

  public LoginResponse(String token, String userId, String email, String fullName) {
    this.token = token;
    this.userId = userId;
    this.email = email;
    this.fullName = fullName;
  }

  public String getToken() { return token; }
  public String getUserId() { return userId; }
  public String getEmail() { return email; }
  public String getFullName() { return fullName; }
}
