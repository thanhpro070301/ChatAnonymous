<template>
  <div class="chat-input-container">
    <!-- Attachment options -->
    <div v-if="showAttachmentOptions" class="attachment-options">
      <div class="attachment-option" @click="triggerImageUpload">
        <ion-icon name="image-outline" class="attachment-icon"></ion-icon>
        <span>Hình ảnh</span>
      </div>
      <div class="attachment-option" @click="triggerFileUpload">
        <ion-icon name="document-outline" class="attachment-icon"></ion-icon>
        <span>File</span>
      </div>
    </div>
    
    <!-- Self-destruct options panel -->
    <div v-if="showSelfDestructOptions && selectedFile && selectedFile.type.startsWith('image/')" class="self-destruct-panel">
      <div class="self-destruct-header">
        <h3 class="text-sm sm:text-base">Cài đặt hình ảnh tự huỷ</h3>
        <button class="close-button" @click="showSelfDestructOptions = false">
          <ion-icon name="close-outline"></ion-icon>
        </button>
      </div>
      
      <div class="self-destruct-content">
        <div class="self-destruct-toggle">
          <label class="switch">
            <input type="checkbox" v-model="selfDestructEnabled">
            <span class="slider round"></span>
          </label>
          <span class="text-xs sm:text-sm">Tự huỷ sau khi xem</span>
        </div>
        
        <div v-if="selfDestructEnabled" class="self-destruct-time">
          <label class="text-xs sm:text-sm">Thời gian tự huỷ (giây):</label>
          <div class="time-selector">
            <button @click="decrementTime" :disabled="selfDestructTime <= 5" class="time-btn">
              <ion-icon name="remove-outline"></ion-icon>
            </button>
            <span class="time-value">{{ selfDestructTime }}s</span>
            <button @click="incrementTime" :disabled="selfDestructTime >= 60" class="time-btn">
              <ion-icon name="add-outline"></ion-icon>
            </button>
          </div>
        </div>
        
        <div class="image-preview">
          <img :src="previewUrl" alt="Preview" />
          <div v-if="selfDestructEnabled" class="self-destruct-badge">
            <ion-icon name="timer-outline"></ion-icon>
            <span class="text-xs">{{ selfDestructTime }}s</span>
          </div>
        </div>
        
        <div class="action-buttons">
          <button class="cancel-button text-xs sm:text-sm" @click="cancelImage">Huỷ</button>
          <button class="confirm-button text-xs sm:text-sm" @click="confirmImage">Xác nhận</button>
        </div>
      </div>
    </div>
    
    <!-- Chat input form -->
    <div class="input-area">
      <button 
        class="attachment-button" 
        @click="toggleAttachmentOptions"
        :class="{ 'active': showAttachmentOptions }"
      >
        <ion-icon name="attach-outline" class="icon-size"></ion-icon>
      </button>
      
      <div class="message-input">
        <textarea 
          ref="messageInput"
          v-model="message" 
          placeholder="Nhập tin nhắn..." 
          @keydown.enter.prevent="handleEnterPress"
          @input="adjustTextareaHeight"
          class="text-sm"
        ></textarea>
        
        <!-- File preview -->
        <div v-if="selectedFile && !showSelfDestructOptions" class="file-preview">
          <div class="file-preview-content">
            <!-- Image preview -->
            <div v-if="selectedFile.type.startsWith('image/')" class="image-preview-thumbnail">
              <img :src="previewUrl" alt="Image preview" />
              <div class="file-preview-overlay">
                <button class="self-destruct-toggle-button text-xs">
                  <ion-icon name="timer-outline" class="icon-size-sm"></ion-icon>
                  <span>Tự huỷ</span>
                </button>
              </div>
            </div>
            
            <!-- File preview -->
            <div v-else class="generic-file-preview">
              <ion-icon name="document-outline" class="icon-size"></ion-icon>
              <span class="text-xs sm:text-sm truncate max-w-[120px] sm:max-w-[200px]">{{ selectedFile.name }}</span>
            </div>
            
            <!-- Remove button -->
            <button class="remove-file" @click="removeFile">
              <ion-icon name="close-circle" class="icon-size"></ion-icon>
            </button>
          </div>
        </div>
      </div>
      
      <button 
        class="send-button" 
        @click="sendMessage" 
        :disabled="!canSendMessage"
      >
        <ion-icon name="send" class="icon-size"></ion-icon>
      </button>
    </div>
    
    <!-- Hidden file inputs -->
    <input 
      type="file"
      ref="imageInput"
      accept="image/*"
      style="display: none"
      @change="handleImageSelected"
    />
    
    <input 
      type="file"
      ref="fileInput"
      style="display: none"
      @change="handleFileSelected"
    />
  </div>
</template>

<script>
export default {
  name: 'ChatInput',
  props: {
    isPaired: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      message: '',
      showAttachmentOptions: false,
      selectedFile: null,
      previewUrl: null,
      showSelfDestructOptions: false,
      selfDestructEnabled: false,
      selfDestructTime: 10,
      maxImageSize: 5 * 1024 * 1024, // 5MB
      maxFileSize: 10 * 1024 * 1024, // 10MB
    };
  },
  computed: {
    canSendMessage() {
      return (this.message.trim() !== '' || this.selectedFile) && this.isPaired;
    }
  },
  methods: {
    toggleAttachmentOptions() {
      this.showAttachmentOptions = !this.showAttachmentOptions;
    },
    
    handleEnterPress(event) {
      // Check if shift key is held down - if yes, allow normal behavior (new line)
      if (!event.shiftKey) {
        this.sendMessage();
      }
    },
    
    adjustTextareaHeight() {
      const textarea = this.$refs.messageInput;
      textarea.style.height = 'auto';
      textarea.style.height = textarea.scrollHeight + 'px';
    },
    
    triggerImageUpload() {
      this.showAttachmentOptions = false;
      this.$refs.imageInput.click();
    },
    
    triggerFileUpload() {
      this.showAttachmentOptions = false;
      this.$refs.fileInput.click();
    },
    
    handleImageSelected(event) {
      const file = event.target.files[0];
      if (!file) return;
      
      // Check file size (max 5MB)
      if (file.size > this.maxImageSize) {
        alert(`Hình ảnh không được vượt quá ${this.formatFileSize(this.maxImageSize)}`);
        return;
      }
      
      this.selectedFile = file;
      this.createPreview(file);
    },
    
    handleFileSelected(event) {
      const file = event.target.files[0];
      if (!file) return;
      
      // Check file size (max 10MB)
      if (file.size > this.maxFileSize) {
        alert(`File không được vượt quá ${this.formatFileSize(this.maxFileSize)}`);
        return;
      }
      
      this.selectedFile = file;
      
      if (file.type.startsWith('image/')) {
        this.createPreview(file);
      } else {
        // For non-image files, no preview needed
        this.previewUrl = null;
      }
    },
    
    createPreview(file) {
      if (file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    
    removeFile() {
      this.selectedFile = null;
      this.previewUrl = null;
      this.selfDestructEnabled = false;
      this.selfDestructTime = 10;
      this.showSelfDestructOptions = false;
      
      // Reset file inputs
      this.$refs.imageInput.value = '';
      this.$refs.fileInput.value = '';
    },
    
    incrementTime() {
      if (this.selfDestructTime < 60) {
        this.selfDestructTime += 5;
      }
    },
    
    decrementTime() {
      if (this.selfDestructTime > 5) {
        this.selfDestructTime -= 5;
      }
    },
    
    cancelImage() {
      this.showSelfDestructOptions = false;
      this.removeFile();
    },
    
    confirmImage() {
      this.showSelfDestructOptions = false;
      // Keep the file selected but close the options panel
    },
    
    sendMessage() {
      if (!this.canSendMessage) return;
      
      if (this.selectedFile) {
        const reader = new FileReader();
        reader.onload = (e) => {
          const fileData = e.target.result;
          
          if (this.selectedFile.type.startsWith('image/')) {
            console.log('Image input - sending image with selfDestruct:', this.selfDestructEnabled, 
                      'selfDestructTime:', this.selfDestructTime);
            
            // Ensure these are the correct types for WebSocket JSON
            const selfDestruct = this.selfDestructEnabled === true;
            const selfDestructTime = selfDestruct ? Number(this.selfDestructTime) : null;
            
            // Emit event for image message
            this.$emit('send-message', {
              content: this.message.trim(),
              imageData: fileData,
              selfDestruct: selfDestruct,
              selfDestructTime: selfDestructTime
            });
          } else {
            // Emit event for file message
            this.$emit('send-message', {
              content: this.message.trim(),
              fileData: fileData,
              fileName: this.selectedFile.name,
              fileType: this.selectedFile.type,
              fileSize: this.selectedFile.size
            });
          }
          
          // Reset after sending
          this.message = '';
          this.removeFile();
          this.adjustTextareaHeight();
        };
        reader.readAsDataURL(this.selectedFile);
      } else {
        // Emit event for text-only message
        this.$emit('send-message', {
          content: this.message.trim()
        });
        
        // Reset after sending
        this.message = '';
        this.adjustTextareaHeight();
      }
    },
    
    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes';
      
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }
  },
  mounted() {
    this.adjustTextareaHeight();
  }
};
</script>

<style scoped>
.chat-input-container {
  border-top: 1px solid rgba(229, 231, 235, 0.5);
  position: relative;
  background-color: #f9fafb;
  padding: 0.5rem;
}

.dark .chat-input-container {
  background-color: #111827;
  border-top-color: rgba(55, 65, 81, 0.5);
}

/* Chat input styles */
.input-area {
  display: flex;
  align-items: flex-end;
  gap: 0.5rem;
  position: relative;
}

.message-input {
  flex: 1;
  position: relative;
  background-color: white;
  border-radius: 1rem;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
}

.dark .message-input {
  background-color: #1f2937;
  border-color: #374151;
}

.message-input textarea {
  width: 100%;
  resize: none;
  border: none;
  outline: none;
  padding: 0.75rem 1rem;
  max-height: 150px;
  min-height: 42px;
  font-size: 0.875rem;
  background-color: transparent;
  color: #1f2937;
}

.dark .message-input textarea {
  color: #e5e7eb;
}

.message-input textarea::placeholder {
  color: #9ca3af;
}

.attachment-button,
.send-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 9999px;
  background-color: #f3f4f6;
  border: 1px solid #e5e7eb;
  color: #6b7280;
  transition: all 0.2s;
  flex-shrink: 0;
}

.dark .attachment-button,
.dark .send-button {
  background-color: #374151;
  border-color: #4b5563;
  color: #9ca3af;
}

.send-button {
  background-color: #6366f1;
  border-color: #4f46e5;
  color: white;
}

.dark .send-button {
  background-color: #4f46e5;
  border-color: #4338ca;
}

.send-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.send-button:not(:disabled):hover {
  background-color: #4f46e5;
  transform: translateY(-1px);
}

.attachment-button:hover {
  background-color: #e5e7eb;
}

.dark .attachment-button:hover {
  background-color: #4b5563;
}

.attachment-button.active {
  background-color: #e5e7eb;
  color: #4f46e5;
}

.dark .attachment-button.active {
  background-color: #4b5563;
  color: #818cf8;
}

/* Attachment options */
.attachment-options {
  position: absolute;
  bottom: 100%;
  left: 0;
  margin-bottom: 0.5rem;
  background-color: white;
  border-radius: 0.75rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  padding: 0.5rem;
  display: flex;
  gap: 0.5rem;
  z-index: 10;
  border: 1px solid #e5e7eb;
}

.dark .attachment-options {
  background-color: #1f2937;
  border-color: #374151;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.2), 0 2px 4px -1px rgba(0, 0, 0, 0.1);
}

.attachment-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 4.5rem;
  height: 4.5rem;
  border-radius: 0.5rem;
  background-color: #f3f4f6;
  padding: 0.75rem;
  cursor: pointer;
  transition: all 0.2s;
}

.dark .attachment-option {
  background-color: #374151;
}

.attachment-option:hover {
  background-color: #e5e7eb;
  transform: translateY(-2px);
}

.dark .attachment-option:hover {
  background-color: #4b5563;
}

.attachment-option span {
  margin-top: 0.5rem;
  font-size: 0.75rem;
  color: #6b7280;
}

.dark .attachment-option span {
  color: #9ca3af;
}

.attachment-icon {
  font-size: 1.5rem;
  color: #6b7280;
}

.dark .attachment-icon {
  color: #9ca3af;
}

/* File preview styles */
.file-preview {
  padding: 0.5rem;
  border-top: 1px solid #e5e7eb;
}

.dark .file-preview {
  border-top-color: #374151;
}

.file-preview-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.image-preview-thumbnail {
  position: relative;
  width: 3rem;
  height: 3rem;
  border-radius: 0.375rem;
  overflow: hidden;
}

.image-preview-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.generic-file-preview {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem;
  background-color: #f3f4f6;
  border-radius: 0.375rem;
}

.dark .generic-file-preview {
  background-color: #374151;
}

.remove-file {
  margin-left: auto;
  color: #ef4444;
  background: none;
  border: none;
  cursor: pointer;
}

/* Responsive icon sizing */
.icon-size {
  font-size: 1.25rem;
}

.icon-size-sm {
  font-size: 1rem;
}

/* Media queries for better responsive design */
@media (max-width: 640px) {
  .chat-input-container {
    padding: 0.375rem;
  }
  
  .input-area {
    gap: 0.375rem;
  }
  
  .attachment-button,
  .send-button {
    width: 2.25rem;
    height: 2.25rem;
  }
  
  .message-input textarea {
    padding: 0.625rem 0.75rem;
    min-height: 38px;
  }
  
  .attachment-option {
    width: 4rem;
    height: 4rem;
  }
}

/* Handle landscape orientation on mobile devices */
@media (max-height: 500px) and (orientation: landscape) {
  .chat-input-container {
    padding: 0.25rem;
  }
  
  .attachment-button,
  .send-button {
    width: 2rem;
    height: 2rem;
  }
  
  .message-input textarea {
    padding: 0.5rem 0.75rem;
    min-height: 36px;
    max-height: 100px;
  }
  
  .attachment-options {
    left: auto;
    right: 0;
  }
  
  .attachment-option {
    width: 3.5rem;
    height: 3.5rem;
    padding: 0.5rem;
  }
}
</style> 