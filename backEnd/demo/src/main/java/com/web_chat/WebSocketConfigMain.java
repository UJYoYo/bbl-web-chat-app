package com.web_chat;

import com.web_chat.service.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfigMain implements WebSocketConfigurer {

    @Autowired
    private ChatWebSocketHandler ChatWebSocketHandler;

    public WebSocketConfigMain() {
        System.out.println("🚀🚀🚀 MAIN WEBSOCKET CONFIG CONSTRUCTOR CALLED! 🚀🚀🚀");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("🔧🔧🔧 MAIN PACKAGE: REGISTERING WEBSOCKET HANDLERS! 🔧🔧🔧");
        System.out.println("ChatWebSocketHandler: " + ChatWebSocketHandler);

        try {
            registry.addHandler(ChatWebSocketHandler, "/ws")
                    .setAllowedOrigins("*")
                    .setAllowedOriginPatterns("*");

            System.out.println("✅✅✅ MAIN PACKAGE: WEBSOCKET SUCCESSFULLY REGISTERED AT /ws! ✅✅✅");
        } catch (Exception e) {
            System.err.println("❌❌❌ MAIN PACKAGE: ERROR REGISTERING WEBSOCKET: " + e.getMessage());
            e.printStackTrace();
        }
    }
}