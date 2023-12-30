package com.projectTwo.AS_PMT_Project_User_Service.Entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserRoles {
    
    private Long id;

    private String roleName;
    
    private String description;


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
