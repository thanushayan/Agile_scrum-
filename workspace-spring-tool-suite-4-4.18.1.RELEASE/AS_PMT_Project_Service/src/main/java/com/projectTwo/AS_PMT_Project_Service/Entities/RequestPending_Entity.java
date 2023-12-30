package com.projectTwo.AS_PMT_Project_Service.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "request_pending") // Define the table name
public class RequestPending_Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Define column name
    private Long id;

    @Column(name = "user_name" , nullable = false) // Define column name
    private String userName;

    @Column(name = "projectId", nullable = false) // Define column name
    private String projectId;
    
    @Column(name = "requester_user_name", nullable = false) // Define column name
    private String requesterName;

    // Constructors, getters, and setters
    // (same as in the previous example)

    // Getters and setters for id field
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
