package com.projectTwo.AS_PMT_Project_Service.Repositories;

import com.projectTwo.AS_PMT_Project_Service.Entities.ScrumBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrumBoardRepository extends JpaRepository<ScrumBoard, Integer> {
	ScrumBoard findByStatusName(String statusName);
}
