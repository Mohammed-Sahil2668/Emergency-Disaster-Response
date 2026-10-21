package com.emergency.response.models;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String emergencyContact;
    private String bloodGroup;
    private String address;

    public UserProfile(String name, String email, String phone, String emergencyContact, String bloodGroup, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.bloodGroup = bloodGroup;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
