package com.web_chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web_chat.entity.MessageEntity;
import com.web_chat.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Get chat history between two users
    public List<MessageEntity> getChatHistory(Integer roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        List<MessageEntity> messages = messageRepository.findByRoomId(roomId);

        if (messages.isEmpty()) {
            throw new RuntimeException("No messages found for the given room ID");
        }
        return messages;
    }

    // Send a message
    public MessageEntity sendMessage(MessageEntity message) {
        // INPUT VALIDATION: Check all required fields
        if (message.getRoomId() == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        if (message.getSenderId() == null) {
            throw new IllegalArgumentException("Sender ID cannot be null");
        }
        if (message.getRecipientId() == null) {
            throw new IllegalArgumentException("Recipient ID cannot be null");
        }
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }
        

        // CALL REPOSITORY: Save message to database
        return messageRepository.save(message);
    }
}