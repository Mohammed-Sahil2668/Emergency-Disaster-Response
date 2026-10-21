package com.emergency.response.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmergencyReport implements Serializable {
    private String id;
    private String title;
    private String category;
    private int categoryIconRes;
    private String description;
    private String location;
    private double latitude;
    private double longitude;
    private String reportedDate;
    private String status; // Reported, Verified, Assigned, Responding, Resolved, Rejected
    private String responderUnit;
    private String officerName;
    private String assignedOfficerId;
    private String eta;
    private String attachedPhotoName;
    private String reporterName;
    private String reporterEmail;
    private String reporterPhone;
    private String reporterPhoto;
    private String rejectionReason;
    private List<TimelineStep> timelineSteps;

    public EmergencyReport(String id, String title, String category, int categoryIconRes,
                           String description, String location, double latitude, double longitude,
                           String reportedDate, String status, String responderUnit,
                           String officerName, String eta, String attachedPhotoName) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.categoryIconRes = categoryIconRes;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reportedDate = reportedDate;
        this.status = status;
        this.responderUnit = responderUnit;
        this.officerName = officerName;
        this.eta = eta;
        this.attachedPhotoName = attachedPhotoName;
        this.reporterName = "Sahil Sharma";
        this.reporterEmail = "sahil.sharma@example.com";
        this.reporterPhone = "+91 98765 43210";
        this.reporterPhoto = "avatar_sahil.png";
        this.rejectionReason = "";
        this.assignedOfficerId = "";
        this.timelineSteps = new ArrayList<>();
    }

    public EmergencyReport(String id, String title, String category, int categoryIconRes,
                           String description, String location, double latitude, double longitude,
                           String reportedDate, String status, String responderUnit,
                           String officerName, String eta, String attachedPhotoName,
                           String reporterName, String reporterEmail, String reporterPhone) {
        this(id, title, category, categoryIconRes, description, location, latitude, longitude,
                reportedDate, status, responderUnit, officerName, eta, attachedPhotoName);
        this.reporterName = reporterName;
        this.reporterEmail = reporterEmail;
        this.reporterPhone = reporterPhone;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryIconRes() {
        return categoryIconRes;
    }

    public void setCategoryIconRes(int categoryIconRes) {
        this.categoryIconRes = categoryIconRes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(String reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponderUnit() {
        return responderUnit;
    }

    public void setResponderUnit(String responderUnit) {
        this.responderUnit = responderUnit;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getAssignedOfficerId() {
        return assignedOfficerId;
    }

    public void setAssignedOfficerId(String assignedOfficerId) {
        this.assignedOfficerId = assignedOfficerId;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getAttachedPhotoName() {
        return attachedPhotoName;
    }

    public void setAttachedPhotoName(String attachedPhotoName) {
        this.attachedPhotoName = attachedPhotoName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    public String getReporterPhoto() {
        return reporterPhoto;
    }

    public void setReporterPhoto(String reporterPhoto) {
        this.reporterPhoto = reporterPhoto;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public List<TimelineStep> getTimelineSteps() {
        return timelineSteps;
    }

    public void setTimelineSteps(List<TimelineStep> timelineSteps) {
        this.timelineSteps = timelineSteps;
    }
}
