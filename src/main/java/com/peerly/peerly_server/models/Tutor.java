// models/Tutor.java
package com.peerly.peerly_server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tutors")
public class Tutor {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;           // Mantém String conforme já estás a usar no backend

    @Column(nullable = false)
    private String name;

    @Column
    private String avatarUrl;    // URL HTTP ou file:// interno

    public Tutor() {}

    // -------- GETTERS --------
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    // -------- SETTERS --------
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
