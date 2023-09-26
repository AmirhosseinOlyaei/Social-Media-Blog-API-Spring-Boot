package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.entity.Message;
import com.example.service.MessageService;
import com.example.repository.AccountRepository;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        // Validate message text
        if (message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 254) {
            return ResponseEntity.badRequest().build();
        }

        // TODO: Validate if 'posted_by' user exists in the database. Return 400 if not.
        if (!accountRepository.existsById(message.getPosted_by())) {
            return ResponseEntity.badRequest().build();
        }

        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);
    }
}
