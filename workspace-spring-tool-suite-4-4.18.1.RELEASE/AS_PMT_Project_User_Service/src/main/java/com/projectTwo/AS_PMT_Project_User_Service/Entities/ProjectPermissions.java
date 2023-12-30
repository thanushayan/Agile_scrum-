package com.projectTwo.AS_PMT_Project_User_Service.Entities;

import java.util.List;

import jakarta.persistence.*;

public class ProjectPermissions  {

  
    private Long id;

    private String projectId;

    private List<UserRoles> projectManagementPermissions;

    private List<UserRoles> taskSprintPermissions;

    private List<UserRoles> manageUsersPermissions;

    private List<UserRoles> addCommentsPermissions;

    private List<UserRoles> uploadDocumentsPermissions;

    private List<UserRoles> viewReportsPermissions;

    private List<UserRoles> systemConfigurationPermissions;

    // Getters and Setters

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

    public List<UserRoles> getProjectManagementPermissions() {
        return projectManagementPermissions;
    }

    public void setProjectManagementPermissions(List<UserRoles> projectManagementPermissions) {
        this.projectManagementPermissions = projectManagementPermissions;
    }

    public List<UserRoles> getTaskSprintPermissions() {
        return taskSprintPermissions;
    }

    public void setTaskSprintPermissions(List<UserRoles> taskSprintPermissions) {
        this.taskSprintPermissions = taskSprintPermissions;
    }

    public List<UserRoles> getManageUsersPermissions() {
        return manageUsersPermissions;
    }

    public void setManageUsersPermissions(List<UserRoles> manageUsersPermissions) {
        this.manageUsersPermissions = manageUsersPermissions;
    }

    public List<UserRoles> getAddCommentsPermissions() {
        return addCommentsPermissions;
    }

    public void setAddCommentsPermissions(List<UserRoles> addCommentsPermissions) {
        this.addCommentsPermissions = addCommentsPermissions;
    }

    public List<UserRoles> getUploadDocumentsPermissions() {
        return uploadDocumentsPermissions;
    }

    public void setUploadDocumentsPermissions(List<UserRoles> uploadDocumentsPermissions) {
        this.uploadDocumentsPermissions = uploadDocumentsPermissions;
    }

    public List<UserRoles> getViewReportsPermissions() {
        return viewReportsPermissions;
    }

    public void setViewReportsPermissions(List<UserRoles> viewReportsPermissions) {
        this.viewReportsPermissions = viewReportsPermissions;
    }

    public List<UserRoles> getSystemConfigurationPermissions() {
        return systemConfigurationPermissions;
    }

    public void setSystemConfigurationPermissions(List<UserRoles> systemConfigurationPermissions) {
        this.systemConfigurationPermissions = systemConfigurationPermissions;
    }
}
