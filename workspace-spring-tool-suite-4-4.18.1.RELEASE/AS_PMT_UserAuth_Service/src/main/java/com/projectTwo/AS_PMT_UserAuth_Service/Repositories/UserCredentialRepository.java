package com.projectTwo.AS_PMT_UserAuth_Service.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectTwo.AS_PMT_UserAuth_Service.Entity.UserCredential;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {
    Optional<UserCredential> findByName(String username);

    UserCredential findByname(String name);

    UserCredential findByEmail(String email);
}

