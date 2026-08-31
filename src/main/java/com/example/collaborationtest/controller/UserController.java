package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.user.UserRequestDTO;
import com.example.collaborationtest.dto.user.UserResponseDTO;
import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO created = userService.createUser(request);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/userId/{email}")
    public ResponseEntity<Integer> getUserByName(@PathVariable String email) {
        return ResponseEntity.ok(userService.getIdByEmail(email));
    }

    @GetMapping("/getRole/{name}")
    public ResponseEntity<Set<Role>> getRole(@PathVariable String name) {
        return ResponseEntity.ok(userService.getRole(name));
    }

    @DeleteMapping("/user/delete/{email:.+}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
