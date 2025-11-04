// models/Tutor.java
package com.peerly.peerly_server.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity @Table(name="tutors")
public class Tutor {
  @Id
  private String id = UUID.randomUUID().toString();

  @Column(nullable=false, length=120)
  private String name;

  @Column(nullable=false, length=80)
  private String subject;

  @Column(name="avatar_url", columnDefinition="TEXT")
  private String avatarUrl; // ex: /uploads/avatars/tutors/t1.jpg

  public void setAvatarUrl(String rel) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setAvatarUrl'");
  }

  // getters/setters...
}
