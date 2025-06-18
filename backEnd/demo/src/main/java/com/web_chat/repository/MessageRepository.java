package com.web_chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web_chat.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    // Find messages by room ID
    List<MessageEntity> findByRoomId(Integer roomId); 
    
}