package com.web_chat.repository;

import com.web_chat.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByRoomIdOrderByTimestampAsc(Integer roomId); // Parameter must be Integer
}