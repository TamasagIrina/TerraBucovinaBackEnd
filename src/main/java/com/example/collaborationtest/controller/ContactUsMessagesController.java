package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.contact.ContactUsMessagesRequestDTO;
import com.example.collaborationtest.dto.contact.ContactUsMessagesResponseDTO;
import com.example.collaborationtest.enums.MessageStatus;
import com.example.collaborationtest.service.ContactUsMessagesService;
import com.example.collaborationtest.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact/us")
public class ContactUsMessagesController {

    private ContactUsMessagesService contactUsMessagesService;
    private EmailService emailService;

    public ContactUsMessagesController(ContactUsMessagesService contactUsMessagesService, EmailService emailService) {
        this.contactUsMessagesService = contactUsMessagesService;
        this.emailService = emailService;
    }

    @GetMapping("/admin/get/all")
    public ResponseEntity<List<ContactUsMessagesResponseDTO>> getAllContactUsMessages() {
        return ResponseEntity.ok(contactUsMessagesService.getContactUsMessages());
    }

    @PutMapping("/add")
    public ResponseEntity<ContactUsMessagesResponseDTO> addContactUsMessages(
            @Valid @RequestBody ContactUsMessagesRequestDTO request) {
        // Persisting also fires the confirmation + admin-notification emails.
        return ResponseEntity.status(HttpStatus.CREATED).body(contactUsMessagesService.addContactUsMessage(request));
    }

    @PatchMapping("/admin/update/status")
    public ResponseEntity<ContactUsMessagesResponseDTO> updateContactUsMessages(@RequestParam int id,
                                                                                @RequestParam MessageStatus status,
                                                                                @RequestParam String message) {
        ContactUsMessagesResponseDTO updateMessage = contactUsMessagesService.updateStatus(id, status);
        if (updateMessage != null) {

            String emailContent = """
                                    <html>
                                        <head>
                                            <meta charset="UTF-8">
                                        </head>
                                        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                            <h2 style="color: #2c3e50;"><b>%s</b>!</h2>

                                            <p style="margin-top: 30px;">Echipa Terra Bucovina</p>
                                        </body>
                                    </html>
                                """.formatted(message);

            emailService.sendEmail(updateMessage.email(), updateMessage.name(),
                    "Raspuns la mesaj- Terra Bucovina", emailContent);

        }
        return ResponseEntity.ok(updateMessage);
    }
}
