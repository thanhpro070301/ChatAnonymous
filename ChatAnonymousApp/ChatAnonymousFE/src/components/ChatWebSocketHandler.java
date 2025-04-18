package com.example.chatanonymousfe.components;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private final Map<String, WebSocketSession> sessions = new HashMap<>();
    private final Map<String, String> partners = new ConcurrentHashMap<>();
    private final Queue<String> waitingUsers = new LinkedList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = extractSessionId(session);
        sessions.put(sessionId, session);
        logger.info("New session connected: " + sessionId);
        
        // Send welcome message
        ObjectNode message = JsonNodeFactory.instance.objectNode();
        message.put("type", "connected");
        message.put("sessionId", sessionId);
        session.sendMessage(new TextMessage(message.toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        String sessionId = extractSessionId(session);
        logger.info("Session disconnected: " + sessionId);
        
        // Clean up partner relationship if exists
        handleLeaveChat(session, sessionId);
        
        // Remove from sessions
        sessions.remove(sessionId);
        waitingUsers.remove(sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = extractSessionId(session);
        String payload = message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(payload);
        
        String type = jsonNode.get("type").asText();
        
        // Check message size (limit to 8MB)
        if (payload.getBytes().length > 8 * 1024 * 1024) {
            sendError(session, "Message too large");
            return;
        }

        logger.info("Received message of type: " + type + " from session: " + sessionId);
        
        // Handle based on message type
        switch (type) {
            case "find":
                handleFindPartner(session, sessionId);
                break;
                
            case "message":
                String content = jsonNode.has("content") ? jsonNode.get("content").asText() : "";
                boolean hasImageData = jsonNode.has("imageData");
                
                // Check if message contains image data
                if (hasImageData) {
                    // Check image size (limit to 5MB)
                    String imageData = jsonNode.get("imageData").asText();
                    if (imageData.getBytes().length > 5 * 1024 * 1024) {
                        sendError(session, "Image too large (max 5MB)");
                        return;
                    }
                    
                    // Process message with image
                    handleMessageWithImage(session, sessionId, content, imageData);
                } else {
                    // Regular text message
                    handleTextOnlyMessage(session, sessionId, content);
                }
                break;
                
            case "leave":
                handleLeaveChat(session, sessionId);
                break;

            case "cancel_search":
                handleCancelSearch(session, sessionId);
                break;
                
            case "image_destroyed":
                handleImageDestroyed(session, sessionId, jsonNode);
                break;
                
            default:
                logger.warn("Unknown message type: " + type);
                sendError(session, "Unknown message type");
        }
    }
    
    private void handleMessageWithImage(WebSocketSession session, String sessionId, String content, String imageData) throws IOException {
        String partnerId = getPartner(sessionId);
        if (partnerId == null) {
            sendError(session, "Not paired with anyone");
            return;
        }
        
        WebSocketSession partnerSession = sessions.get(partnerId);
        if (partnerSession != null && partnerSession.isOpen()) {
            // Create message with image data to send to partner
            ObjectNode messageForPartner = JsonNodeFactory.instance.objectNode();
            messageForPartner.put("type", "message");
            messageForPartner.put("content", content);
            messageForPartner.put("sender", "partner");
            messageForPartner.put("imageData", imageData);
            
            partnerSession.sendMessage(new TextMessage(messageForPartner.toString()));
        }
    }
    
    private void handleTextOnlyMessage(WebSocketSession session, String sessionId, String content) throws IOException {
        String partnerId = getPartner(sessionId);
        if (partnerId == null) {
            sendError(session, "Not paired with anyone");
            return;
        }
        
        WebSocketSession partnerSession = sessions.get(partnerId);
        if (partnerSession != null && partnerSession.isOpen()) {
            // Create regular text message for partner
            ObjectNode messageForPartner = JsonNodeFactory.instance.objectNode();
            messageForPartner.put("type", "message");
            messageForPartner.put("content", content);
            messageForPartner.put("sender", "partner");
            
            partnerSession.sendMessage(new TextMessage(messageForPartner.toString()));
        }
    }
    
    private void handleFindPartner(WebSocketSession session, String sessionId) throws IOException {
        // First check if already paired
        if (partners.containsKey(sessionId)) {
            sendError(session, "Already paired with a partner");
            return;
        }
        
        // Check if someone is waiting
        if (!waitingUsers.isEmpty()) {
            String partnerId = waitingUsers.poll();
            
            // Validate partner is still connected
            WebSocketSession partnerSession = sessions.get(partnerId);
            if (partnerSession != null && partnerSession.isOpen()) {
                // Create partnership
                partners.put(sessionId, partnerId);
                partners.put(partnerId, sessionId);
                
                // Notify both users
                notifyPaired(session, partnerSession);
            } else {
                // Partner disconnected while waiting, try again
                handleFindPartner(session, sessionId);
            }
        } else {
            // No one waiting, add to queue
            waitingUsers.add(sessionId);
            
            // Notify user they're waiting
            ObjectNode message = JsonNodeFactory.instance.objectNode();
            message.put("type", "waiting");
            session.sendMessage(new TextMessage(message.toString()));
        }
    }
    
    private void notifyPaired(WebSocketSession session1, WebSocketSession session2) throws IOException {
        ObjectNode message = JsonNodeFactory.instance.objectNode();
        message.put("type", "paired");
        
        session1.sendMessage(new TextMessage(message.toString()));
        session2.sendMessage(new TextMessage(message.toString()));
    }
    
    private void handleLeaveChat(WebSocketSession session, String sessionId) throws IOException {
        String partnerId = partners.remove(sessionId);
        
        if (partnerId != null) {
            // Remove the partner relationship
            partners.remove(partnerId);
            
            // Notify partner if still connected
            WebSocketSession partnerSession = sessions.get(partnerId);
            if (partnerSession != null && partnerSession.isOpen()) {
                ObjectNode message = JsonNodeFactory.instance.objectNode();
                message.put("type", "partnerLeft");
                partnerSession.sendMessage(new TextMessage(message.toString()));
            }
            
            // Notify user
            ObjectNode message = JsonNodeFactory.instance.objectNode();
            message.put("type", "leftChat");
            session.sendMessage(new TextMessage(message.toString()));
        }
    }
    
    private String extractSessionId(WebSocketSession session) {
        return session.getId();
    }
    
    private String getPartner(String sessionId) {
        return partners.get(sessionId);
    }
    
    private void sendError(WebSocketSession session, String errorMessage) throws IOException {
        ObjectNode message = JsonNodeFactory.instance.objectNode();
        message.put("type", "error");
        message.put("message", errorMessage);
        session.sendMessage(new TextMessage(message.toString()));
    }
    
    private void handleCancelSearch(WebSocketSession session, String sessionId) throws IOException {
        // Remove from waiting queue if present
        waitingUsers.remove(sessionId);
        
        // Notify user that search was canceled
        ObjectNode message = JsonNodeFactory.instance.objectNode();
        message.put("type", "cancel_search");
        message.put("content", "Tìm kiếm đã bị hủy.");
        session.sendMessage(new TextMessage(message.toString()));
    }
    
    private void handleImageDestroyed(WebSocketSession session, String sessionId, JsonNode jsonNode) throws IOException {
        // Check if user is paired
        String partnerId = getPartner(sessionId);
        if (partnerId == null) {
            return; // Silently ignore if not paired
        }
        
        // Get the message ID of the destroyed image
        String messageId = jsonNode.has("messageId") ? jsonNode.get("messageId").asText() : null;
        if (messageId == null) {
            return; // Silently ignore if no message ID
        }
        
        // Forward the destruction notification to the partner
        WebSocketSession partnerSession = sessions.get(partnerId);
        if (partnerSession != null && partnerSession.isOpen()) {
            ObjectNode notification = JsonNodeFactory.instance.objectNode();
            notification.put("type", "image_destroyed");
            notification.put("messageId", messageId);
            
            partnerSession.sendMessage(new TextMessage(notification.toString()));
        }
    }
} 