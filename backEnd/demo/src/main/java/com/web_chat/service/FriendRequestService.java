package com.web_chat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web_chat.entity.FriendRequest;
import com.web_chat.repository.FriendRequestRepository;

@Service
public class FriendRequestService {
    
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    
    // Send a friend request
    public FriendRequest sendFriendRequest(Integer senderId, Integer recipientId) {
        // Check if request already exists
        Optional<FriendRequest> existingRequest = friendRequestRepository
            .findBySenderIdAndRecipientId(senderId, recipientId);
        
        if (existingRequest.isPresent()) {
            throw new RuntimeException("Friend request already sent");
        }
        
        // Check if there's a reverse request (recipient has already sent to sender)
        Optional<FriendRequest> reverseRequest = friendRequestRepository
            .findBySenderIdAndRecipientId(recipientId, senderId);
        
        if (reverseRequest.isPresent()) {
            throw new RuntimeException("This user has already sent you a friend request");
        }
        
        // Don't allow users to send requests to themselves
        if (senderId.equals(recipientId)) {
            throw new RuntimeException("Cannot send friend request to yourself");
        }
        
        // Create new friend request
        FriendRequest friendRequest = new FriendRequest(senderId, recipientId, "pending");
        return friendRequestRepository.save(friendRequest);
    }
    
    // Accept a friend request
    public FriendRequest acceptFriendRequest(Integer requestId) {
        Optional<FriendRequest> optionalRequest = friendRequestRepository.findById(requestId);
        
        if (!optionalRequest.isPresent()) {
            throw new RuntimeException("Friend request not found");
        }
        
        FriendRequest friendRequest = optionalRequest.get();
        
        if (!"pending".equals(friendRequest.getStatus())) {
            throw new RuntimeException("Friend request is not pending");
        }
        
        friendRequest.setStatus("accepted");
        return friendRequestRepository.save(friendRequest);
    }
    
    // Reject a friend request
    public FriendRequest rejectFriendRequest(Integer requestId) {
        Optional<FriendRequest> optionalRequest = friendRequestRepository.findById(requestId);
        
        if (!optionalRequest.isPresent()) {
            throw new RuntimeException("Friend request not found");
        }
        
        FriendRequest friendRequest = optionalRequest.get();
        
        if (!"pending".equals(friendRequest.getStatus())) {
            throw new RuntimeException("Friend request is not pending");
        }
        
        friendRequest.setStatus("rejected");
        return friendRequestRepository.save(friendRequest);
    }
    
    // Get pending requests for a user (requests they received)
    public List<FriendRequest> getPendingRequestsForUser(Integer userId) {
        return friendRequestRepository.findByRecipientIdAndStatus(userId, "pending");
    }
    
    // Get sent requests by a user
    public List<FriendRequest> getSentRequestsByUser(Integer userId) {
        return friendRequestRepository.findBySenderId(userId);
    }
    
    // Get all requests for a user (sent and received)
    public List<FriendRequest> getAllRequestsForUser(Integer userId) {
        List<FriendRequest> sentRequests = friendRequestRepository.findBySenderId(userId);
        List<FriendRequest> receivedRequests = friendRequestRepository.findByRecipientId(userId);
        
        sentRequests.addAll(receivedRequests);
        return sentRequests;
    }
    
    // Delete/Cancel a friend request
    public void cancelFriendRequest(Integer requestId, Integer userId) {
        Optional<FriendRequest> optionalRequest = friendRequestRepository.findById(requestId);

        if (!optionalRequest.isPresent()) {
            throw new RuntimeException("Friend request not found");
        }

        FriendRequest friendRequest = optionalRequest.get();

        // Only sender can cancel their own request
        if (!friendRequest.getSenderId().equals(userId)) {
            throw new RuntimeException("You can only cancel your own friend requests");
        }

        friendRequestRepository.delete(friendRequest);
    }
    
    // Get accepted friends for a user
    public List<FriendRequest> getAcceptedFriends(Integer userId) {
        List<FriendRequest> sentRequests = friendRequestRepository.findBySenderIdAndStatus(userId, "accepted");
        List<FriendRequest> receivedRequests = friendRequestRepository.findByRecipientIdAndStatus(userId, "accepted");
        sentRequests.addAll(receivedRequests);
        return sentRequests;
    }
}