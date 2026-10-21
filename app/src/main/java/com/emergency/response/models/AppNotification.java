package com.emergency.response.models;

import java.io.Serializable;

public class AppNotification implements Serializable {
    private String id;
    private String roleTarget; // CITIZEN, AUTHORITY, OFFICER
    private String title;
    private String message;
    private String emergencyType;
    private String location;
    private String timestamp;
    private String reportId;
    private boolean isRead;

    public AppNotification(String id, String roleTarget, String title, String message,
                           String emergencyType, String location, String timestamp,
                           String reportId, boolean isRead) {
        this.id = id;
        this.roleTarget = roleTarget;
        this.title = title;
        this.message = message;
        this.emergencyType = emergencyType;
        this.location = location;
        this.timestamp = timestamp;
        this.reportId = reportId;
        this.isRead = isRead;
    }

    public String getId() {
        return id;
    }

    public String getRoleTarget() {
        return roleTarget;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public String getLocation() {
        return location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getReportId() {
        return reportId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
