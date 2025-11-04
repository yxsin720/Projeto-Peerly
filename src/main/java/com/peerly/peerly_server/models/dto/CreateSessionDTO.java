package com.peerly.peerly_server.models.dto;

public class CreateSessionDTO {
    private String tutorId;
    private String subjectId; // opcional
    private String title;
    private String description;
    private String startsAtUtc; // ex: 2025-11-01T14:00:00Z
    private String endsAtUtc;   // ex: 2025-11-01T15:00:00Z
    private Integer maxParticipants;

    public String getTutorId() { return tutorId; }
    public void setTutorId(String tutorId) { this.tutorId = tutorId; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStartsAtUtc() { return startsAtUtc; }
    public void setStartsAtUtc(String startsAtUtc) { this.startsAtUtc = startsAtUtc; }
    public String getEndsAtUtc() { return endsAtUtc; }
    public void setEndsAtUtc(String endsAtUtc) { this.endsAtUtc = endsAtUtc; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
}
