package com.example.collaborationtest.controller;

import com.example.collaborationtest.enums.MessageStatus;
import com.example.collaborationtest.model.ContactUsMessages;
import com.example.collaborationtest.service.ContactUsMessagesService;
import com.example.collaborationtest.service.EmailService;
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
    public List<ContactUsMessages> getAllContactUsMessages() {
        return contactUsMessagesService.getContactUsMessages();
    }

    @PutMapping("/add")
    public ContactUsMessages addContactUsMessages(@RequestBody ContactUsMessages contactUsMessages) {
        ContactUsMessages messageAdded= contactUsMessagesService.addContactUsMessage(contactUsMessages);
        if(messageAdded!=null) {
            this.emailService.sendContactResponseEmail(messageAdded);
            this.emailService.sendNewContactMessageToAdmins(messageAdded);
        }
        return messageAdded;
    }

    @PatchMapping("/admin/update/status")
    public ContactUsMessages updateContactUsMessages(   @RequestParam int id,
                                                        @RequestParam MessageStatus status,
                                                        @RequestParam String message) {
        ContactUsMessages updateMessage= contactUsMessagesService.updateStatus(id, status);
        if (updateMessage != null) {

            String domainPart = updateMessage.getEmail().split("@")[1];
            String provider = domainPart.split("\\.")[0];
            String emailContent =  """
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

            emailService.sendEmail(updateMessage.getEmail(), "Raspuns la mesaj- Terra Bucovina", emailContent, provider);

        }
        return updateMessage;
    }


}
