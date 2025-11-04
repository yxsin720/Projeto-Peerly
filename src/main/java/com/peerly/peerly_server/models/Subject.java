package com.peerly.peerly_server.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id @Column(length = 36) private String id;
    @Column(nullable = false, unique = true, length = 120) private String slug;
    @Column(nullable = false, length = 120) private String name;

    @PrePersist public void pre() { if (id == null || id.isBlank()) id = UUID.randomUUID().toString(); }
    public String getId() { return id; } public void setId(String id){ this.id=id; }
    public String getSlug(){ return slug; } public void setSlug(String slug){ this.slug=slug; }
    public String getName(){ return name; } public void setName(String name){ this.name=name; }
}
