<template>
  <div class="message-wrapper" :class="{ 'message-self': isUser, 'message-other': !isUser }">
    <!-- System message design -->
    <div v-if="message.isSystemMessage" class="system-message-container">
      <div class="system-message">
        {{ message.content }}
      </div>
    </div>
    
    <!-- User/Stranger message design -->
    <div v-else class="message-container" 
      :class="{ 'self': isUser, 'other': !isUser }">
      
      <!-- Avatar cho người lạ -->
      <div v-if="!isUser" class="avatar">
        <img :src="strangerAvatar" alt="Avatar" />
      </div>
      
      <!-- Message content -->
      <div class="message">
        <!-- Hiển thị trạng thái đã đọc (nếu là tin nhắn của user) -->
        <div v-if="isUser && message.isRead" class="read-status">
          <ion-icon name="checkmark-done-outline"></ion-icon>
        </div>
        
        <!-- Display image if there is one -->
        <div v-if="message.imageData && !message.isDestroyed" class="image-container">
          <div 
            @click="openImage(message.imageData, message.selfDestruct, message.selfDestructTime, message.id)"
            class="image-preview"
            :class="{'self-destruct': message.selfDestruct}"
          >
            <img :src="message.imageData" alt="Shared image" />
            
            <!-- Self-destruct badge and countdown -->
            <div v-if="message.selfDestruct" class="self-destruct-badge">
              <ion-icon name="timer-outline"></ion-icon>
              <span v-if="message.viewStarted">{{ message.countdown }}s</span>
              <span v-else>{{ message.selfDestructTime }}s</span>
            </div>
          </div>
        </div>
        
        <!-- Display message if image was destroyed -->
        <div v-if="message.isDestroyed" class="destroyed-message">
          <ion-icon name="flame-outline"></ion-icon>
          <span>Hình ảnh đã tự hủy</span>
        </div>
        
        <!-- Display file if there is one -->
        <div v-if="message.fileData" class="file-container" @click="openFile(message.fileData, message.fileName, message.fileType)">
          <div class="file-icon">
            <ion-icon name="document"></ion-icon>
          </div>
          <div class="file-info">
            <div class="file-name">{{ message.fileName }}</div>
            <div class="file-size">{{ formatFileSize(message.fileSize) }}</div>
          </div>
          <div class="file-download">
            <ion-icon name="download-outline"></ion-icon>
          </div>
        </div>
        
        <!-- Message text -->
        <div class="message-text" v-if="message.content">
          <div v-html="formatMessageContent(message.content)"></div>
        </div>
        
        <!-- Message metadata -->
        <div class="message-metadata">
          <small class="timestamp">{{ formatTime(message.timestamp) }}</small>
          
          <!-- Nút thả reaction -->
          <button 
            v-if="!isUser && !message.isSystemMessage" 
            class="reaction-button"
            @click="showReactionPicker = !showReactionPicker"
          >
            <ion-icon name="happy-outline"></ion-icon>
          </button>
          
          <!-- Hiển thị reaction picker -->
          <div v-if="showReactionPicker" class="reaction-picker">
            <button 
              v-for="emoji in REACTION_EMOJIS" 
              :key="emoji"
              class="reaction-emoji"
              @click="addReaction(emoji)"
            >
              {{ emoji }}
            </button>
          </div>
        </div>
        
        <!-- Hiển thị reactions -->
        <div v-if="message.reactions && message.reactions.length > 0" class="reactions-container">
          <div 
            v-for="reaction in message.reactions" 
            :key="reaction.id"
            class="reaction-bubble"
          >
            {{ reaction.emoji }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MessageItem',
  props: {
    message: {
      type: Object,
      required: true
    },
    currentUser: {
      type: String,
      required: true
    },
    strangerAvatar: {
      type: String,
      default: '/default-avatar.png'
    }
  },
  data() {
    return {
      showReactionPicker: false,
      REACTION_EMOJIS: ['👍', '❤️', '😂', '😮', '😢', '👏']
    };
  },
  computed: {
    isUser() {
      return this.message.sender === 'user';
    }
  },
  methods: {
    formatTime(timestamp) {
      if (!timestamp) return '';
      
      const date = new Date(timestamp);
      let hours = date.getHours();
      let minutes = date.getMinutes();
      
      // Pad with leading zero
      hours = hours < 10 ? '0' + hours : hours;
      minutes = minutes < 10 ? '0' + minutes : minutes;
      
      return `${hours}:${minutes}`;
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
    
    formatMessageContent(content) {
      if (!content) return '';
      
      // Replace URLs with clickable links
      let formatted = content.replace(
        /(https?:\/\/[^\s]+)/g,
        '<a href="$1" target="_blank" rel="noopener noreferrer">$1</a>'
      );
      
      // Replace newlines with <br>
      formatted = formatted.replace(/\n/g, '<br>');
      
      return formatted;
    },
    
    openImage(imageUrl, selfDestruct, selfDestructTime, messageId) {
      this.$emit('open-image', imageUrl, selfDestruct, selfDestructTime, messageId);
    },
    
    openFile(fileData, fileName, fileType) {
      // Create a blob from the base64 data
      const byteCharacters = atob(fileData.split(',')[1]);
      const byteNumbers = new Array(byteCharacters.length);
      
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      
      const byteArray = new Uint8Array(byteNumbers);
      const blob = new Blob([byteArray], { type: fileType });
      
      // Create download link
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      
      a.href = url;
      a.download = fileName;
      a.click();
      
      // Clean up
      URL.revokeObjectURL(url);
    },
    
    addReaction(emoji) {
      this.showReactionPicker = false;
      this.$emit('add-reaction', {
        messageId: this.message.id,
        emoji: emoji
      });
    }
  }
};
</script>

<style scoped>
/* ... phần CSS hiện tại ... */

.image-container {
  max-width: 300px;
  margin-bottom: 8px;
  position: relative;
}

.image-preview {
  position: relative;
  cursor: pointer;
  overflow: hidden;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.image-preview img {
  max-width: 100%;
  max-height: 300px;
  display: block;
  border-radius: 12px;
  transition: filter 0.2s;
}

.image-preview:hover img {
  filter: brightness(0.9);
}

.image-preview.self-destruct img {
  border: 2px solid #ff4b4b;
}

.self-destruct-badge {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background-color: rgba(255, 75, 75, 0.8);
  color: white;
  border-radius: 12px;
  padding: 4px 8px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.destroyed-message {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: rgba(255, 75, 75, 0.1);
  color: #ff4b4b;
  padding: 8px 12px;
  border-radius: 12px;
  font-size: 14px;
  margin-bottom: 8px;
}

.destroyed-message ion-icon {
  font-size: 20px;
}
</style> 