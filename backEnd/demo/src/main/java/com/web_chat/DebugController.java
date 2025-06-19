package com.web_chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @GetMapping("/debug")
    public String debug() {
        System.out.println("✅ Debug endpoint called on port 1234!");
        return "✅ Server is running on port 1234! WebSocket should be at ws://localhost:1234/ws";
    }
}