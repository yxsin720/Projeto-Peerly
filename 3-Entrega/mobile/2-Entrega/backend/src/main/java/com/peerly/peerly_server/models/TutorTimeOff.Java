package com.peerly.peerly_server.models;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity @Table(name="tutor_time_off")
public class TutorTimeOff {
  @Id @Column(length=36) private String id;
  @Column(name="tutor_id", length=36, nullable=false) private String tutorId;
  @Column(name="start_at", nullable=false) private OffsetDateTime startAt; 
  @Column(name="end_at", nullable=false) private OffsetDateTime endAt;     
  @Column(length=255) private String reason;

}
