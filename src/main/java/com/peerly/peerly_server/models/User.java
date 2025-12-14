package com.peerly.peerly_server.models;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
  @Id
  @Column(length = 36)
  private String id;

  @Column(nullable = false, unique = true, length = 254)
  private String email;

  @Column(name = "password_hash", columnDefinition = "TEXT")
  private String passwordHash;

  @Column(name = "full_name", nullable = false, length = 200)
  private String fullName;

  public enum Role { student, tutor, both, admin }

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Role role = Role.student;

  @Column(name = "avatar_url", columnDefinition = "TEXT")
  private String avatarUrl;

  @Column(nullable = false, length = 10)
  private String language = "pt";

  @Column(name = "area", length = 60)
  private String area;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private Instant updatedAt;

  @PrePersist
  public void prePersist() {
    if (id == null || id.isBlank()) id = UUID.randomUUID().toString();
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }
  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }
  public String getAvatarUrl() { return avatarUrl; }
  public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
  public String getLanguage() { return language; }
  public void setLanguage(String language) { this.language = language; }
  public String getArea() { return area; }
  public void setArea(String area) { this.area = area; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
