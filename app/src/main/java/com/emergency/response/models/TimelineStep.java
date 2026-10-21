package com.emergency.response.models;

public class TimelineStep {
    private String title;
    private String description;
    private String time;
    private boolean isCompleted;
    private boolean isCurrent;

    public TimelineStep(String title, String description, String time, boolean isCompleted, boolean isCurrent) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.isCompleted = isCompleted;
        this.isCurrent = isCurrent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubtitle(String subtitle) {
        this.description = subtitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimestamp(String timestamp) {
        this.time = timestamp;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
