package com.projectTwo.AS_PMT_Project_Service.Services;

import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;
import com.projectTwo.AS_PMT_Project_Service.Repositories.UserRolesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRolesService {

    private final UserRolesRepository userRolesRepository;

    @Autowired
    public UserRolesService(UserRolesRepository userRolesRepository) {
        this.userRolesRepository = userRolesRepository;
    }

    public UserRoles addRole(UserRoles userRoles) {
        // Implement your business logic for adding a role
        return userRolesRepository.save(userRoles);
    }

    public List<UserRoles> getAllRoles() {
        // Implement your business logic for getting all roles
        return userRolesRepository.findAll();
    }

    public UserRoles getRoleById(Long id) {
        // Implement your business logic for getting a role by ID
        return userRolesRepository.findById(id).orElse(null);
    }

    public UserRoles getRoleByName(String name) {
        // Implement your business logic for getting a role by name
        return userRolesRepository.findByRoleName(name);
    }
}
