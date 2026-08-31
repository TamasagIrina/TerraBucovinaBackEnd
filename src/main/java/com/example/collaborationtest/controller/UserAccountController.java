package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.user.PasswordChangeRequestDTO;
import com.example.collaborationtest.dto.user.UserResponseDTO;
import com.example.collaborationtest.dto.user.UserSelfUpdateDTO;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.service.EmailService;
import com.example.collaborationtest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Self-service endpoints for the currently logged-in user: view/edit their
 * own profile and request a password change. All routes here resolve "the
 * user" from the JWT (via {@link Authentication#getName()}, which is the
 * user's email — see {@code MyUserDetailService}), never from a client-
 * supplied id, so a user can only ever act on their own account.
 */
@RestController
@RequestMapping("/api/user")
public class UserAccountController {

    private final UserService userService;
    private final EmailService emailService;

    @Value("${app.backend-url:http://localhost:3307}")
    private String backendUrl;

    public UserAccountController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(Authentication authentication) {
        UserResponseDTO user = userService.getUserById(userService.getIdByEmail(authentication.getName()));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMe(Authentication authentication,
                                                     @Valid @RequestBody UserSelfUpdateDTO request) {
        return ResponseEntity.ok(userService.updateSelf(authentication.getName(), request));
    }

    @PostMapping("/me/password/change-request")
    public ResponseEntity<String> requestPasswordChange(Authentication authentication,
                                                         @Valid @RequestBody PasswordChangeRequestDTO request) {
        String token = userService.requestPasswordChange(authentication.getName(), request);
        User user = userService.getUser(authentication.getName());

        String confirmationUrl = backendUrl + "/api/auth/confirm-password-change?token=" + token;
        emailService.sendPasswordChangeConfirmationEmail(user.getEmail(), user.getUsername(), confirmationUrl);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Verifică email-ul pentru a confirma schimbarea parolei");
    }
}
