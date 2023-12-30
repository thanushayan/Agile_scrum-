package com.projectTwo.AS_PMT_Project_Service.Services;

import com.projectTwo.AS_PMT_Project_Service.Entities.ScrumBoard;
import com.projectTwo.AS_PMT_Project_Service.Repositories.ScrumBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScrumBoardService {

    private final ScrumBoardRepository scrumBoardRepository;

    @Autowired
    public ScrumBoardService(ScrumBoardRepository scrumBoardRepository) {
        this.scrumBoardRepository = scrumBoardRepository;
    }

    public List<ScrumBoard> getAllScrumBoards() {
        return scrumBoardRepository.findAll();
    }

    public ScrumBoard createScrumBoard(ScrumBoard scrumBoard) {
        // Any pre-processing or validation can be done here before saving to the database
        return scrumBoardRepository.save(scrumBoard);
    }

    public ScrumBoard updateScrumBoard(int id, ScrumBoard updatedScrumBoard) {
        Optional<ScrumBoard> existingScrumBoard = scrumBoardRepository.findById(id);
        if (existingScrumBoard.isPresent()) {
            // Update the existing ScrumBoard entity
            updatedScrumBoard.setStatusName(updatedScrumBoard.getStatusName()); // Ensure the ID is set for the update
            return scrumBoardRepository.save(updatedScrumBoard);
        }
        // If the entity doesn't exist, you might throw an exception or handle it accordingly
        // For simplicity, returning null here. In real scenarios, proper error handling should be implemented.
        return null;
    }
    
    public ScrumBoard getScrumBoardByStatusName(String statusName) {
        return scrumBoardRepository.findByStatusName(statusName);
    }

    public boolean scrumBoardExists(int id) {
        return scrumBoardRepository.existsById(id);
    }

    // Other methods for additional functionalities, error handling, etc.

}

