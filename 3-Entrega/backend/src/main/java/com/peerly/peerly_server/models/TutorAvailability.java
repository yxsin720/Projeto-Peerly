package com.peerly.peerly_server.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tutor_availability")
public class TutorAvailability {
    @Id @Column(length = 36) private String id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tutor_id", nullable = false)
    private User tutor;

    @Column(nullable = false) private int weekday; 
    @Column(name = "start_time", nullable = false) private java.time.LocalTime startTime;
    @Column(name = "end_time", nullable = false) private java.time.LocalTime endTime;
    @Column(nullable = false, length = 64) private String timezone = "UTC";

    @PrePersist public void pre(){ if (id == null || id.isBlank()) id = UUID.randomUUID().toString(); }
    // getters/setters
    public String getId(){ return id; } public void setId(String id){ this.id=id; }
    public User getTutor(){ return tutor; } public void setTutor(User tutor){ this.tutor=tutor; }
    public int getWeekday(){ return weekday; } public void setWeekday(int weekday){ this.weekday=weekday; }
    public java.time.LocalTime getStartTime(){ return startTime; } public void setStartTime(java.time.LocalTime t){ this.startTime=t; }
    public java.time.LocalTime getEndTime(){ return endTime; } public void setEndTime(java.time.LocalTime t){ this.endTime=t; }
    public String getTimezone(){ return timezone; } public void setTimezone(String tz){ this.timezone=tz; }
}
