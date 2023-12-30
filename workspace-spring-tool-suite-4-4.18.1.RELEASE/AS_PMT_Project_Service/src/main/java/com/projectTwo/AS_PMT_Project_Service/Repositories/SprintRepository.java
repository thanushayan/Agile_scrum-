package com.projectTwo.AS_PMT_Project_Service.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectTwo.AS_PMT_Project_Service.Entities.Sprint_Entity;



public interface SprintRepository extends JpaRepository<Sprint_Entity, Long> {
	Sprint_Entity findBySprintId(String sprintID);

	List<Sprint_Entity> findByProjectId(String projectId);
	
	void deleteBySprintId(String sprintID);
}
