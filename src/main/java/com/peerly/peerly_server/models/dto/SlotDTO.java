package com.peerly.peerly_server.models.dto;

public class SlotDTO {
    private String id;
    private int weekday;        // 0..6
    private String startTime;   // "HH:mm:ss"
    private String endTime;     // "HH:mm:ss"
    private String timezone;    // ex: "Europe/Lisbon"

    // ✅ Construtor vazio (obrigatório para JSON)
    public SlotDTO() {}

    // ✅ Construtor usado no TutorsController (com 5 argumentos)
    public SlotDTO(String id, int weekday, String startTime, String endTime, String timezone) {
        this.id = id;
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timezone = timezone;
    }

    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getWeekday() { return weekday; }
    public void setWeekday(int weekday) { this.weekday = weekday; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}
