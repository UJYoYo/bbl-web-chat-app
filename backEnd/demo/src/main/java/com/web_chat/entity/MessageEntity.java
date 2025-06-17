package com.web_chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "message") 
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageID") 
    private Integer messageId; 

    @Column(name = "senderID") 
    private Integer senderId;  

    @Column(name = "recipientID") 
    private Integer recipientId; 

    @Column(name = "message") 
    private String content;

    @Column(name = "roomID") 
    private Integer roomId;
      
    // Constructor
    public MessageEntity() {
    }
    public MessageEntity(Integer senderId, Integer recipientId, String content, Integer roomId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.roomId = roomId;
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