package thanhpro0703.ChatAnonymousApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import thanhpro0703.ChatAnonymousApp.model.UserSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ChatStatsService {
    private final RoomManager roomManager;
    
    // Số lượng thống kê
    private AtomicInteger totalConnections = new AtomicInteger(0);
    private AtomicInteger totalMessages = new AtomicInteger(0);
    private AtomicInteger totalPairings = new AtomicInteger(0);
    private AtomicInteger peakConcurrentUsers = new AtomicInteger(0);
    private AtomicInteger totalReconnections = new AtomicInteger(0);
    
    // Thời gian bắt đầu theo dõi
    private LocalDateTime startTime = LocalDateTime.now();
    
    // Lưu trữ thống kê theo thời gian (mỗi giờ)
    private ConcurrentHashMap<String, Integer> hourlyStats = new ConcurrentHashMap<>();
    
    @Autowired
    public ChatStatsService(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    /**
     * Cập nhật thống kê khi có kết nối mới
     */
    public void recordNewConnection() {
        totalConnections.incrementAndGet();
        updatePeakConcurrentUsers();
    }
    
    /**
     * Cập nhật thống kê khi có tin nhắn mới
     */
    public void recordNewMessage() {
        totalMessages.incrementAndGet();
    }
    
    /**
     * Cập nhật thống kê khi có ghép cặp mới
     */
    public void recordNewPairing() {
        totalPairings.incrementAndGet();
    }
    
    /**
     * Cập nhật thống kê khi có kết nối lại
     */
    public void recordReconnection() {
        totalReconnections.incrementAndGet();
    }
    
    /**
     * Kiểm tra và cập nhật số người dùng đồng thời cao nhất
     */
    private void updatePeakConcurrentUsers() {
        int currentUsers = roomManager.getWaitingCount() + roomManager.getPairedCount() * 2;
        int peak = peakConcurrentUsers.get();
        if (currentUsers > peak) {
            peakConcurrentUsers.set(currentUsers);
        }
    }
    
    /**
     * Lấy tất cả thống kê
     */
    public Map<String, Object> getAllStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Thống kê cơ bản
        stats.put("waitingUsers", roomManager.getWaitingCount());
        stats.put("activePairs", roomManager.getPairedCount());
        stats.put("totalConnections", totalConnections.get());
        stats.put("totalMessages", totalMessages.get());
        stats.put("totalPairings", totalPairings.get());
        stats.put("peakConcurrentUsers", peakConcurrentUsers.get());
        stats.put("totalReconnections", totalReconnections.get());
        stats.put("startTime", startTime.toString());
        stats.put("uptime", getUptimeInMinutes());
        
        return stats;
    }
    
    /**
     * Lấy số phút hoạt động từ khi bắt đầu
     */
    private long getUptimeInMinutes() {
        return java.time.Duration.between(startTime, LocalDateTime.now()).toMinutes();
    }
    
    /**
     * Xử lý thống kê theo lịch trình
     * Chạy mỗi giờ để lưu thống kê
     */
    @Scheduled(cron = "0 0 * * * *") // Chạy vào đầu mỗi giờ
    public void processHourlyStats() {
        String hourKey = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        int currentUsers = roomManager.getWaitingCount() + roomManager.getPairedCount() * 2;
        hourlyStats.put(hourKey + "-users", currentUsers);
        hourlyStats.put(hourKey + "-messages", totalMessages.get());
        log.info("Recorded hourly stats: {} users, {} messages", currentUsers, totalMessages.get());
    }
    
    /**
     * Lấy thống kê theo giờ
     */
    public Map<String, Map<String, Integer>> getHourlyStats(int hours) {
        Map<String, Map<String, Integer>> result = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < hours; i++) {
            LocalDateTime time = now.minusHours(i);
            String hourKey = time.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
            
            Map<String, Integer> hourData = new HashMap<>();
            hourData.put("users", hourlyStats.getOrDefault(hourKey + "-users", 0));
            hourData.put("messages", hourlyStats.getOrDefault(hourKey + "-messages", 0));
            
            result.put(hourKey, hourData);
        }
        
        return result;
    }
    
    /**
     * Reset thống kê
     */
    public void resetStats() {
        totalConnections.set(0);
        totalMessages.set(0);
        totalPairings.set(0);
        peakConcurrentUsers.set(0);
        totalReconnections.set(0);
        startTime = LocalDateTime.now();
        hourlyStats.clear();
        log.info("All statistics have been reset");
    }
} 