package com.web_chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web_chat.entity.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    
    // Find requests sent by a specific user
    List<FriendRequest> findBySenderId(Integer senderId);
    
    // Find requests received by a specific user
    List<FriendRequest> findByRecipientId(Integer recipientId);
    
    // Find requests by status
    List<FriendRequest> findByStatus(String status);
    
    // Find pending requests for a specific recipient
    List<FriendRequest> findByRecipientIdAndStatus(Integer recipientId, String status);
    
    // Find pending requests sent by a specific user
    List<FriendRequest> findBySenderIdAndStatus(Integer senderId, String status);
    
    // Check if a friend request already exists between two users
    Optional<FriendRequest> findBySenderIdAndRecipientId(Integer senderId, Integer recipientId);
    
    // Check if there's any request between two users (either direction)
    List<FriendRequest> findBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
        Integer senderId1, Integer recipientId1, Integer senderId2, Integer recipientId2);
}