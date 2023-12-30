package com.projectTwo.AS_PMT_Chat.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectTwo.AS_PMT_Chat.Entity.ChatEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

	List<ChatEntity> findByProjectId(String projectId);
    // Custom query methods if needed
}
