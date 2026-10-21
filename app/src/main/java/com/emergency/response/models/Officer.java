package com.emergency.response.models;

import java.io.Serializable;

public class Officer implements Serializable {
    private String id;
    private String name;
    private String gender; // Male, Female, Other, Prefer not to say
    private int genderIconRes;
    private int age;
    private String department;
    private String designation;
    private String employeeId;
    private String phone;
    private String email;
    private String experience;
    private String availability; // Available, On Duty, Off Duty
    private int photoResId;
    private String photoUri;

    public Officer(String id, String name, String gender, int genderIconRes, int age,
                   String department, String designation, String employeeId,
                   String phone, String email, String experience, String availability,
                   int photoResId, String photoUri) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.genderIconRes = genderIconRes;
        this.age = age;
        this.department = department;
        this.designation = designation;
        this.employeeId = employeeId;
        this.phone = phone;
        this.email = email;
        this.experience = experience;
        this.availability = availability;
        this.photoResId = photoResId;
        this.photoUri = photoUri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGenderIconRes() {
        return genderIconRes;
    }

    public void setGenderIconRes(int genderIconRes) {
        this.genderIconRes = genderIconRes;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getPhotoResId() {
        return photoResId;
    }

    public void setPhotoResId(int photoResId) {
        this.photoResId = photoResId;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
