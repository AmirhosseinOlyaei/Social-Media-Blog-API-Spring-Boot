package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessageById(Integer id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        return optionalMessage.orElse(null);
    }

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Integer id, Message message) {
        // Check if the message exists in the database
        if (messageRepository.existsById(id)) {

            // Check if the message text is empty
            if (message.getMessage_text().isEmpty()) {
                throw new IllegalArgumentException("Message text cannot be empty.");
            }

            // Check if the message text exceeds 255 characters
            if (message.getMessage_text().length() > 255) {
                throw new IllegalArgumentException("Message text exceeds the allowable limit.");
            }

            // If the checks pass, save the message and return
            return messageRepository.save(message);
        }
        return null; // If the message doesn't exist, return null
    }

    @Override
    public void deleteMessage(Integer id) {
        messageRepository.deleteById(id);
    }

    @Override
    public Message createMessage(Message message) {
        // Validation and business logic can be added here
        return messageRepository.save(message);
    }

    @Override
    public int deleteMessageById(Integer messageId) {
        return messageRepository.deleteMessageById(messageId);
    }

    @Override
    public List<Message> getAllMessagesForUser(Integer userId) {
        return messageRepository.findAllByPosted_by(userId);
    }

}
