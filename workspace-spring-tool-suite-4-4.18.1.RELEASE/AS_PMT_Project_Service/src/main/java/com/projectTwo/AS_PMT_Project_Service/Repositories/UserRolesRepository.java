package com.projectTwo.AS_PMT_Project_Service.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

	UserRoles findByRoleName(String name);
    
}
