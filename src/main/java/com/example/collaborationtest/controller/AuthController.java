package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.user.LoginRequestDTO;
import com.example.collaborationtest.dto.user.UserRequestDTO;
import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.service.EmailService;
import com.example.collaborationtest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder encoder;

    @Value("${app.backend-url:http://localhost:3307}")
    private String backendUrl;

    @Value("${app.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    public AuthController(UserService userService, PasswordEncoder encoder, EmailService emailService) {
        this.userService = userService;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequestDTO request) {

        if (this.userService.getUser(request.email()) == null) {
            User newUser = new User();
            newUser.setUsername(request.username());
            newUser.setEmail(request.email());
            newUser.setPassword(request.password());
            newUser.setRoles(Collections.singleton(Role.USER));
            newUser.setEnabled(true);
            newUser.setEmailConfirmed(false);
            newUser.setConfirmationToken(UUID.randomUUID().toString());
            if (request.termsAccepted()) {
                newUser.setTermsAccepted(true);
                newUser.setTermsAcceptedAt(LocalDateTime.now());
                newUser.setTermsVersion(LocalDate.now().toString());
            }

            User savedUser = userService.createUser(newUser);
            if (savedUser == null) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            // Confirmation link points back to this API; the endpoint then redirects to the frontend.
            String confirmationUrl = backendUrl + "/api/auth/confirm?token=" + savedUser.getConfirmationToken();
            emailService.sendAccountConfirmationEmail(savedUser.getEmail(), savedUser.getUsername(), confirmationUrl);
        }

        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Clicked from the confirmation email. Validates the token, marks the email
     * as confirmed, then redirects the browser to the frontend login page.
     */
    @GetMapping("/confirm")
    public ResponseEntity<Void> confirm(@RequestParam String token) {
        boolean confirmed = userService.confirmEmail(token);
        String target = frontendUrl + "/login?confirmed=" + (confirmed ? "true" : "false");
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(target)).build();
    }

    /**
     * Clicked from the password-change confirmation email. Applies the staged
     * password if the token is valid/unexpired, then redirects to the account
     * page on the frontend.
     */
    @GetMapping("/confirm-password-change")
    public ResponseEntity<Void> confirmPasswordChange(@RequestParam String token) {
        boolean confirmed = userService.confirmPasswordChange(token);
        String target = frontendUrl + "/user/account?passwordChanged=" + (confirmed ? "true" : "false");
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(target)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {

        User vrfUser = new User();
        vrfUser.setEmail(request.email());
        vrfUser.setPassword(request.password());
        vrfUser.setUsername(this.userService.getUser(request.email()).getUsername());

        String token = userService.verify(vrfUser);

        if ("Verification failed".equals(token)) {
            return ResponseEntity.ok("Invalid username or password");
        }

        return ResponseEntity.ok(token);
    }
}
