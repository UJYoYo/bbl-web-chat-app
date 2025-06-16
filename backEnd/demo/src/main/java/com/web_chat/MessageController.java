package com.web_chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.web_chat.entity.MessageEntity;
import com.web_chat.repository.MessageRepository;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    public static class ChatMessage {
        private Integer senderId;
        private Integer recipientId;
        private String content;
        private Integer roomId;

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

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        // Save message to database
        MessageEntity entity = new MessageEntity();
        entity.setSenderId(message.getSenderId());
        entity.setRecipientId(message.getRecipientId());
        entity.setContent(message.getContent());
        entity.setRoomId(message.getRoomId()); // Changed from String.valueOf() to direct Integer
        entity.setTimestamp(new java.util.Date());
        
        messageRepository.save(entity);
        
        return message;
    }

    @MessageMapping("/chat.history")
    @SendTo("/topic/history")
    public List<MessageEntity> getHistory(Integer roomId) { // Changed parameter type from String to Integer
        // Retrieve actual history from database
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}