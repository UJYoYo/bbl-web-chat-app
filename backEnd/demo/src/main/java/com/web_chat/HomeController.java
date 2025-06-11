package com.web_chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web_chat.dto.ApiResponse;
import com.web_chat.entity.User;
import com.web_chat.service.UserService;

@RestController
public class HomeController {

    @Autowired
    private UserService userService;

    // Request body class
    public static class UserBody {
        private String username;
        private String password;
        
        // Default constructor
        public UserBody() {}
        
        // Getters and setters
        public String getUsername() { 
            return username; 
        }
        
        public void setUsername(String username) { 
            this.username = username; 
        }
        
        public String getPassword() { 
            return password; 
        }
        
        public void setPassword(String password) { 
            this.password = password; 
        }
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserBody userBody) {
        try {
            // Validate input
            if (userBody.getUsername() == null || userBody.getUsername().trim().isEmpty()) {
                ApiResponse response = new ApiResponse("Username is required", false);
                return ResponseEntity.badRequest().body(response);
            }
            
            if (userBody.getPassword() == null || userBody.getPassword().trim().isEmpty()) {
                ApiResponse response = new ApiResponse("Password is required", false);
                return ResponseEntity.badRequest().body(response);
            }
            
            // Register user
            User user = userService.registerUser(userBody.getUsername().trim(), userBody.getPassword());
            
            ApiResponse response = new ApiResponse("User registered successfully!", true, user.getUserId());
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse("Registration failed: " + e.getMessage(), false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}