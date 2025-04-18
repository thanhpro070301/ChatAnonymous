package thanhpro0703.ChatAnonymousApp.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import thanhpro0703.ChatAnonymousApp.model.Message;
import thanhpro0703.ChatAnonymousApp.model.UserSession;
import thanhpro0703.ChatAnonymousApp.service.ChatStatsService;
import thanhpro0703.ChatAnonymousApp.service.RoomManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final RoomManager roomManager;
    private final ChatStatsService chatStatsService;
    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(RoomManager roomManager, ChatStatsService chatStatsService) {
        this.roomManager = roomManager;
        this.chatStatsService = chatStatsService;
        // Cấu hình objectMapper để bỏ qua các trường không xác định
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        log.info("New connection established: {}", sessionId);
        
        try {
            UserSession user = new UserSession(sessionId, session);
            
            // Kiểm tra xem có phải là phiên đang kết nối lại không
            String partnerId = roomManager.getPartnerId(sessionId);
            
            if (partnerId != null && sessions.containsKey(partnerId)) {
                // Phiên đang kết nối lại, cập nhật WebSocketSession mới
                log.info("Session reconnecting: {} (paired with {})", sessionId, partnerId);
                sessions.put(sessionId, user);
                
                // Ghi nhận kết nối lại trong thống kê
                chatStatsService.recordReconnection();
                
                // Thông báo kết nối lại thành công
                sendMessage(session, new Message("connected", "Đã kết nối với người lạ", "system", null));
                
                // Thông báo cho đối tác
                WebSocketSession partnerSession = sessions.get(partnerId).getSocket();
                if (partnerSession != null && partnerSession.isOpen()) {
                    sendMessage(partnerSession, new Message("status", "Người lạ đã kết nối lại", "system", null));
                }
            } else {
                // Đây là một phiên mới
                sessions.put(sessionId, user);
                
                // Ghi nhận kết nối mới trong thống kê
                chatStatsService.recordNewConnection();
                
                // Add user to waiting queue and try to pair
                roomManager.addToQueue(user);
            }
        } catch (Exception e) {
            log.error("Error in connection establishment: {}", e.getMessage(), e);
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (IOException closeEx) {
                log.error("Error closing session: {}", closeEx.getMessage());
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String sessionId = session.getId();
        
        // Cập nhật thời gian hoạt động cuối cùng
        if (sessions.containsKey(sessionId)) {
            sessions.get(sessionId).updateActivity();
        }
        
        try {
            // Kiểm tra kích thước message
            String payload = textMessage.getPayload();
            int payloadSize = payload.length();
            log.info("Message received from session {}, size: {} bytes", sessionId, payloadSize);
            
            if (payloadSize > 750000) { // ~750KB limit
                log.warn("Message too large from session {}, size: {} bytes", sessionId, payloadSize);
                sendMessage(session, new Message("status", "Không thể xử lý tin nhắn vì kích thước quá lớn (tối đa 750KB)", "system", null));
                return;
            }
            
            // Kiểm tra JSON hợp lệ
            Message message;
            try {
                message = objectMapper.readValue(payload, Message.class);
            } catch (Exception e) {
                log.error("Invalid JSON format from session {}: {}", sessionId, e.getMessage());
                sendMessage(session, new Message("status", "Tin nhắn không đúng định dạng", "system", null));
                return;
            }
            
            // Kiểm tra xem tin nhắn có chứa dữ liệu hình ảnh không
            boolean hasImage = message.getImageData() != null && !message.getImageData().isEmpty();
            
            if (hasImage) {
                int imageSize = message.getImageData().length();
                // Kiểm tra kích thước dữ liệu hình ảnh
                if (imageSize > 500000) { // Giảm giới hạn xuống ~500KB
                    log.warn("Image data too large from session {}: {} bytes", sessionId, imageSize);
                    sendMessage(session, new Message("status", "Hình ảnh quá lớn, không thể gửi (tối đa 500KB)", "system", null));
                    return;
                }
                
                // Ghi log thêm thông tin để debug
                log.info("Message contains image data, size: {} bytes, type: {}, selfDestruct: {}, selfDestructTime: {}", 
                        imageSize, message.getType(), message.getSelfDestruct(), message.getSelfDestructTime());
            }
            
            String partnerId = roomManager.getPartnerId(sessionId);
            
            log.debug("Processing message type: {}, from session: {}, to partner: {}, has image: {}", 
                   message.getType(), sessionId, partnerId, hasImage);
            
            try {
                switch (message.getType()) {
                    case "message":
                    case "image":
                        // Ghi nhận thống kê tin nhắn
                        chatStatsService.recordNewMessage();
                        
                        if (partnerId != null && sessions.containsKey(partnerId)) {
                            WebSocketSession partnerSession = sessions.get(partnerId).getSocket();
                            
                            if (partnerSession != null && partnerSession.isOpen()) {
                                try {
                                    // Đảm bảo giữ nguyên thông tin về hình ảnh khi gửi đến người nhận
                                    Message outMessage = new Message(
                                        hasImage ? "message" : message.getType(), 
                                        message.getContent(), 
                                        "stranger", 
                                        message.getImageData()
                                    );
                                    
                                    // Thêm các trường mới nếu có
                                    if (message.getSenderId() != null) {
                                        outMessage.setSenderId(message.getSenderId());
                                    }
                                    if (message.getReceiver() != null) {
                                        outMessage.setReceiver(message.getReceiver());
                                    }
                                    
                                    // Thêm thông tin tự hủy nếu có
                                    if (message.getSelfDestruct() != null) {
                                        outMessage.setSelfDestruct(message.getSelfDestruct());
                                        log.info("Setting selfDestruct: {} for outgoing message", message.getSelfDestruct());
                                    }
                                    if (message.getSelfDestructTime() != null) {
                                        outMessage.setSelfDestructTime(message.getSelfDestructTime());
                                        log.info("Setting selfDestructTime: {} for outgoing message", message.getSelfDestructTime());
                                    }
                                    
                                    // Log the full outgoing message for debugging
                                    log.info("Outgoing message to partner {}: type={}, hasImage={}, selfDestruct={}, selfDestructTime={}", 
                                            partnerId, outMessage.getType(), (outMessage.getImageData() != null), 
                                            outMessage.getSelfDestruct(), outMessage.getSelfDestructTime());
                                    
                                    // Gửi tin nhắn và kiểm tra kết quả
                                    boolean sent = sendMessage(partnerSession, outMessage);
                                    if (!sent) {
                                        log.warn("Failed to send message to partner {}", partnerId);
                                        sendMessage(session, new Message("status", "Đối tượng chat không thể nhận tin nhắn", "system", null));
                                    }
                                } catch (Exception e) {
                                    log.error("Error sending message to partner: {}", e.getMessage(), e);
                                    sendMessage(session, new Message("status", "Không thể gửi tin nhắn tới người nhận: " + e.getMessage(), "system", null));
                                }
                            } else {
                                // Đối tác không trực tuyến hoặc socket đã đóng
                                log.warn("Partner session {} is not available or closed", partnerId);
                                sendMessage(session, new Message("status", "Đối tượng chat có thể đã offline, tin nhắn có thể không được nhận", "system", null));
                            }
                        } else {
                            // Không có đối tác, thêm vào hàng đợi
                            if (sessions.containsKey(sessionId)) {
                                roomManager.addToQueue(sessions.get(sessionId));
                            }
                        }
                        break;
                    case "leave":
                        log.info("Session {} requested to leave chat", sessionId);
                        disconnect(sessionId, true);
                        break;
                    case "find":
                        log.info("Session {} requested to find new partner", sessionId);
                        // User wants to find a new partner
                        if (partnerId != null) {
                            // Notify current partner that stranger left
                            notifyPartnerLeft(partnerId);
                            // Remove current pairing
                            roomManager.removePair(sessionId);
                        }
                        
                        // Add back to queue
                        if (sessions.containsKey(sessionId)) {
                            roomManager.addToQueue(sessions.get(sessionId));
                        }
                        break;
                    case "connection_status":
                        log.debug("Session {} requested connection status", sessionId);
                        // Yêu cầu kiểm tra trạng thái kết nối
                        if (partnerId != null && sessions.containsKey(partnerId)) {
                            WebSocketSession partnerSession = sessions.get(partnerId).getSocket();
                            if (partnerSession != null && partnerSession.isOpen()) {
                                // Đối tác đang trực tuyến, gửi lại thông báo connected
                                sendMessage(session, new Message("connected", "Đã kết nối với người lạ", "system", null));
                            } else {
                                // Đối tác không trực tuyến
                                sendMessage(session, new Message("status", "Đối tượng chat đang offline", "system", null));
                            }
                        } else {
                            // Không có đối tác, thêm vào hàng đợi
                            if (sessions.containsKey(sessionId)) {
                                roomManager.addToQueue(sessions.get(sessionId));
                            }
                        }
                        break;
                    case "read_receipt":
                    case "typing":
                    case "reaction":
                        // Xử lý các loại tin nhắn mới
                        if (partnerId != null && sessions.containsKey(partnerId)) {
                            WebSocketSession partnerSession = sessions.get(partnerId).getSocket();
                            if (partnerSession != null && partnerSession.isOpen()) {
                                sendMessage(partnerSession, message);
                            }
                        }
                        break;
                    default:
                        log.warn("Unknown message type from session {}: {}", sessionId, message.getType());
                        sendMessage(session, new Message("status", "Loại tin nhắn không được hỗ trợ: " + message.getType(), "system", null));
                }
            } catch (Exception e) {
                log.error("Error processing message type {}: {}", message.getType(), e.getMessage(), e);
                sendMessage(session, new Message("status", "Lỗi xử lý tin nhắn loại " + message.getType() + ": " + e.getMessage(), "system", null));
            }
        } catch (Exception e) {
            log.error("Error processing message from session {}: {}", sessionId, e.getMessage(), e);
            try {
                // Thông báo lỗi nhưng không làm mất kết nối
                sendMessage(session, new Message("status", "Đã xảy ra lỗi khi xử lý tin nhắn: " + e.getMessage(), "system", null));
            } catch (Exception ex) {
                log.error("Could not send error message to session {}: {}", sessionId, ex.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();
        log.info("Connection closed: {}, status: {}", sessionId, status);
        disconnect(sessionId, false);
    }

    private void disconnect(String sessionId, boolean reconnect) {
        if (sessionId == null) {
            return;
        }
        
        String partnerId = roomManager.getPartnerId(sessionId);
        
        if (partnerId != null && sessions.containsKey(partnerId)) {
            notifyPartnerLeft(partnerId);
        }
        
        // Remove from queue if waiting
        if (roomManager.isWaiting(sessionId) && sessions.containsKey(sessionId)) {
            roomManager.removeFromQueue(sessions.get(sessionId));
        }
        
        // Remove from paired users
        roomManager.removePair(sessionId);
        
        // Remove from active sessions if not reconnecting
        if (!reconnect) {
            sessions.remove(sessionId);
        }
    }
    
    private void notifyPartnerLeft(String partnerId) {
        if (sessions.containsKey(partnerId)) {
            WebSocketSession partnerSession = sessions.get(partnerId).getSocket();
            sendMessage(partnerSession, new Message("leave", "Người lạ đã rời đi", "system", null));
        }
    }
    
    private boolean sendMessage(WebSocketSession session, Message message) {
        try {
            if (session != null && session.isOpen()) {
                String messageJson = objectMapper.writeValueAsString(message);
                
                // Kiểm tra kích thước tin nhắn trước khi gửi
                if (messageJson.length() > 750000) {
                    log.warn("Message too large to send, size: {} bytes", messageJson.length());
                    
                    // Nếu có hình ảnh, thử gửi tin nhắn không có hình ảnh
                    if (message.getImageData() != null && !message.getImageData().isEmpty()) {
                        log.info("Attempting to send message without image data");
                        message.setImageData(null);
                        message.setContent(message.getContent() + " [Hình ảnh quá lớn, không thể gửi]");
                        String reducedJson = objectMapper.writeValueAsString(message);
                        session.sendMessage(new TextMessage(reducedJson));
                        return true;
                    }
                    
                    return false;
                }
                
                session.sendMessage(new TextMessage(messageJson));
                return true;
            }
            return false;
        } catch (IOException e) {
            log.error("Error sending message: {}", e.getMessage(), e);
            return false;
        }
    }
} 