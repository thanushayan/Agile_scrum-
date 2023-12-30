package com.projectTwo.AS_PMT_Project_Service.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;
import com.projectTwo.AS_PMT_Project_Service.Services.UserRolesService;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class UserRolesController {

    private final UserRolesService userRolesService;

    public UserRolesController(UserRolesService userRolesService) {
        this.userRolesService = userRolesService;
    }

    @PostMapping
    public ResponseEntity<UserRoles> addRole(@RequestBody UserRoles userRoles) {
        try {
            UserRoles createdRole = userRolesService.addRole(userRoles);
            return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exception and return appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserRoles>> getAllRoles() {
        try {
            List<UserRoles> roles = userRolesService.getAllRoles();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exception and return appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoles> getRoleById(@PathVariable Long id) {
        try {
            UserRoles role = userRolesService.getRoleById(id);
            return role != null ? new ResponseEntity<>(role, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle exception and return appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserRoles> getRoleByName(@PathVariable String name) {
        try {
            UserRoles role = userRolesService.getRoleByName(name);
            return role != null ? new ResponseEntity<>(role, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handle exception and return appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
