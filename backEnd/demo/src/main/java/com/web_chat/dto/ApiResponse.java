package com.web_chat.dto;

public class ApiResponse {
    private String message;
    private boolean success;
    private Integer userId;
    
    // Default constructor
    public ApiResponse() {}
    
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    public ApiResponse(String message, boolean success, Integer userId) {
        this.message = message;
        this.success = success;
        this.userId = userId;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}