package thanhpro0703.ChatAnonymousApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String type; // "message", "leave", "join", "image", "connected", "status"
    private String content;
    private String sender;
    private String imageData; // Base64 encoded image data
    private String senderId; // ID của người gửi, hỗ trợ xác định khi reload
    private String receiver; // Người nhận tin nhắn
    private Boolean selfDestruct; // Whether the image should self-destruct
    private Integer selfDestructTime; // Self-destruct time in seconds
    
    // Constructor không có senderId và receiver để tương thích với mã cũ
    public Message(String type, String content, String sender, String imageData) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.imageData = imageData;
        this.senderId = null;
        this.receiver = null;
        this.selfDestruct = false;
        this.selfDestructTime = null;
    }
} 