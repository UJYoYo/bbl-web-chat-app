package com.web_chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; 

@Entity
@Table(name = "friend_requests")
public class FriendRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;
    
    @Column(name = "sender_id")
    private Integer senderId;
    
    @Column(name = "recipient_id")
    private Integer recipientId;

    @Column(name = "status")
    private String status;
    
    // Default constructor
    public FriendRequest() {}
    
    // Constructor
    public FriendRequest(Integer senderId, Integer recipientId, String status) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.status = status;
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
    public Integer getRequestId() {
        return requestId;
    }
    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}