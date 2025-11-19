package com.example.collaborationtest.repository;

import com.example.collaborationtest.model.ContactUsMessages;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactUsMessagesRepo extends JpaRepository<ContactUsMessages, Integer> {
}
