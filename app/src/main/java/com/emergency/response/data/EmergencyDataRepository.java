package com.emergency.response.data;

import com.emergency.response.R;
import com.emergency.response.models.AppNotification;
import com.emergency.response.models.EmergencyCategory;
import com.emergency.response.models.EmergencyReport;
import com.emergency.response.models.Officer;
import com.emergency.response.models.TimelineStep;
import com.emergency.response.models.UserProfile;
import com.emergency.response.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class EmergencyDataRepository {

    public enum UserRole {
        CITIZEN,
        AUTHORITY,
        OFFICER
    }

    private static EmergencyDataRepository instance;
    private final List<EmergencyCategory> categories;
    private final List<EmergencyReport> reports;
    private final List<Officer> officers;
    private final List<AppNotification> notifications;

    private UserRole currentRole = UserRole.CITIZEN;
    private String currentUserId = "citizen_default";
    private UserProfile citizenProfile;
    private Officer currentOfficerProfile;

    private EmergencyDataRepository() {
        categories = new ArrayList<>();
        reports = new ArrayList<>();
        officers = new ArrayList<>();
        notifications = new ArrayList<>();

        initCategories();
        initDefaultProfiles();
        initOfficers();
        initSampleReports();
        initSampleNotifications();
    }

    public static synchronized EmergencyDataRepository getInstance() {
        if (instance == null) {
            instance = new EmergencyDataRepository();
        }
        return instance;
    }

    // Role & Session Management
    public UserRole getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(UserRole role) {
        this.currentRole = role;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    private void initCategories() {
        categories.add(new EmergencyCategory("flood", "Flood", R.drawable.ic_flood, R.color.cat_flood, R.color.cat_flood_bg, "Urban waterlogging or river flooding"));
        categories.add(new EmergencyCategory("cyclone", "Cyclone", R.drawable.ic_cyclone, R.color.cat_cyclone, R.color.cat_cyclone_bg, "Storm surge, high winds"));
        categories.add(new EmergencyCategory("heavy_rainfall", "Heavy Rainfall", R.drawable.ic_heavy_rainfall, R.color.cat_heavy_rainfall, R.color.cat_heavy_rainfall_bg, "Violent downpour, flash waterlogging"));
        categories.add(new EmergencyCategory("landslide", "Landslide", R.drawable.ic_landslide, R.color.cat_landslide, R.color.cat_landslide_bg, "Rockfall and soil displacement"));
        categories.add(new EmergencyCategory("earthquake", "Earthquake", R.drawable.ic_earthquake, R.color.cat_earthquake, R.color.cat_earthquake_bg, "Structural cracks and tremor damage"));
        categories.add(new EmergencyCategory("road_accident", "Road Accidents", R.drawable.ic_road_accident, R.color.cat_road_accident, R.color.cat_road_accident_bg, "Vehicular crash & trauma rescue"));
        categories.add(new EmergencyCategory("fire", "Fire Accidents", R.drawable.ic_fire_accident, R.color.cat_fire, R.color.cat_fire_bg, "Commercial or residential active blaze"));
        categories.add(new EmergencyCategory("medical", "Medical Emergencies", R.drawable.ic_medical_emergency, R.color.cat_medical, R.color.cat_medical_bg, "Cardiac, trauma, respiratory crisis"));
        categories.add(new EmergencyCategory("other", "Other Emergencies", R.drawable.ic_other_emergency, R.color.cat_other, R.color.cat_other_bg, "Chemical spill, gas leak, hazardous crisis"));
    }

    private void initDefaultProfiles() {
        citizenProfile = new UserProfile(
                "Sahil Sharma",
                "sahil.sharma@example.com",
                "+91 98765 43210",
                "+91 98765 11223 (Family Primary)",
                "O+ Positive",
                "#42, 12th Main Road, Indiranagar, Bengaluru, KA"
        );
    }

    private void initOfficers() {
        officers.add(new Officer(
                "OFF-4092", "Insp. Rajesh Kumar", "Male", R.drawable.ic_gender_male,
                38, "Police Department", "Police Inspector", "POL-4092",
                "+91 98450 12345", "rajesh.kumar@police.gov.in", "14 Years", "On Duty",
                R.drawable.ic_user_avatar, null
        ));

        officers.add(new Officer(
                "OFF-2810", "Station Officer V. Nair", "Male", R.drawable.ic_gender_male,
                42, "Fire and Rescue Department", "Fire Officer", "FIR-2810",
                "+91 98451 23456", "v.nair@fire.gov.in", "18 Years", "On Duty",
                R.drawable.ic_user_avatar, null
        ));

        officers.add(new Officer(
                "OFF-1102", "Dr. Ananya Roy", "Female", R.drawable.ic_gender_female,
                34, "Medical and Ambulance Department", "Medical Officer", "MED-1102",
                "+91 98452 34567", "ananya.roy@health.gov.in", "9 Years", "Available",
                R.drawable.ic_user_avatar, null
        ));

        officers.add(new Officer(
                "OFF-6451", "Officer Sunita Patil", "Female", R.drawable.ic_gender_female,
                29, "Disaster Management Department", "Disaster Response Officer", "DIS-6451",
                "+91 98453 45678", "sunita.patil@disaster.gov.in", "6 Years", "Available",
                R.drawable.ic_user_avatar, null
        ));

        officers.add(new Officer(
                "OFF-0012", "Chief Vikram Rathore", "Male", R.drawable.ic_gender_male,
                45, "Emergency Response Department", "Department Head", "ERD-0012",
                "+91 98454 56789", "vikram.rathore@emergency.gov.in", "22 Years", "Available",
                R.drawable.ic_user_avatar, null
        ));

        officers.add(new Officer(
                "OFF-3341", "Paramedic Anand", "Male", R.drawable.ic_gender_male,
                31, "Medical and Ambulance Department", "Paramedic", "MED-3341",
                "+91 98455 67890", "anand.paramedic@health.gov.in", "7 Years", "Available",
                R.drawable.ic_user_avatar, null
        ));

        officers.add(new Officer(
                "OFF-5590", "Pooja Sharma", "Female", R.drawable.ic_gender_female,
                27, "Fire and Rescue Department", "Firefighter", "FIR-5590",
                "+91 98456 78901", "pooja.sharma@fire.gov.in", "4 Years", "Available",
                R.drawable.ic_user_avatar, null
        ));

        currentOfficerProfile = officers.get(0); // Default officer session (Insp. Rajesh Kumar)
    }

    private void initSampleReports() {
        // Report 1: Flood - Status: Responding
        EmergencyReport report1 = new EmergencyReport(
                "EMG-2026-8942", "Flash Flooding Near 100 Feet Underpass", "Flood", R.drawable.ic_flood,
                "Severe stormwater inundation reached 3.5 feet. Three vehicles stranded with occupants. Low-lying basements flooded.",
                "100 Feet Rd, Indiranagar, Bengaluru", 12.9716, 77.5946,
                "21 Oct 2026, 14:30", "Responding", "Disaster Quick Response Team #4",
                "Insp. Rajesh Kumar", "6–8 Minutes (ETA)", "incident_flood_01.jpg",
                "Sahil Sharma", "sahil.sharma@example.com", "+91 98765 43210"
        );
        report1.setAssignedOfficerId("OFF-4092");
        List<TimelineStep> steps1 = new ArrayList<>();
        steps1.add(new TimelineStep("1. Incident Reported", "Submitted by Citizen Sahil Sharma via mobile app", "14:30", true, false));
        steps1.add(new TimelineStep("2. Verified by Central Control", "Verified by Operations Center via CCTV & sensor", "14:34", true, false));
        steps1.add(new TimelineStep("3. Unit Dispatched", "Insp. Rajesh Kumar (Badge #4092) deployed", "14:38", true, false));
        steps1.add(new TimelineStep("4. Responders En Route", "Unit 4 en route. Traffic clearance requested on 100ft road", "14:42", true, true));
        steps1.add(new TimelineStep("5. Incident Resolved", "Pending completion of drainage & evacuation", "Pending", false, false));
        report1.setTimelineSteps(steps1);
        reports.add(report1);

        // Report 2: Fire Accident - Status: Assigned
        EmergencyReport report2 = new EmergencyReport(
                "EMG-2026-7810", "Electrical Transformer Blaze in Commercial Plaza", "Fire Accidents", R.drawable.ic_fire_accident,
                "Heavy black smoke and sparks emanating from primary distribution transformer adjacent to grocery market. Fire spreading to canopy.",
                "MG Road Metro Station Junction, Bengaluru", 12.9752, 77.6094,
                "21 Oct 2026, 13:15", "Assigned", "City Fire & Rescue Station #2",
                "Station Officer V. Nair", "10–12 Minutes (ETA)", "incident_fire_02.jpg",
                "Kavita Deshmukh", "kavita.d@example.com", "+91 99887 76655"
        );
        report2.setAssignedOfficerId("OFF-2810");
        List<TimelineStep> steps2 = new ArrayList<>();
        steps2.add(new TimelineStep("1. Incident Reported", "Transmitted with photo evidence by Citizen", "13:15", true, false));
        steps2.add(new TimelineStep("2. Verified by Central Control", "Verified by Fire Control Room. Power utility notified", "13:18", true, false));
        steps2.add(new TimelineStep("3. Unit Assigned", "Station Officer V. Nair assigned to incident", "13:22", true, true));
        steps2.add(new TimelineStep("4. Responders En Route", "Vehicles preparing for high-speed dispatch", "Pending", false, false));
        steps2.add(new TimelineStep("5. Incident Resolved", "Pending fire containment and cooling", "Pending", false, false));
        report2.setTimelineSteps(steps2);
        reports.add(report2);

        // Report 3: Road Accidents - Status: Verified
        EmergencyReport report3 = new EmergencyReport(
                "EMG-2026-6451", "Multi-Vehicle Collision on Flyover", "Road Accidents", R.drawable.ic_road_accident,
                "Head-on collision involving two four-wheelers and a motorbike. Lane blocked, traffic backup building rapidly, 2 injured persons conscious.",
                "Domlur Flyover Ramp, Bengaluru", 12.9609, 77.6387,
                "21 Oct 2026, 11:45", "Verified", "Traffic Police & 108 Emergency Ambulance",
                "Unassigned", "Pending Officer Selection", "incident_accident_03.jpg",
                "Ramesh Iyer", "ramesh.iyer@example.com", "+91 91234 56789"
        );
        List<TimelineStep> steps3 = new ArrayList<>();
        steps3.add(new TimelineStep("1. Incident Reported", "Citizen distress submission logged", "11:45", true, false));
        steps3.add(new TimelineStep("2. Verified by Central Control", "Confirmed with highway patrol camera feed", "11:49", true, true));
        steps3.add(new TimelineStep("3. Unit Assigned", "Awaiting responder assignment", "Pending", false, false));
        steps3.add(new TimelineStep("4. Responders En Route", "Pending", "Pending", false, false));
        steps3.add(new TimelineStep("5. Incident Resolved", "Pending road clearance", "Pending", false, false));
        report3.setTimelineSteps(steps3);
        reports.add(report3);

        // Report 4: Heavy Rainfall - Status: Reported
        EmergencyReport report4 = new EmergencyReport(
                "EMG-2026-5219", "Uprooted Banyan Tree Obstructing Access Road", "Heavy Rainfall", R.drawable.ic_heavy_rainfall,
                "Violent squall caused massive tree to fall across both lanes, bringing down optical fiber cables. No casualties reported.",
                "HAL 3rd Stage, 80 Feet Road, Bengaluru", 12.9678, 77.6521,
                "21 Oct 2026, 10:10", "Reported", "Municipal Disaster Forestry Wing",
                "Unassigned", "Triage in progress", "incident_tree_04.jpg",
                "Sahil Sharma", "sahil.sharma@example.com", "+91 98765 43210"
        );
        List<TimelineStep> steps4 = new ArrayList<>();
        steps4.add(new TimelineStep("1. Incident Reported", "Citizen logged tree blockage report", "10:10", true, true));
        steps4.add(new TimelineStep("2. Verified by Central Control", "Pending authority review", "Pending", false, false));
        steps4.add(new TimelineStep("3. Unit Assigned", "Pending forestry crew", "Pending", false, false));
        steps4.add(new TimelineStep("4. Responders En Route", "Pending", "Pending", false, false));
        steps4.add(new TimelineStep("5. Incident Resolved", "Pending removal", "Pending", false, false));
        report4.setTimelineSteps(steps4);
        reports.add(report4);

        // Report 5: Medical Emergencies - Status: Resolved
        EmergencyReport report5 = new EmergencyReport(
                "EMG-2026-4102", "Critical Respiratory Distress at Community Park", "Medical Emergencies", R.drawable.ic_medical_emergency,
                "Elderly citizen experienced severe shortness of breath and collapsed on jogging track. Oxygen administered on site.",
                "Koramangala 4th Block Park, Bengaluru", 12.9344, 77.6288,
                "20 Oct 2026, 17:20", "Resolved", "Advanced Cardiac Life Support Ambulance #12",
                "Dr. Ananya Roy", "Completed in 14 minutes", "incident_medical_05.jpg",
                "Deepak Verma", "deepak.v@example.com", "+91 97766 55443"
        );
        report5.setAssignedOfficerId("OFF-1102");
        List<TimelineStep> steps5 = new ArrayList<>();
        steps5.add(new TimelineStep("1. Incident Reported", "Logged via SOS One-Touch", "17:20", true, false));
        steps5.add(new TimelineStep("2. Verified by Central Control", "Call center established voice verification", "17:21", true, false));
        steps5.add(new TimelineStep("3. Unit Assigned", "Dr. Ananya Roy dispatched", "17:23", true, false));
        steps5.add(new TimelineStep("4. Responders En Route", "Unit arrived at park gate in 7 mins", "17:30", true, false));
        steps5.add(new TimelineStep("5. Incident Resolved", "Patient stabilized and transferred to tertiary hospital", "17:48", true, true));
        report5.setTimelineSteps(steps5);
        reports.add(report5);
    }

    private void initSampleNotifications() {
        // Authority notifications
        notifications.add(new AppNotification("NOTIF-101", "AUTHORITY", "NEW EMERGENCY REPORT",
                "Flash Flood reported at 100 Feet Rd, Indiranagar", "Flood",
                "100 Feet Rd, Indiranagar, Bengaluru", "21 Oct 2026, 14:30", "EMG-2026-8942", false));

        notifications.add(new AppNotification("NOTIF-102", "AUTHORITY", "NEW EMERGENCY REPORT",
                "Electrical Transformer Blaze reported at MG Road Metro", "Fire Accidents",
                "MG Road Metro Station Junction, Bengaluru", "21 Oct 2026, 13:15", "EMG-2026-7810", false));

        notifications.add(new AppNotification("NOTIF-103", "AUTHORITY", "NEW EMERGENCY REPORT",
                "Multi-Vehicle Collision reported on Domlur Flyover", "Road Accidents",
                "Domlur Flyover Ramp, Bengaluru", "21 Oct 2026, 11:45", "EMG-2026-6451", false));

        notifications.add(new AppNotification("NOTIF-104", "AUTHORITY", "NEW EMERGENCY REPORT",
                "Uprooted Tree reported at HAL 3rd Stage, 80 Feet Road", "Heavy Rainfall",
                "HAL 3rd Stage, 80 Feet Road, Bengaluru", "21 Oct 2026, 10:10", "EMG-2026-5219", false));

        // Officer notifications
        notifications.add(new AppNotification("NOTIF-201", "OFFICER", "URGENT TASK ASSIGNED",
                "You have been assigned to Flash Flooding Near 100 Feet Underpass", "Flood",
                "100 Feet Rd, Indiranagar, Bengaluru", "21 Oct 2026, 14:38", "EMG-2026-8942", false));

        // Citizen notifications
        notifications.add(new AppNotification("NOTIF-301", "CITIZEN", "REPORT VERIFIED",
                "Your Flash Flood incident was verified by Central Control", "Flood",
                "100 Feet Rd, Indiranagar, Bengaluru", "21 Oct 2026, 14:34", "EMG-2026-8942", false));
    }

    // Categories
    public List<EmergencyCategory> getCategories() {
        return new ArrayList<>(categories);
    }

    // Reports Access
    public synchronized List<EmergencyReport> getAllReports() {
        return new ArrayList<>(reports);
    }

    public synchronized List<EmergencyReport> getRecentReports() {
        int limit = Math.min(reports.size(), 4);
        return new ArrayList<>(reports.subList(0, limit));
    }

    public synchronized List<EmergencyReport> getReportsForCitizen(String citizenName) {
        // Citizens can only view their own reports
        List<EmergencyReport> list = new ArrayList<>();
        for (EmergencyReport r : reports) {
            if (r.getReporterName() != null && (r.getReporterName().equalsIgnoreCase(citizenName) || citizenName == null)) {
                list.add(r);
            }
        }
        return list;
    }

    public synchronized List<EmergencyReport> getReportsByStatus(String status) {
        if (status == null || status.equalsIgnoreCase("All") || status.startsWith("All")) {
            return getAllReports();
        }
        List<EmergencyReport> filtered = new ArrayList<>();
        for (EmergencyReport r : reports) {
            if (r.getStatus().equalsIgnoreCase(status)) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public synchronized EmergencyReport getReportById(String id) {
        for (EmergencyReport r : reports) {
            if (r.getId().equalsIgnoreCase(id)) {
                return r;
            }
        }
        return null;
    }

    public synchronized void addReport(EmergencyReport report) {
        List<TimelineStep> steps = new ArrayList<>();
        steps.add(new TimelineStep("1. Incident Reported", "Submitted by Citizen via mobile app", report.getReportedDate(), true, true));
        steps.add(new TimelineStep("2. Verified by Central Control", "Queued for instant emergency verification", "In Progress", false, false));
        steps.add(new TimelineStep("3. Unit Assigned", "Nearest emergency responder will be designated", "Pending", false, false));
        steps.add(new TimelineStep("4. Responders En Route", "GPS live tracking will activate upon dispatch", "Pending", false, false));
        steps.add(new TimelineStep("5. Incident Resolved", "Post-incident safety confirmation", "Pending", false, false));
        report.setTimelineSteps(steps);

        reports.add(0, report);

        // Generate Authority in-app notification immediately
        String notifId = "NOTIF-" + System.currentTimeMillis() % 10000;
        notifications.add(0, new AppNotification(
                notifId,
                "AUTHORITY",
                "NEW EMERGENCY REPORT: " + report.getCategory().toUpperCase(),
                report.getTitle() + " at " + report.getLocation(),
                report.getCategory(),
                report.getLocation(),
                report.getReportedDate(),
                report.getId(),
                false
        ));
    }

    // Authority Actions
    public synchronized boolean verifyReport(String reportId) {
        EmergencyReport report = getReportById(reportId);
        if (report != null) {
            report.setStatus("Verified");
            List<TimelineStep> steps = report.getTimelineSteps();
            if (steps != null && steps.size() >= 2) {
                steps.get(0).setCompleted(true);
                steps.get(0).setCurrent(false);
                steps.get(1).setCompleted(true);
                steps.get(1).setCurrent(true);
                steps.get(1).setTimestamp(DateTimeUtils.getCurrentFormattedDateTime());
                steps.get(1).setSubtitle("Verified by Command Center Operator");
            }
            // Notify citizen
            notifications.add(0, new AppNotification(
                    "NOTIF-" + System.currentTimeMillis() % 10000,
                    "CITIZEN",
                    "REPORT VERIFIED",
                    "Your emergency report (" + report.getId() + ") has been verified by the control center.",
                    report.getCategory(),
                    report.getLocation(),
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    report.getId(),
                    false
            ));
            return true;
        }
        return false;
    }

    public synchronized boolean rejectReport(String reportId, String reason) {
        EmergencyReport report = getReportById(reportId);
        if (report != null) {
            report.setStatus("Rejected");
            report.setRejectionReason(reason);
            List<TimelineStep> steps = report.getTimelineSteps();
            if (steps != null && !steps.isEmpty()) {
                steps.add(new TimelineStep("Report Rejected", "Reason: " + reason, DateTimeUtils.getCurrentFormattedDateTime(), true, true));
            }
            // Notify citizen
            notifications.add(0, new AppNotification(
                    "NOTIF-" + System.currentTimeMillis() % 10000,
                    "CITIZEN",
                    "REPORT REJECTED",
                    "Incident report was reviewed and marked Rejected: " + reason,
                    report.getCategory(),
                    report.getLocation(),
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    report.getId(),
                    false
            ));
            return true;
        }
        return false;
    }

    public synchronized boolean assignOfficerToReport(String reportId, String officerId) {
        EmergencyReport report = getReportById(reportId);
        Officer officer = getOfficerById(officerId);
        if (report != null && officer != null) {
            report.setStatus("Assigned");
            report.setOfficerName(officer.getName() + " (" + officer.getDesignation() + ")");
            report.setResponderUnit(officer.getDepartment());
            report.setAssignedOfficerId(officer.getId());
            report.setEta("6–10 Minutes");

            // Update officer availability
            officer.setAvailability("On Duty");

            // Update timeline
            List<TimelineStep> steps = report.getTimelineSteps();
            if (steps != null && steps.size() >= 3) {
                steps.get(0).setCompleted(true);
                steps.get(1).setCompleted(true);
                steps.get(2).setCompleted(true);
                steps.get(2).setCurrent(true);
                steps.get(2).setTimestamp(DateTimeUtils.getCurrentFormattedDateTime());
                steps.get(2).setSubtitle("Unit Assigned: " + officer.getName() + " (" + officer.getDepartment() + ")");
            }

            // Create notification for Officer
            notifications.add(0, new AppNotification(
                    "NOTIF-" + System.currentTimeMillis() % 10000,
                    "OFFICER",
                    "NEW TASK ASSIGNED: " + report.getCategory().toUpperCase(),
                    "Dispatched to: " + report.getTitle() + " at " + report.getLocation(),
                    report.getCategory(),
                    report.getLocation(),
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    report.getId(),
                    false
            ));

            // Create notification for Citizen
            notifications.add(0, new AppNotification(
                    "NOTIF-" + System.currentTimeMillis() % 10000,
                    "CITIZEN",
                    "OFFICER ASSIGNED",
                    officer.getName() + " from " + officer.getDepartment() + " has been assigned to your incident.",
                    report.getCategory(),
                    report.getLocation(),
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    report.getId(),
                    false
            ));
            return true;
        }
        return false;
    }

    // Officer Operations
    public synchronized List<EmergencyReport> getTasksForOfficer(String officerId) {
        List<EmergencyReport> list = new ArrayList<>();
        for (EmergencyReport r : reports) {
            if (officerId != null && officerId.equalsIgnoreCase(r.getAssignedOfficerId())) {
                list.add(r);
            }
        }
        return list;
    }

    public synchronized boolean startOfficerResponse(String reportId, String officerId) {
        EmergencyReport report = getReportById(reportId);
        if (report != null) {
            report.setStatus("Responding");
            List<TimelineStep> steps = report.getTimelineSteps();
            if (steps != null && steps.size() >= 4) {
                steps.get(2).setCurrent(false);
                steps.get(3).setCompleted(true);
                steps.get(3).setCurrent(true);
                steps.get(3).setTimestamp(DateTimeUtils.getCurrentFormattedDateTime());
                steps.get(3).setSubtitle("Officer en route to incident coordinates");
            }
            // Notify citizen
            notifications.add(0, new AppNotification(
                    "NOTIF-" + System.currentTimeMillis() % 10000,
                    "CITIZEN",
                    "RESCUE UNIT EN ROUTE",
                    "Responder unit is actively en route to your incident location.",
                    report.getCategory(),
                    report.getLocation(),
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    report.getId(),
                    false
            ));
            return true;
        }
        return false;
    }

    public synchronized boolean completeOfficerTask(String reportId, String officerId) {
        EmergencyReport report = getReportById(reportId);
        Officer officer = getOfficerById(officerId);
        if (report != null) {
            report.setStatus("Resolved");
            if (officer != null) {
                officer.setAvailability("Available");
            }
            List<TimelineStep> steps = report.getTimelineSteps();
            if (steps != null && steps.size() >= 5) {
                steps.get(3).setCurrent(false);
                steps.get(4).setCompleted(true);
                steps.get(4).setCurrent(true);
                steps.get(4).setTimestamp(DateTimeUtils.getCurrentFormattedDateTime());
                steps.get(4).setSubtitle("Incident resolved and safety validated on site");
            }
            // Notify citizen & Authority
            notifications.add(0, new AppNotification(
                    "NOTIF-" + System.currentTimeMillis() % 10000,
                    "CITIZEN",
                    "EMERGENCY RESOLVED",
                    "Your reported emergency has been resolved by the response unit.",
                    report.getCategory(),
                    report.getLocation(),
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    report.getId(),
                    false
            ));
            notifications.add(0, new AppNotification(
                    "NOTIF-" + System.currentTimeMillis() % 10000,
                    "AUTHORITY",
                    "INCIDENT RESOLVED: " + report.getId(),
                    "Officer completed field operations at " + report.getLocation(),
                    report.getCategory(),
                    report.getLocation(),
                    DateTimeUtils.getCurrentFormattedDateTime(),
                    report.getId(),
                    false
            ));
            return true;
        }
        return false;
    }

    // Officers List & Filter
    public synchronized List<Officer> getAllOfficers() {
        return new ArrayList<>(officers);
    }

    public synchronized Officer getOfficerById(String id) {
        for (Officer o : officers) {
            if (o.getId().equalsIgnoreCase(id)) {
                return o;
            }
        }
        return null;
    }

    public synchronized List<Officer> getAvailableOfficers() {
        List<Officer> list = new ArrayList<>();
        for (Officer o : officers) {
            if ("Available".equalsIgnoreCase(o.getAvailability())) {
                list.add(o);
            }
        }
        return list;
    }

    public synchronized void updateOfficer(Officer updated) {
        for (int i = 0; i < officers.size(); i++) {
            if (officers.get(i).getId().equalsIgnoreCase(updated.getId())) {
                officers.set(i, updated);
                break;
            }
        }
        if (currentOfficerProfile != null && currentOfficerProfile.getId().equalsIgnoreCase(updated.getId())) {
            currentOfficerProfile = updated;
        }
    }

    public Officer getCurrentOfficerProfile() {
        return currentOfficerProfile;
    }

    public void setCurrentOfficerProfile(Officer officer) {
        this.currentOfficerProfile = officer;
    }

    // Notifications
    public synchronized List<AppNotification> getNotificationsForRole(String role) {
        List<AppNotification> list = new ArrayList<>();
        for (AppNotification n : notifications) {
            if (role == null || role.equalsIgnoreCase(n.getRoleTarget())) {
                list.add(n);
            }
        }
        return list;
    }

    public synchronized void markNotificationRead(String id) {
        for (AppNotification n : notifications) {
            if (n.getId().equalsIgnoreCase(id)) {
                n.setRead(true);
                break;
            }
        }
    }

    // Profiles
    public UserProfile getUserProfile() {
        return citizenProfile;
    }

    public void updateUserProfile(UserProfile profile) {
        this.citizenProfile = profile;
    }

    // Metrics for Authority Dashboard
    public int getTotalCount() {
        return reports.size();
    }

    public int getNewReportedCount() {
        int count = 0;
        for (EmergencyReport r : reports) {
            if (r.getStatus().equalsIgnoreCase("Reported")) count++;
        }
        return count;
    }

    public int getVerifiedCount() {
        int count = 0;
        for (EmergencyReport r : reports) {
            if (r.getStatus().equalsIgnoreCase("Verified")) count++;
        }
        return count;
    }

    public int getAssignedCount() {
        int count = 0;
        for (EmergencyReport r : reports) {
            if (r.getStatus().equalsIgnoreCase("Assigned")) count++;
        }
        return count;
    }

    public int getRespondingCount() {
        int count = 0;
        for (EmergencyReport r : reports) {
            if (r.getStatus().equalsIgnoreCase("Responding")) count++;
        }
        return count;
    }

    public int getResolvedCount() {
        int count = 0;
        for (EmergencyReport r : reports) {
            if (r.getStatus().equalsIgnoreCase("Resolved")) count++;
        }
        return count;
    }

    public int getActiveCount() {
        int count = 0;
        for (EmergencyReport r : reports) {
            if (!r.getStatus().equalsIgnoreCase("Resolved") && !r.getStatus().equalsIgnoreCase("Rejected")) {
                count++;
            }
        }
        return count;
    }
}
