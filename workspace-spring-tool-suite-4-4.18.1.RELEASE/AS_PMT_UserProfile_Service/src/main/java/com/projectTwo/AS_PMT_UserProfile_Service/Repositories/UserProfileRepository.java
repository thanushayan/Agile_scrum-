package com.projectTwo.AS_PMT_UserProfile_Service.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectTwo.AS_PMT_UserProfile_Service.Entities.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    UserProfile findByUsername(String username);

   
}

