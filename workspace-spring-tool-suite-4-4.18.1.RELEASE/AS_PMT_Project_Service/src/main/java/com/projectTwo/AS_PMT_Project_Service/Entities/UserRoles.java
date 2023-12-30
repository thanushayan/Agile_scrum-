package com.projectTwo.AS_PMT_Project_Service.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "user_roles")
public class UserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", columnDefinition = "VARCHAR(255)")
    private String roleName;
    
    @Column(name = "description", columnDefinition = "VARCHAR(255)")
    private String description;
    
    @ManyToMany(mappedBy = "projectManagementPermissions", cascade = CascadeType.ALL)
    private List<ProjectPermissions> projectsWithProjectManagementPermissions;


    public UserRoles()
    {
    	
    }
    
    public UserRoles(String roleName, String description)
    {
    	this.roleName = roleName;
    	this.description = description;
    }
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

  
}
