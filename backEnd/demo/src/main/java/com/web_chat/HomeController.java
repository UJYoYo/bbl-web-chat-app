// Controller for handling home page
package com.web_chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    public static class userBody {
        public String username;
        public String password;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody userBody user) {
        return "Registering: " + user.username + " with password: " + user.password;
    }
    
}
