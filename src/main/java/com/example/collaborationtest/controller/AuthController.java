package com.example.collaborationtest.controller;

import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.service.EmailService;
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

    private final EmailService emailService;

    private final PasswordEncoder encoder;

    public AuthController(UserService userService, PasswordEncoder encoder, EmailService emailService) {
        this.userService = userService;
        this.encoder = encoder;
        this.emailService = emailService;

    }


    @PostMapping("/register")
    public ResponseEntity<?> register( @RequestBody User user) {
        // Create new user via UserService

       if (this.userService.getUser(user.getEmail())==null){
           User newUser = new User();
           newUser.setUsername(user.getUsername());
           newUser.setEmail(user.getEmail());
           newUser.setPassword(user.getPassword());
           newUser.setRoles(   Collections.singleton(Role.USER));
           newUser.setEnabled(true);
           User savedUser = userService.createUser(newUser);
           if (savedUser == null) {
               return ResponseEntity.badRequest().body("Username already exists");
           }
       }

        String domainPart = user.getEmail().split("@")[1];
        String provider = domainPart.split("\\.")[0];
        String emailContent =  """
                                    <html>
                                        <head>
                                            <meta charset="UTF-8">
                                        </head>
                                        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                            <h2 style="color: #2c3e50;">Salut <b>%s</b>!</h2>
                                            <p>Contul tau a fost creat cu succes.</p>
                                            <p style="margin-top: 20px;">
                                                Iti multumim ca ti-ai facut cont la <strong>Terra Bucovina</strong>!<br>
                                                Ne bucuram sa te avem în comunitatea noastra.
                                            </p>
                                            <p style="margin-top: 30px;">Echipa Terra Bucovina</p>
                                        </body>
                                    </html>
                                """.formatted(user.getUsername());

        emailService.sendEmail(user.getEmail(), "Confirmare cont", emailContent, provider);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody User user) {

        User vrfUser = new User();
        vrfUser.setEmail(user.getEmail());
        vrfUser.setPassword(user.getPassword());
        vrfUser.setUsername(this.userService.getUser(user.getEmail()).getUsername());
       

        String token = userService.verify(vrfUser);

        if ("Verification failed".equals(token)) {
            return ResponseEntity.ok("Invalid username or password");
        }

        return ResponseEntity.ok(token);
    }
}
