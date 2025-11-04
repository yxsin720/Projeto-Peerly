package com.peerly.peerly_server.models.dto;

public class RegisterRequest {
  public String email;
  public String fullName;
  public String password; // vem em texto simples
  public String role;     // opcional: "student", "tutor", ...
}
