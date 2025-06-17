package com.web_chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web_chat.entity.MessageEntity;
import com.web_chat.repository.MessageRepository;

@RestController 
@RequestMapping("/chat")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // DTO class for sending messages
    public static class ChatMessage {
        private Integer senderId;
        private Integer recipientId;
        private String content;
        private Integer roomId;
        private Integer messageId;

        // Default constructor
        public ChatMessage() {}

        // Constructor
        public ChatMessage(Integer roomId, Integer senderId, Integer recipientId, String content) {
            this.roomId = roomId;
            this.senderId = senderId;
            this.recipientId = recipientId;
            this.content = content;

        }

        public Integer getMessageId() {  
            return messageId;
        }

        public void setMessageId(Integer messageId) { 
            this.messageId = messageId;
        }

        // Getters and Setters
        public Integer getSenderId() {
            return senderId;
        }

        public void setSenderId(Integer senderId) {
            this.senderId = senderId;
        }

        public Integer getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(Integer recipientId) {
            this.recipientId = recipientId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getRoomId() {
            return roomId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
        }
    }

    @GetMapping("/getHistory")
    public ResponseEntity<?> getChatHistory(@RequestParam("roomId") Integer roomId) {
        try {
            List<MessageEntity> messages = messageRepository.findByRoomId(roomId);
            
            
            return ResponseEntity.ok(messages);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to get chat history: " + e.getMessage());
            errorResponse.put("roomId", roomId);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}