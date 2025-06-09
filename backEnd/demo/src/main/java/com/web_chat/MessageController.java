package com.web_chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/messages")
public class MessageController {
    
    public static class messageBody {
        public String senderId;
        public String recipientId;
        public String message;
    }

    // Funtcions to search for a user in db
    // http://localhost:8080/messages/getHistory?roomId=111111
    @GetMapping("/getHistory")
    public String getHistory(@RequestParam("roomId") String roomId) {
        // search for the user in the database
        return "Getting history for room with roomId: " + roomId;
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody messageBody message) {
        return "Message sent from: " + message.senderId 
                + " to: " + message.recipientId 
                + " with content: " + message.message;
    }
    
}
