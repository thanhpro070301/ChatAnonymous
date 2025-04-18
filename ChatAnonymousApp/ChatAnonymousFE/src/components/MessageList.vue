<template>
  <div class="message-list-container" ref="messagesContainer">
    <!-- Connection status -->
    <div v-if="!isPaired && !isSearching" class="connection-status">
      <div class="status-icon disconnected">
        <i class="fas fa-plug"></i>
      </div>
      <div class="status-text">
        <p>Bạn chưa kết nối với ai</p>
        <button @click="findNewPartner" class="find-button">
          <i class="fas fa-search"></i> Tìm người trò chuyện
        </button>
      </div>
    </div>
    
    <div v-if="isSearching" class="connection-status">
      <div class="status-icon searching">
        <i class="fas fa-spinner fa-spin"></i>
      </div>
      <div class="status-text">
        <p>Đang tìm người trò chuyện...</p>
      </div>
    </div>
    
    <!-- Messages -->
    <div class="messages">
      <div v-for="(message, index) in messages" :key="index" :class="['message', message.sender]">
        <div class="message-content">
          <!-- Nội dung văn bản -->
          <p v-if="message.content">{{ message.content }}</p>
          
          <!-- Hình ảnh -->
          <div v-if="message.imageData" class="image-container">
            <!-- Hình ảnh tự huỷ -->
            <div v-if="message.selfDestruct && !message.isExpired" class="self-destruct-container">
              <img :src="message.imageData" alt="Hình ảnh" @load="handleImageLoaded(message)" />
              
              <div class="self-destruct-overlay">
                <div class="timer-container">
                  <div class="timer-countdown">{{ getRemainingTime(message) }}</div>
                  <div class="timer-progress">
                    <div class="timer-bar" :style="{ width: calculateProgress(message) + '%' }"></div>
                  </div>
                </div>
                <div class="self-destruct-label">
                  <i class="fas fa-fire-alt"></i> Hình ảnh tự huỷ
                </div>
              </div>
            </div>
            
            <!-- Hình ảnh thường -->
            <img v-else :src="message.imageData" alt="Hình ảnh" @click="openImageViewer(message.imageData)" />
          </div>
          
          <!-- File đính kèm -->
          <div v-if="message.fileData" class="file-attachment">
            <div class="file-info">
              <i class="fas fa-file"></i>
              <span class="file-name">{{ message.fileName }}</span>
              <span class="file-size">({{ formatFileSize(message.fileSize) }})</span>
            </div>
            <a :href="message.fileData" :download="message.fileName" class="download-button">
              <i class="fas fa-download"></i> Tải xuống
            </a>
          </div>
          
          <div class="message-meta">
            <span class="timestamp">{{ formatTimestamp(message.timestamp) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MessageList',
  props: {
    messages: {
      type: Array,
      default: () => []
    },
    isPaired: {
      type: Boolean,
      default: false
    },
    isSearching: {
      type: Boolean,
      default: false
    }
  },
  
  data() {
    return {
      timerIntervals: {}, // Lưu trữ các interval ID cho từng tin nhắn
    };
  },
  
  watch: {
    messages() {
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    }
  },
  
  methods: {
    scrollToBottom() {
      if (this.$refs.messagesContainer) {
        this.$refs.messagesContainer.scrollTop = this.$refs.messagesContainer.scrollHeight;
      }
    },
    
    formatTimestamp(timestamp) {
      if (!timestamp) return '';
      
      const date = new Date(timestamp);
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    },
    
    formatFileSize(bytes) {
      if (!bytes) return '0 B';
      
      const units = ['B', 'KB', 'MB', 'GB'];
      let size = bytes;
      let unitIndex = 0;
      
      while (size >= 1024 && unitIndex < units.length - 1) {
        size /= 1024;
        unitIndex++;
      }
      
      return `${size.toFixed(1)} ${units[unitIndex]}`;
    },
    
    findNewPartner() {
      this.$emit('find-partner');
    },
    
    openImageViewer(imageData) {
      // Mở trình xem hình ảnh
      this.$emit('view-image', imageData);
    },
    
    // Các phương thức xử lý hình ảnh tự huỷ
    startSelfDestructTimer(message) {
      if (!message.selfDestruct) return;
      
      // Nếu chưa có startTime, thiết lập ngay khi hình ảnh được tải
      if (!message.startTime) {
        message.startTime = new Date().getTime();
      }
      
      // Xóa timer cũ nếu có
      if (this.selfDestructTimers.has(message.id)) {
        clearInterval(this.selfDestructTimers.get(message.id));
      }
      
      // Tạo timer mới
      const timerId = setInterval(() => {
        // Tính thời gian còn lại
        const elapsed = (new Date().getTime() - message.startTime) / 1000;
        const timeLeft = message.selfDestructTime - elapsed;
        
        if (timeLeft <= 0) {
          // Hết thời gian, xóa hình ảnh
          this.handleSelfDestruct(message);
          clearInterval(timerId);
        } else {
          // Cập nhật UI
          this.$forceUpdate();
        }
      }, 100);
      
      // Lưu ID của timer
      this.selfDestructTimers.set(message.id, timerId);
    },
    
    getSelfDestructTimeRemaining(message) {
      if (!message.selfDestruct || !message.startTime) return '';
      
      const elapsed = (new Date().getTime() - message.startTime) / 1000;
      const timeLeft = Math.max(0, message.selfDestructTime - elapsed);
      
      return timeLeft.toFixed(1) + 's';
    },
    
    getSelfDestructProgress(message) {
      if (!message.selfDestruct || !message.startTime) return 0;
      
      const elapsed = (new Date().getTime() - message.startTime) / 1000;
      const progress = (elapsed / message.selfDestructTime) * 100;
      
      return Math.min(100, Math.max(0, progress));
    },
    
    handleSelfDestruct(message) {
      // Xóa timer
      if (this.selfDestructTimers.has(message.id)) {
        clearInterval(this.selfDestructTimers.get(message.id));
        this.selfDestructTimers.delete(message.id);
      }
      
      // Thay thế hình ảnh bằng thông báo
      message.imageData = null;
      message.content = message.content || 'Hình ảnh đã tự hủy';
      message.selfDestruct = false;
      
      // Thông báo đến component cha
      this.$emit('message-self-destructed', message.id);
    }
  },
  
  beforeDestroy() {
    // Xóa tất cả timer khi component bị hủy
    for (const timerId of this.selfDestructTimers.values()) {
      clearInterval(timerId);
    }
    this.selfDestructTimers.clear();
  }
};
</script>

<style scoped>
.message-list-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow-y: auto;
  padding: 16px;
}

.connection-status {
  display: flex;
  align-items: center;
  padding: 16px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  margin-bottom: 16px;
}

.status-icon {
  font-size: 24px;
  margin-right: 16px;
}

.status-icon.disconnected {
  color: #ff5252;
}

.status-icon.searching {
  color: #ffc107;
}

.find-button {
  margin-top: 8px;
  padding: 8px 16px;
  background: #4285f4;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}

.find-button:hover {
  background: #2a75f3;
}

.messages {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.message {
  max-width: 75%;
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
}

.message.user {
  align-self: flex-end;
}

.message.stranger {
  align-self: flex-start;
}

.message-content {
  background: #f1f1f1;
  border-radius: 16px;
  padding: 12px 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  word-break: break-word;
}

.message.user .message-content {
  background: #e3f2fd;
}

.message p {
  margin: 0;
  line-height: 1.4;
}

.message-meta {
  display: flex;
  justify-content: flex-end;
  margin-top: 4px;
  font-size: 0.75rem;
  color: #757575;
}

.image-container {
  position: relative;
  margin-top: 8px;
  max-width: 300px;
  border-radius: 8px;
  overflow: hidden;
}

.image-container img {
  width: 100%;
  display: block;
  cursor: pointer;
}

.self-destruct-container {
  position: relative;
}

.self-destruct-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 12px;
}

.timer-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.timer-countdown {
  color: white;
  font-size: 24px;
  font-weight: bold;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  margin-bottom: 8px;
}

.timer-progress {
  width: 100%;
  height: 4px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  overflow: hidden;
}

.timer-bar {
  height: 100%;
  background: #ff5252;
  transition: width 0.1s linear;
}

.self-destruct-label {
  color: white;
  font-size: 14px;
  text-align: center;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.file-attachment {
  margin-top: 8px;
  padding: 12px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
}

.file-info {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.file-name {
  margin-left: 8px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}

.file-size {
  margin-left: 4px;
  color: #757575;
}

.download-button {
  align-self: flex-end;
  padding: 6px 12px;
  background: #4caf50;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-size: 12px;
  display: flex;
  align-items: center;
}

.download-button i {
  margin-right: 4px;
}
</style> 