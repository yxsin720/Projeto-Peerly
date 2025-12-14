package com.peerly.peerly_server.models.dto;

public class GoogleMeetSpaceResponse {

    private String name;
    private String meetingUri;
    private String meetingCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeetingUri() {
        return meetingUri;
    }

    public void setMeetingUri(String meetingUri) {
        this.meetingUri = meetingUri;
    }

    public String getMeetingCode() {
        return meetingCode;
    }

    public void setMeetingCode(String meetingCode) {
        this.meetingCode = meetingCode;
    }
}
