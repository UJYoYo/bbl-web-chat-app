package com.web_chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web_chat.entity.MessageEntity;
import com.web_chat.repository.MessageRepository;

@RestController
@RequestMapping("/chat")
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

    // @MessageMapping("/chat.send")
    // @SendTo("/topic/public")
    // public ChatMessage sendMessage(ChatMessage message) {
    //     // Save message to database
    //     MessageEntity entity = new MessageEntity();
    //     entity.setSenderId(message.getSenderId());
    //     entity.setRecipientId(message.getRecipientId());
    //     entity.setContent(message.getContent());
    //     entity.setRoomId(message.getRoomId()); // Changed from String.valueOf() to direct Integer
    //     entity.setTimestamp(new java.util.Date());
        
    //     messageRepository.save(entity);
        
    //     return message;
    // }

    @GetMapping("/getHistory")
    public List<MessageEntity> getChatHistory(Integer roomId) {
        return messageRepository.findByRoomId(roomId);
    }
}