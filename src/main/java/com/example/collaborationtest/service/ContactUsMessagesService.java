package com.example.collaborationtest.service;

import com.example.collaborationtest.enums.MessageStatus;
import com.example.collaborationtest.model.ContactUsMessages;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.repository.ContactUsMessagesRepo;
import com.example.collaborationtest.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactUsMessagesService {
    private ContactUsMessagesRepo contactUsMessagesRepo;

    public ContactUsMessagesService(ContactUsMessagesRepo contactUsMessagesRepo) {
       this.contactUsMessagesRepo = contactUsMessagesRepo;
    }

    public List<ContactUsMessages> getContactUsMessages() {
      return contactUsMessagesRepo.findAll();
    }

    public ContactUsMessages addContactUsMessage(ContactUsMessages contactUsMessages) {
        return contactUsMessagesRepo.save(contactUsMessages);
    }
    public ContactUsMessages updateContactUsMessage(ContactUsMessages contactUsMessages) {
        return contactUsMessagesRepo.save(contactUsMessages);
    }
    public ContactUsMessages deleteContactUsMessage(ContactUsMessages contactUsMessages) {
        contactUsMessagesRepo.delete(contactUsMessages);
        return contactUsMessages;
    }

    public ContactUsMessages updateStatus(int id, MessageStatus status ) {
        ContactUsMessages contactUsMessages = contactUsMessagesRepo.findById(id).get();
        contactUsMessages.setStatus(status);
        return contactUsMessages;
    }
}
