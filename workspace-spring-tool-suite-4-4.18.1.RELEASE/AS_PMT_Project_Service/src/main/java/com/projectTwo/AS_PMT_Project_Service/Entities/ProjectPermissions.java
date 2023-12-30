package com.projectTwo.AS_PMT_Project_Service.Entities;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Project_Permissions")
public class ProjectPermissions  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "projectManagement_Permissions_UserRoles",
        joinColumns = @JoinColumn(name = "project_permissions_id"),
        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
    )
    private List<UserRoles> projectManagementPermissions;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "taskSprint_Permissions_UserRoles",
        joinColumns = @JoinColumn(name = "project_permissions_id"),
        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
    )
    private List<UserRoles> taskSprintPermissions;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "manageUsers_Permissions_UserRoles",
        joinColumns = @JoinColumn(name = "project_permissions_id"),
        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
    )
    private List<UserRoles> manageUsersPermissions;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "addComments_Permissions_UserRoles",
        joinColumns = @JoinColumn(name = "project_permissions_id"),
        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
    )
    private List<UserRoles> addCommentsPermissions;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "uploadDocuments_Permissions_UserRoles",
        joinColumns = @JoinColumn(name = "project_permissions_id"),
        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
    )
    private List<UserRoles> uploadDocumentsPermissions;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "viewReports_Permissions_UserRoles",
        joinColumns = @JoinColumn(name = "project_permissions_id"),
        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
    )
    private List<UserRoles> viewReportsPermissions;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "systemConfiguration_Permissions_UserRoles",
        joinColumns = @JoinColumn(name = "project_permissions_id"),
        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
    )
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
