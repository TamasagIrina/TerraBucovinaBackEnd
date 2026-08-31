package com.example.collaborationtest.controller;


import com.example.collaborationtest.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String to,
                                            @RequestParam String customerName,
                                            @RequestParam String subject,
                                            @RequestParam String body) {
        emailService.sendEmail(to, customerName, subject, body);
        return ResponseEntity.ok("Email trimis către " + to);
    }



}
