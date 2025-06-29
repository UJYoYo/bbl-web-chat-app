package com.web_chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web_chat.dto.ApiResponse;
import com.web_chat.entity.FriendRequest;
import com.web_chat.entity.User;
import com.web_chat.service.FriendRequestService;
import com.web_chat.service.UserService;
@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendRequestService friendRequestService;
    
    @Autowired
    private UserService userService;

    // Request body classes
    public static class SendRequestBody {
        private String recipientUsername;
        private String senderUsername;
        private String status;

        public SendRequestBody() {
        }

        // Getters and setters
        public String getRecipientUsername() {
            return recipientUsername;
        }

        public void setRecipientUsername(String recipientUsername) {
            this.recipientUsername = recipientUsername;
        }

        public String getSenderUsername() {
            return senderUsername;
        }

        public void setSenderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    public static class FriendBody {
        private Integer userId;
        private String username;
        private Integer roomId;

        public FriendBody() {}

        public Integer getUserId() { return userId; }
        public Integer getRoomId() { return roomId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public void setRoomId(Integer roomId) { this.roomId = roomId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
    
    public static class HandleRequestBody {
        private Integer senderId;
        private Integer recipientId;
        private String status;
        
        public HandleRequestBody() {}
        
        public Integer getSenderId() { return senderId; }
        public Integer getRecipientId() { return recipientId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // Function to search for a user in db
    // /friends/getUser?username=john
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam("username") String username) {
        try {
            User user = userService.findByUsername(username);
            
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                ApiResponse response = new ApiResponse("User not found", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Error searching for user: " + e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Function to send a friend request after searching for a user
    @PostMapping("/sendRequest")
    public ResponseEntity<ApiResponse> sendRequest(@RequestBody SendRequestBody request) {
        try {
            // Find sender and recipient by username
            User sender = userService.findByUsername(request.getSenderUsername());
            User recipient = userService.findByUsername(request.getRecipientUsername());
            
            if (sender == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse("Sender username not found", false));
            }
            
            if (recipient == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse("Recipient username not found", false));
            }
            
            // Send friend request using the service
            FriendRequest friendRequest = friendRequestService.sendFriendRequest(
                sender.getUserId(), 
                recipient.getUserId()
            );
            
            ApiResponse response = new ApiResponse(
                "Friend request sent to " + request.getRecipientUsername() + 
                " from " + request.getSenderUsername(), 
                true, 
                friendRequest.getRequestId()
            );
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to send friend request: " + e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    
    // Get only pending requests received by a user
    @GetMapping("/getPendingRequests")
    public ResponseEntity<?> getPendingRequests(@RequestParam("username") String username) {
        try {
            User user = userService.findByUsername(username);
            
            if (user == null) {
                ApiResponse response = new ApiResponse("User not found", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Get pending requests that this user received
            List<FriendRequest> pendingRequests = friendRequestService.getPendingRequestsForUser(user.getUserId());
            
            List<FriendBody> pendingFriends = new ArrayList<>();
            User tempUser;
            FriendBody temp;
            // Loop through the pending requests
            for (FriendRequest request : pendingRequests) {
                // Check if the recipient is the user
                if (request.getRecipientId().equals(user.getUserId())) {
                    // Find username of the sender
                    tempUser = userService.findByUserId(request.getSenderId());
                } else {
                    tempUser = userService.findByUserId(request.getRecipientId());
                }
                // Create a FriendBody object for the pending friend
                temp = new FriendBody();
                temp.setUserId(tempUser.getUserId());
                temp.setUsername(tempUser.getUsername());
                // Add the pending friend to the list
                pendingFriends.add(temp);

            }
            //return pendingRequests;
            return ResponseEntity.ok(pendingFriends);
            
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Error retrieving pending requests: " + e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // POST Function to accept or reject a friend request
    @PostMapping("/requests")
    public ResponseEntity<ApiResponse> handleRequest(@RequestBody HandleRequestBody request) {
        try {
            if (null == request.getStatus()) {
                ApiResponse response = new ApiResponse(
                        "Invalid status. Use 'accepted' or 'rejected'",
                        false);
                return ResponseEntity.badRequest().body(response);
            } else
                switch (request.getStatus()) {
                    case "accepted" -> {
                        // Convert HandleRequestBody to FriendRequest
                        FriendRequest temp = new FriendRequest();
                        temp.setSenderId(request.getSenderId());
                        temp.setRecipientId(request.getRecipientId());
                        temp.setStatus("accepted");
                        // Accept the friend request
                        FriendRequest friendRequest = friendRequestService.acceptFriendRequest(temp);
                        
                        ApiResponse response = new ApiResponse(
                                "Friend request accepted!",
                                true,
                                friendRequest.getRequestId());
                        return ResponseEntity.ok(response);

                    }
                    case "rejected" -> {
                        // Convert HandleRequestBody to FriendRequest
                        FriendRequest temp = new FriendRequest();
                        temp.setSenderId(request.getSenderId());
                        temp.setRecipientId(request.getRecipientId());
                        temp.setStatus("rejected");
                        // Reject the friend request
                        FriendRequest friendRequest = friendRequestService.rejectFriendRequest(temp);

                        ApiResponse response = new ApiResponse(
                                "Friend request rejected!",
                                true,
                                friendRequest.getRequestId());
                        return ResponseEntity.ok(response);

                    }
                    default -> {
                        ApiResponse response = new ApiResponse(
                                "Invalid status. Use 'accepted' or 'rejected'",
                                false);
                        return ResponseEntity.badRequest().body(response);
                    }
                }

        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Failed to handle friend request: " + e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Function to get all friends of a user
    @GetMapping("/getFriends")
    public ResponseEntity<?> getFriends(@RequestParam("username") String username) {
        try {
            User user = userService.findByUsername(username);

            if (user == null) {
                ApiResponse response = new ApiResponse("User not found", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Get all friends of the user
            List<FriendRequest> requestRequests = friendRequestService.getAcceptedFriends(user.getUserId());
            List<FriendBody> friends = new ArrayList<>();
            User tempUser;
            FriendBody temp;

            // Loop through the friends list checking if the recipient is the user
            for (FriendRequest requests : requestRequests) {
                if (requests.getRecipientId().equals(user.getUserId())) {
                    // Find username of the sender
                    tempUser = userService.findByUserId((requests.getSenderId()));
                } else {
                    tempUser = userService.findByUserId((requests.getRecipientId()));
                }
                // Create a FriendBody object for the friend
                temp = new FriendBody();
                temp.setUserId(tempUser.getUserId());
                temp.setUsername(tempUser.getUsername());
                temp.setRoomId(requests.getRequestId()); 
                // Add the friend to the list
                friends.add(temp);
            }

            return ResponseEntity.ok(friends);

        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Error retrieving friends: " + e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}