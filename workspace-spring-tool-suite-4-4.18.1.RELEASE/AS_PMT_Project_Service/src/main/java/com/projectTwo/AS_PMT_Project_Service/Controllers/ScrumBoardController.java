package com.projectTwo.AS_PMT_Project_Service.Controllers;

import com.projectTwo.AS_PMT_Project_Service.Entities.ScrumBoard;
import com.projectTwo.AS_PMT_Project_Service.Services.ScrumBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scrum-boards")
public class ScrumBoardController {

    private final ScrumBoardService scrumBoardService;

    @Autowired
    public ScrumBoardController(ScrumBoardService scrumBoardService) {
        this.scrumBoardService = scrumBoardService;
    }

    @GetMapping
    public ResponseEntity<List<ScrumBoard>> getAllScrumBoards() {
        List<ScrumBoard> scrumBoards = scrumBoardService.getAllScrumBoards();
        return new ResponseEntity<>(scrumBoards, HttpStatus.OK);
    }
    
    @GetMapping("/{name}")
    public ResponseEntity<ScrumBoard> getScrumBoards(@PathVariable String name) {
        ScrumBoard scrumBoards = scrumBoardService.getScrumBoardByStatusName(name);
        return new ResponseEntity<ScrumBoard>(scrumBoards, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ScrumBoard> createScrumBoard(@RequestBody ScrumBoard scrumBoard) {
        ScrumBoard createdScrumBoard = scrumBoardService.createScrumBoard(scrumBoard);
        return new ResponseEntity<>(createdScrumBoard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScrumBoard> updateScrumBoard(@PathVariable int id, @RequestBody ScrumBoard scrumBoard) {
        if (scrumBoardService.scrumBoardExists(id)) {
            ScrumBoard updatedScrumBoard = scrumBoardService.updateScrumBoard(id, scrumBoard);
            return new ResponseEntity<>(updatedScrumBoard, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Other methods for error handling and additional functionalities

}
