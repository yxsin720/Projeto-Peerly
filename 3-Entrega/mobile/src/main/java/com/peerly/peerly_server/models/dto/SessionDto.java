package com.peerly.peerly_server.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionDto {
    private String id;
    private String tutorId;
    private String subjectId;
    private String title;
    private String description;
    private String startsAt;
    private String endsAt;
    private String status;
    private Integer maxParticipants;
    private Integer priceTotalCents;
    private String createdAt;
    private TutorSlimDto tutor;

    private String studentId;
    private String studentName;
    private String studentAvatarUrl;

    public String getId() { return id; }
    public String getTutorId() { return tutorId; }
    public String getSubjectId() { return subjectId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStartsAt() { return startsAt; }
    public String getEndsAt() { return endsAt; }
    public String getStatus() { return status; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public Integer getPriceTotalCents() { return priceTotalCents; }
    public String getCreatedAt() { return createdAt; }
    public TutorSlimDto getTutor() { return tutor; }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStudentAvatarUrl() { return studentAvatarUrl; }

    public void setId(String id) { this.id = id; }
    public void setTutorId(String tutorId) { this.tutorId = tutorId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartsAt(String startsAt) { this.startsAt = startsAt; }
    public void setEndsAt(String endsAt) { this.endsAt = endsAt; }
    public void setStatus(String status) { this.status = status; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public void setPriceTotalCents(Integer priceTotalCents) { this.priceTotalCents = priceTotalCents; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setTutor(TutorSlimDto tutor) { this.tutor = tutor; }

    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setStudentAvatarUrl(String studentAvatarUrl) { this.studentAvatarUrl = studentAvatarUrl; }
}
