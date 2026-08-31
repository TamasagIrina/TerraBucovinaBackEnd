package com.example.collaborationtest.mapper;

import com.example.collaborationtest.dto.contact.ContactUsMessagesRequestDTO;
import com.example.collaborationtest.dto.contact.ContactUsMessagesResponseDTO;
import com.example.collaborationtest.model.ContactUsMessages;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts between {@link ContactUsMessages} and its DTOs. Status is left at
 * the entity default on creation and managed by the service afterwards.
 */
@Component
public class ContactUsMessagesMapper {

    public ContactUsMessages toEntity(ContactUsMessagesRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return ContactUsMessages.builder()
                .name(dto.name())
                .email(dto.email())
                .phone_number(dto.phone_number())
                .subject(dto.subject())
                .message(dto.message())
                .build();
    }

    public ContactUsMessagesResponseDTO toResponse(ContactUsMessages message) {
        if (message == null) {
            return null;
        }
        return new ContactUsMessagesResponseDTO(
                message.getId(),
                message.getName(),
                message.getEmail(),
                message.getPhone_number(),
                message.getSubject(),
                message.getMessage(),
                message.getStatus()
        );
    }

    public List<ContactUsMessagesResponseDTO> toResponseList(List<ContactUsMessages> messages) {
        return Optional.ofNullable(messages).orElseGet(List::of)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
