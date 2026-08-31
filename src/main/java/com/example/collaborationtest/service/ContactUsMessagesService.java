package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.contact.ContactUsMessagesRequestDTO;
import com.example.collaborationtest.dto.contact.ContactUsMessagesResponseDTO;
import com.example.collaborationtest.enums.MessageStatus;
import com.example.collaborationtest.mapper.ContactUsMessagesMapper;
import com.example.collaborationtest.model.ContactUsMessages;
import com.example.collaborationtest.repository.ContactUsMessagesRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactUsMessagesService {
    private ContactUsMessagesRepo contactUsMessagesRepo;
    private final ContactUsMessagesMapper contactUsMessagesMapper;
    private final EmailService emailService;

    public ContactUsMessagesService(ContactUsMessagesRepo contactUsMessagesRepo,
                                    ContactUsMessagesMapper contactUsMessagesMapper,
                                    EmailService emailService) {
        this.contactUsMessagesRepo = contactUsMessagesRepo;
        this.contactUsMessagesMapper = contactUsMessagesMapper;
        this.emailService = emailService;
    }

    public List<ContactUsMessagesResponseDTO> getContactUsMessages() {
        return contactUsMessagesMapper.toResponseList(contactUsMessagesRepo.findAll());
    }

    /**
     * Persists a contact-us message and fires the confirmation + admin-notification
     * emails. Keeping the email side-effects here means the controller works purely
     * with DTOs and never handles the entity.
     */
    public ContactUsMessagesResponseDTO addContactUsMessage(ContactUsMessagesRequestDTO request) {
        ContactUsMessages saved = contactUsMessagesRepo.save(contactUsMessagesMapper.toEntity(request));

        emailService.sendContactResponseEmail(saved);
        emailService.sendNewContactMessageToAdmins(saved);

        return contactUsMessagesMapper.toResponse(saved);
    }

    public ContactUsMessagesResponseDTO updateStatus(int id, MessageStatus status) {
        ContactUsMessages contactUsMessages = contactUsMessagesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesajul nu a fost găsit"));

        contactUsMessages.setStatus(status);

        return contactUsMessagesMapper.toResponse(contactUsMessagesRepo.save(contactUsMessages));
    }
}
