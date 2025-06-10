// Controller for managing friends in a web chat application
package com.web_chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendController {

    public static class sendRequestBody {
        public String recipientUsername;
        public String senderUsername;
        public String status;
    }

    // Funtcions to search for a user in db
    // /friends/getUser?username=john
    @GetMapping("/getUser")
    public String getUser(@RequestParam("username") String username) {
        // search for the user in the database
        return "Searching for: " + username;
    }

    // Function to send a friend request after searching for a user
    @PostMapping("/sendRequest")
    public String sendRequest(@RequestBody sendRequestBody request) {
        request.status = "pending"; // Set initial status to pending
        return "Friend request sent to: " + request.recipientUsername 
               + " from: " + request.senderUsername 
               + " with status: " + request.status;
    }

    // Function to get all friend requests for a user
    // /friends/getRequests?username=john
    @GetMapping("/getRequests")
    public String getRequests(@RequestParam("username") String username) {
        // GET request to retrieve all friend requests for the user
        return "Retrieving friend requests for: " + username;
    }

    // POST Function to accept or reject a friend request
    @PostMapping("/requests")
    public String request(@RequestBody sendRequestBody request) {
        if (request.status.equals("accepted")) {
            // Update the status of the friend request in the database
            return "Friend request accepted.";
        } else if (request.status.equals("rejected")) {
            // Delete  friend request in the database
            return "Friend request rejected.";
        } else {
            return "";
        }
    }
    
}
