package com.web_chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.web_chat", "com.web_chat.config", "com.web_chat.service" })
public class App {
    public static void main(String[] args) {
        System.out.println("🚀 Starting Spring Boot Application with WebSocket support...");
        SpringApplication.run(App.class, args);
        System.out.println("✅ Application started successfully!");
    }
}