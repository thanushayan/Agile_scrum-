package com.projectTwo.AS_PMT_Project_Service.Entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "backlogs")
public class Backlog_Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @ManyToMany(targetEntity = Issue_Entity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "Sprint_Tasks", referencedColumnName = "project_id")
    private List<Issue_Entity> issues = new ArrayList<>();

    // Constructors, getters, and setters

    public Backlog_Entity() {
    }

  
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public List<Issue_Entity> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue_Entity> issues) {
        this.issues = issues;
    }

    public void addIssue(Issue_Entity issue) {
        this.issues.add(issue);
    }

    public void removeIssue(Issue_Entity issue) {
        this.issues.remove(issue);
    }
    
}
