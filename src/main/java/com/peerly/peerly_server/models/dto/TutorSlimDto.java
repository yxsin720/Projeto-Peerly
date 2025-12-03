package com.peerly.peerly_server.models.dto;

public class TutorSlimDto {
  public String id;
  public String name;
  public String avatarUrl;
  public TutorSlimDto() {}
  public TutorSlimDto(String id, String name, String avatarUrl) {
    this.id = id; this.name = name; this.avatarUrl = avatarUrl;
  }
}
