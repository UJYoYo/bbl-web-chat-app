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
    public List<MessageEntity> getChatHistory(Integer roomId)
    {
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        List<MessageEntity> messages = messageRepository.findByRoomId(roomId);

        if (messages.isEmpty()) {
            throw new RuntimeException("No messages found for the given room ID");
        }
        return messages;
    }
}