package com.projectTwo.AS_PMT_UserProfile_Service.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = true)
    private String location;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String phone;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(length = 1000)
    private String skills;

    @Column(length = 1000)
    private String experience;
    
    @Column(length = 1000)
    private String interests;
    
    @Column
    private String profilePicture;


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

