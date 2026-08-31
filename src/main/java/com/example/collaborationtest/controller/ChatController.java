package com.example.collaborationtest.controller;

import com.example.collaborationtest.dto.chat.ChatRequestDTO;
import com.example.collaborationtest.dto.chat.ChatResponseDTO;
import com.example.collaborationtest.service.ChatBotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public chatbot endpoint. The visitor describes a problem and receives a
 * recommendation of real catalog products.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatBotService chatBotService;

    public ChatController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping
    public ResponseEntity<ChatResponseDTO> chat(@Valid @RequestBody ChatRequestDTO request) {
        return ResponseEntity.ok(chatBotService.recommend(request.message()));
    }
}
