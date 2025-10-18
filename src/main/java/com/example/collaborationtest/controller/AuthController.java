package com.example.collaborationtest.controller;

import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final PasswordEncoder encoder;

    public AuthController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;

    }


    @PostMapping("/register")
    public ResponseEntity<?> register( @RequestBody User user) {
        // Create new user via UserService
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setRoles(   Collections.singleton(Role.USER)); // default role
        newUser.setEnabled(true);

        User savedUser = userService.createUser(newUser);
        if (savedUser == null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody User user) {
        System.out.println(user.getUsername());
        User vrfUser = new User();
        vrfUser.setUsername(user.getUsername());
        vrfUser.setPassword(user.getPassword());

        String token = userService.verify(vrfUser);

        if ("Verification failed".equals(token)) {
            return ResponseEntity.ok("Invalid username or password");
        }

        return ResponseEntity.ok(token);
    }
}
