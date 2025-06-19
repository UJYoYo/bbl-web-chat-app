package com.web_chat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.web_chat.entity.MessageEntity;
import com.web_chat.service.MessageService;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private MessageService messageService;

    // Use the same DTO as your REST controller
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

        // Getters and Setters
        public Integer getMessageId() {  
            return messageId;
        }

        public void setMessageId(Integer messageId) { 
            this.messageId = messageId;
        }

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

    /**
     * Handle real-time message sending via WebSocket
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            // Convert WebSocket DTO to MessageEntity (same as your REST controller)
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setRoomId(chatMessage.getRoomId());
            messageEntity.setSenderId(chatMessage.getSenderId());
            messageEntity.setRecipientId(chatMessage.getRecipientId());
            messageEntity.setContent(chatMessage.getContent());

            // Use your existing MessageService to save to database
            MessageEntity savedMessage = messageService.sendMessage(messageEntity);
            
            // Convert saved entity back to DTO for broadcasting
            ChatMessage responseMessage = new ChatMessage(
                savedMessage.getRoomId(),
                savedMessage.getSenderId(),
                savedMessage.getRecipientId(),
                savedMessage.getContent()
            );
            responseMessage.setMessageId(savedMessage.getMessageId());

            // Broadcast message to ALL users subscribed to this room
            messagingTemplate.convertAndSend(
                "/topic/room/" + savedMessage.getRoomId(), 
                responseMessage
            );
            
        } catch (Exception e) {
            System.err.println("Error sending message via WebSocket: " + e.getMessage());
            // Optionally, you could send an error message back to the sender
        }
    }

    /**
     * Handle getting chat history via WebSocket
     */
    @MessageMapping("/chat.getHistory")
    public void getChatHistory(@Payload ChatMessage historyRequest) {
        try {
            // Get chat history using your existing service
            List<MessageEntity> messages = messageService.getChatHistory(historyRequest.getRoomId());
            
            // Convert each MessageEntity to ChatMessage DTO
            for (MessageEntity message : messages) {
                ChatMessage historyMessage = new ChatMessage(
                    message.getRoomId(),
                    message.getSenderId(),
                    message.getRecipientId(),
                    message.getContent()
                );
                historyMessage.setMessageId(message.getMessageId());
                
                // Send each historical message to the requesting user only
                messagingTemplate.convertAndSendToUser(
                    historyRequest.getSenderId().toString(),
                    "/queue/history",
                    historyMessage
                );
            }
            
            // Send completion notification
            ChatMessage completionMessage = new ChatMessage();
            completionMessage.setContent("Chat history loaded (" + messages.size() + " messages)");
            messagingTemplate.convertAndSendToUser(
                historyRequest.getSenderId().toString(),
                "/queue/notifications",
                completionMessage
            );
            
        } catch (RuntimeException e) {
            // Handle case when no messages found
            ChatMessage noHistoryMessage = new ChatMessage();
            noHistoryMessage.setContent("No previous messages in this room");
            messagingTemplate.convertAndSendToUser(
                historyRequest.getSenderId().toString(),
                "/queue/notifications",
                noHistoryMessage
            );
        } catch (Exception e) {
            // Handle other errors
            ChatMessage errorMessage = new ChatMessage();
            errorMessage.setContent("Error loading chat history: " + e.getMessage());
            messagingTemplate.convertAndSendToUser(
                historyRequest.getSenderId().toString(),
                "/queue/notifications",
                errorMessage
            );
        }
    }
}