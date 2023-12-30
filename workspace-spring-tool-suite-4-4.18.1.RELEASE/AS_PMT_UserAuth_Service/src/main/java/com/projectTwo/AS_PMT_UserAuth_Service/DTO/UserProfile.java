package com.projectTwo.AS_PMT_UserAuth_Service.DTO;

import jakarta.persistence.*;

public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String profilePicture;

    private String username;

    private String location;

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private String skills;

    private String experience;

    private String interests;


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
    
    public String getInterest() {
        return interests;
    }

    public void setInterest(String interests) {
        this.interests = interests;
    }
    
    public String getProfilePicture()
    {
    	return profilePicture;
    }
    public void setProfilePicture(String profilePicture)
    {
    	this.profilePicture= profilePicture;
    }
  
}

