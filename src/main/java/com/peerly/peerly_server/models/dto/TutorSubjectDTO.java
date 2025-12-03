package com.peerly.peerly_server.models.dto;

public class TutorSubjectDTO {
    private String subjectId;
    private String subjectSlug;
    private String subjectName;
    private String level;              
    private Integer pricePerHourCents; 

    public TutorSubjectDTO() {}

    public TutorSubjectDTO(String subjectId, String subjectSlug, String subjectName, String level, Integer pricePerHourCents) {
        this.subjectId = subjectId;
        this.subjectSlug = subjectSlug;
        this.subjectName = subjectName;
        this.level = level;
        this.pricePerHourCents = pricePerHourCents;
    }

    public String getSubjectId() { return subjectId; }
    public String getSubjectSlug() { return subjectSlug; }
    public String getSubjectName() { return subjectName; }
    public String getLevel() { return level; }
    public Integer getPricePerHourCents() { return pricePerHourCents; }

    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public void setSubjectSlug(String subjectSlug) { this.subjectSlug = subjectSlug; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public void setLevel(String level) { this.level = level; }
    public void setPricePerHourCents(Integer pricePerHourCents) { this.pricePerHourCents = pricePerHourCents; }
}
