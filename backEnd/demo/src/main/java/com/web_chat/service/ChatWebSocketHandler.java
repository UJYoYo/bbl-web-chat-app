package com.web_chat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Store active sessions: sessionId -> WebSocketSession
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // Store room memberships: roomId -> Set of sessionIds
    private final Map<String, Set<String>> roomMembers = new ConcurrentHashMap<>();

    // Store user info: sessionId -> UserInfo
    private final Map<String, UserInfo> userSessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler() {
        System.out.println("🚀 ChatWebSocketHandler created!");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        System.out.println("✅ WebSocket connection established: " + sessionId);
        sessions.put(sessionId, session);

        // Extract user info from query parameters
        URI uri = session.getUri();
        if (uri != null) {
            Map<String, String> queryParams = getQueryParams(uri.getQuery());
            String roomId = queryParams.get("roomId");
            String userId = queryParams.get("userId");
            String username = queryParams.get("username");

            if (roomId != null && userId != null && username != null) {
                UserInfo userInfo = new UserInfo(userId, username, roomId);
                userSessions.put(sessionId, userInfo);

                // Add user to room
                roomMembers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);

                // Send connection confirmation
                sendToSession(session, createMessage("connection_established", Map.of(
                        "message", "Connected to room " + roomId,
                        "userId", userId,
                        "username", username)));

                // Notify other users in room that this user joined
                broadcastToRoom(roomId, createMessage("user_joined", Map.of(
                        "username", username,
                        "userId", userId,
                        "message", username + " joined the chat")), sessionId);

                // Send message history to the new user
                sendMessageHistory(session, roomId);

                System.out.println("👤 User " + username + " (ID: " + userId + ") joined room " + roomId);
            } else {
                sendErrorMessage(session, "Missing required parameters: roomId, userId, or username");
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();
        System.out.println("📨 Received message: " + payload);

        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String messageType = jsonNode.get("type").asText();

            System.out.println("🔍 DEBUG: Message type: " + messageType);

            switch (messageType) {
                case "send_message":
                    System.out.println("🔍 DEBUG: Calling handleSendMessage");
                    handleSendMessage(session, jsonNode);
                    break;
                case "join_room":
                    System.out.println("🔍 DEBUG: Calling handleJoinRoom");
                    handleJoinRoom(session, jsonNode);
                    break;
                case "ping":
                    System.out.println("🔍 DEBUG: Calling handlePing");
                    handlePing(session);
                    break;
                default:
                    System.out.println("Unknown message type: " + messageType);
                    sendErrorMessage(session, "Unknown message type: " + messageType);
            }
        } catch (Exception e) {
            System.err.println("Error handling WebSocket message: " + e.getMessage());
            e.printStackTrace();
            sendErrorMessage(session, "Error processing message: " + e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err
                .println("❌ WebSocket transport error for session " + session.getId() + ": " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String sessionId = session.getId();
        System.out.println("🔌 WebSocket connection closed: " + sessionId + ", Status: " + closeStatus);

        UserInfo userInfo = userSessions.get(sessionId);
        if (userInfo != null) {
            // Remove user from room
            Set<String> roomSessions = roomMembers.get(userInfo.getRoomId());
            if (roomSessions != null) {
                roomSessions.remove(sessionId);
                if (roomSessions.isEmpty()) {
                    roomMembers.remove(userInfo.getRoomId());
                    System.out.println("🏠 Room " + userInfo.getRoomId() + " is now empty");
                }
            }

            // Notify other users in room that this user left
            broadcastToRoom(userInfo.getRoomId(), createMessage("user_left", Map.of(
                    "username", userInfo.getUsername(),
                    "userId", userInfo.getUserId(),
                    "message", userInfo.getUsername() + " left the chat")), sessionId);

            userSessions.remove(sessionId);
            System.out.println("👋 User " + userInfo.getUsername() + " left room " + userInfo.getRoomId());
        }

        sessions.remove(sessionId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void handleSendMessage(WebSocketSession session, JsonNode jsonNode) {
        try {
            System.out.println("🔍 DEBUG: Processing send_message for session: " + session.getId());

            UserInfo userInfo = userSessions.get(session.getId());
            System.out.println("🔍 DEBUG: UserInfo found: " + userInfo);
            System.out.println("🔍 DEBUG: Total userSessions: " + userSessions.size());

            if (userInfo == null) {
                System.out.println("❌ DEBUG: User not found in session! Sending error...");
                sendErrorMessage(session, "User not authenticated");
                return;
            }

            String roomId = jsonNode.get("roomId").asText();
            String content = jsonNode.get("content").asText().trim();

            if (content.isEmpty()) {
                sendErrorMessage(session, "Message content cannot be empty");
                return;
            }

            String senderId = userInfo.getUserId();
            String messageId = UUID.randomUUID().toString();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Create message object for broadcasting
            Map<String, Object> messageData = Map.of(
                    "messageId", messageId,
                    "senderId", senderId,
                    "senderUsername", userInfo.getUsername(),
                    "content", content,
                    "timestamp", timestamp,
                    "roomId", roomId);

            // Broadcast to all users in the room (including sender for confirmation)
            broadcastToRoom(roomId, createMessage("new_message", messageData), null);

            System.out.println("💬 Message sent in room " + roomId + " by " + userInfo.getUsername() + ": " + content);
            System.out.println("🔍 DEBUG: handleSendMessage completed successfully");

        } catch (Exception e) {
            System.err.println("Error handling send message: " + e.getMessage());
            e.printStackTrace();
            sendErrorMessage(session, "Error sending message: " + e.getMessage());
        }
    }

    private void handleJoinRoom(WebSocketSession session, JsonNode jsonNode) {
        try {
            System.out.println("🔍 DEBUG: Processing join_room for session: " + session.getId());

            String roomId = jsonNode.get("roomId").asText();
            String userId = jsonNode.get("userId").asText();
            String username = jsonNode.get("username").asText();

            System.out.println(
                    "🔍 DEBUG: Join room data - roomId: " + roomId + ", userId: " + userId + ", username: " + username);

            // Remove user from previous room if any
            UserInfo existingUserInfo = userSessions.get(session.getId());
            if (existingUserInfo != null) {
                System.out.println("🔍 DEBUG: Removing user from previous room: " + existingUserInfo.getRoomId());
                Set<String> oldRoomSessions = roomMembers.get(existingUserInfo.getRoomId());
                if (oldRoomSessions != null) {
                    oldRoomSessions.remove(session.getId());
                }
            }

            // Add to new room
            UserInfo userInfo = new UserInfo(userId, username, roomId);
            userSessions.put(session.getId(), userInfo);

            System.out.println("🔍 DEBUG: User stored in session. Total sessions now: " + userSessions.size());
            System.out.println(
                    "🔍 DEBUG: User info stored: " + userInfo.getUsername() + " in room " + userInfo.getRoomId());

            roomMembers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session.getId());

            System.out.println("🔍 DEBUG: Room " + roomId + " now has " + roomMembers.get(roomId).size() + " members");

            // Send confirmation to user
            sendToSession(session, createMessage("room_joined", Map.of(
                    "roomId", roomId,
                    "message", "Successfully joined room " + roomId)));

            // Send message history
            sendMessageHistory(session, roomId);

            // Notify other users
            broadcastToRoom(roomId, createMessage("user_joined", Map.of(
                    "username", username,
                    "userId", userId,
                    "message", username + " joined the chat")), session.getId());

            System.out.println("🏠 User " + username + " joined room " + roomId);

        } catch (Exception e) {
            System.err.println("Error handling join room: " + e.getMessage());
            sendErrorMessage(session, "Error joining room: " + e.getMessage());
        }
    }

    private void handlePing(WebSocketSession session) {
        try {
            sendToSession(session, createMessage("pong", Map.of("timestamp", System.currentTimeMillis())));
        } catch (Exception e) {
            System.err.println("Error handling ping: " + e.getMessage());
        }
    }

    private void sendMessageHistory(WebSocketSession session, String roomId) {
        try {
            // For now, send empty history - you can integrate with your database later
            List<Map<String, Object>> messageHistory = new ArrayList<>();

            Map<String, Object> historyMessage = Map.of(
                    "type", "message_history",
                    "roomId", roomId,
                    "messages", messageHistory);

            sendToSession(session, objectMapper.writeValueAsString(historyMessage));

        } catch (Exception e) {
            System.err.println("Error sending message history: " + e.getMessage());
        }
    }

    private void broadcastToRoom(String roomId, String message, String excludeSessionId) {
        System.out.println("🔍 DEBUG: Broadcasting to room " + roomId + ", excluding session: " + excludeSessionId);

        Set<String> roomSessions = roomMembers.get(roomId);
        if (roomSessions != null) {
            System.out.println("🔍 DEBUG: Room has " + roomSessions.size() + " total sessions");
            int sentCount = 0;
            for (String sessionId : roomSessions) {
                System.out.println("🔍 DEBUG: Checking session " + sessionId + " (exclude: " + excludeSessionId + ")");
                if (!sessionId.equals(excludeSessionId)) {
                    WebSocketSession session = sessions.get(sessionId);
                    if (session != null && session.isOpen()) {
                        try {
                            System.out.println("🔍 DEBUG: Sending message to session " + sessionId);
                            session.sendMessage(new TextMessage(message));
                            sentCount++;
                        } catch (IOException e) {
                            System.err.println("Error sending message to session " + sessionId + ": " + e.getMessage());
                            // Remove broken session
                            sessions.remove(sessionId);
                            roomSessions.remove(sessionId);
                            userSessions.remove(sessionId);
                        }
                    } else {
                        System.out.println("🔍 DEBUG: Session " + sessionId + " is null or closed");
                    }
                } else {
                    System.out.println("🔍 DEBUG: Skipping excluded session " + sessionId);
                }
            }
            System.out.println("📡 Broadcasted message to " + sentCount + " users in room " + roomId);
        } else {
            System.out.println("🔍 DEBUG: No sessions found for room " + roomId);
        }
    }

    private void sendToSession(WebSocketSession session, String message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                System.err.println("Error sending message to session: " + e.getMessage());
            }
        }
    }

    private String createMessage(String type, Map<String, Object> data) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", type);
            message.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            message.putAll(data);
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            System.err.println("Error creating message: " + e.getMessage());
            return "{\"type\":\"error\",\"message\":\"Error creating message\"}";
        }
    }

    private void sendErrorMessage(WebSocketSession session, String errorMessage) {
        try {
            Map<String, Object> error = Map.of(
                    "type", "error",
                    "message", errorMessage,
                    "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            sendToSession(session, objectMapper.writeValueAsString(error));
        } catch (Exception e) {
            System.err.println("Error sending error message: " + e.getMessage());
        }
    }

    private Map<String, String> getQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    try {
                        String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                        String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                        params.put(key, value);
                    } catch (Exception e) {
                        System.err.println("Error decoding query parameter: " + pair);
                    }
                }
            }
        }
        return params;
    }

    // Inner class to store user information
    private static class UserInfo {
        private final String userId;
        private final String username;
        private final String roomId;

        public UserInfo(String userId, String username, String roomId) {
            this.userId = userId;
            this.username = username;
            this.roomId = roomId;
        }

        public String getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getRoomId() {
            return roomId;
        }

        @Override
        public String toString() {
            return "UserInfo{userId='" + userId + "', username='" + username + "', roomId='" + roomId + "'}";
        }
    }
}