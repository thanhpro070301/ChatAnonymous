package thanhpro0703.ChatAnonymousApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thanhpro0703.ChatAnonymousApp.service.ChatStatsService;
import thanhpro0703.ChatAnonymousApp.service.RoomManager;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RoomManager roomManager;
    private final ChatStatsService chatStatsService;

    public ApiController(RoomManager roomManager, ChatStatsService chatStatsService) {
        this.roomManager = roomManager;
        this.chatStatsService = chatStatsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getBasicStats() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "online");
        response.put("waitingUsers", roomManager.getWaitingCount());
        response.put("activePairs", roomManager.getPairedCount());
        response.put("onlineCount", roomManager.getWaitingCount() + (roomManager.getPairedCount() * 2));
        response.put("chattingCount", roomManager.getPairedCount() * 2);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/stats/advanced")
    public ResponseEntity<Map<String, Object>> getAdvancedStats() {
        return ResponseEntity.ok(chatStatsService.getAllStats());
    }
    
    @GetMapping("/stats/hourly")
    public ResponseEntity<Map<String, Map<String, Integer>>> getHourlyStats(
            @RequestParam(defaultValue = "24") int hours) {
        return ResponseEntity.ok(chatStatsService.getHourlyStats(hours));
    }
    
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetSessions(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        // Kiểm tra xác thực admin
        if (adminKey == null || !adminKey.equals(System.getenv("CHAT_ADMIN_KEY"))) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Unauthorized access");
            return ResponseEntity.status(401).body(errorResponse);
        }
        
        // Thực hiện reset
        roomManager.resetAll();
        chatStatsService.resetStats();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "All sessions and statistics have been reset");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Chat Anonymous Service");
        response.put("version", "1.0.0");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
} 