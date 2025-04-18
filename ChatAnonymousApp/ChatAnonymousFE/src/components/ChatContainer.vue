<template>
  <div class="chat-container h-full flex flex-col neuro-container overflow-hidden rounded-2xl" :class="{'dark-mode': darkMode}">
    <!-- Header -->
    <div class="bg-gradient-to-r from-white/90 to-secondary-50/90 dark:from-gray-950/90 dark:to-gray-900/90 backdrop-blur-glass px-2 sm:px-4 py-2 sm:py-3 flex flex-wrap md:flex-nowrap items-center justify-between gap-1 sm:gap-2 border-b border-gray-200/30 dark:border-gray-700/30">
      <div class="flex items-center">
        <div :class="{
          'w-2.5 h-2.5 sm:w-3 sm:h-3 rounded-full animate-pulse mr-1.5 sm:mr-2 flex-shrink-0': true,
          'bg-gradient-to-r from-secondary-500 to-primary-500': isConnected && isPaired,
          'bg-gradient-to-r from-secondary-500 to-primary-500': isConnecting || (isConnected && !isPaired),
          'bg-gradient-to-r from-red-500 to-pink-500': !isConnected
        }"></div>
        <h2 class="text-xs sm:text-sm md:text-base font-semibold truncate dark:text-gray-100 max-w-[100px] xs:max-w-[150px] sm:max-w-[200px] md:max-w-xs">
          <span v-if="isConnected && !isPaired && !isSearching" class="flex items-center">
            <span class="glass-btn dark:glass-btn-dark inline-flex items-center px-1.5 sm:px-2.5 py-1 sm:py-1.5 rounded-lg shadow-sm hover:shadow text-2xs sm:text-xs">
              <span class="h-1.5 w-1.5 sm:h-2 sm:w-2 rounded-full bg-violet-500 mr-1 sm:mr-2 animate-pulse"></span>
              <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 sm:h-3.5 sm:w-3.5 mr-0.5 sm:mr-1 text-secondary-600 dark:text-secondary-400" viewBox="0 0 20 20" fill="currentColor">
                <path d="M13 6a3 3 0 11-6 0 3 3 0 016 0zM18 8a2 2 0 11-4 0 2 2 0 014 0zM14 15a4 4 0 00-8 0v3h8v-3zM6 8a2 2 0 11-4 0 2 2 0 014 0zM16 18v-3a5.972 5.972 0 00-.75-2.906A3.005 3.005 0 0119 15v3h-3zM4.75 12.094A5.973 5.973 0 004 15v3H1v-3a3 3 0 013.75-2.906z" />
              </svg>
              <span class="font-medium text-secondary-600 dark:text-secondary-400">{{ onlineCount }}</span>
              <span class="ml-0.5 text-secondary-600 dark:text-secondary-400">online</span>
            </span>
          </span>
          <span v-else>
            {{ connectionStatus }}
          </span>
        </h2>
        <!-- Typing indicator -->
        <div v-if="isPaired && isTyping" class="typing-indicator flex items-center text-2xs sm:text-xs text-secondary-600 dark:text-secondary-400 ml-1 sm:ml-2 animate-fade-in whitespace-nowrap">
          <span class="mr-1 sm:mr-2">Đang nhập</span>
          <span class="dot animate-bounce"></span>
          <span class="dot animate-bounce" style="animation-delay: 0.2s"></span>
          <span class="dot animate-bounce" style="animation-delay: 0.4s"></span>
        </div>
      </div>
      <div class="flex items-center space-x-1 sm:space-x-2 md:space-x-3 ml-auto">
        <!-- Dark mode toggle -->
        <button @click="toggleDarkMode" class="glass-btn dark:glass-btn-dark p-1 sm:p-1.5 rounded-full flex-shrink-0">
          <svg v-if="!darkMode" xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5 sm:h-4 sm:w-4 text-gray-700" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5 sm:h-4 sm:w-4 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
          </svg>
        </button>
        <transition name="slide-up">
          <button v-if="isConnected && (isPaired || messages.length > 0)" @click="findNewPartner" 
            class="glass-btn dark:glass-btn-dark flex items-center text-secondary-600 dark:text-secondary-400 px-1.5 sm:px-2.5 py-1 sm:py-1.5 rounded-lg shadow-sm hover:shadow text-2xs sm:text-xs"
            :disabled="!isConnected || isConnecting"
            :class="{'opacity-50 cursor-not-allowed': !isConnected || isConnecting}">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 sm:h-3.5 sm:w-3.5 mr-0.5 sm:mr-1" viewBox="0 0 20 20" fill="currentColor">
              <path d="M8 9a3 3 0 100-6 3 3 0 000 6zM8 11a6 6 0 016 6H2a6 6 0 016-6zM16 7a1 1 0 10-2 0v1h-1a1 1 0 100 2h1v1a1 1 0 102 0v-1h1a1 1 0 100-2h-1V7z" />
            </svg>
            <span class="whitespace-nowrap">Tìm mới</span>
          </button>
        </transition>
        <transition name="slide-up">
          <button v-if="isConnected && isPaired" @click="endChat" 
            class="glass-btn dark:glass-btn-dark flex items-center text-red-600 dark:text-red-400 px-1.5 sm:px-2.5 py-1 sm:py-1.5 rounded-lg shadow-sm hover:shadow text-2xs sm:text-xs"
            :disabled="!isConnected"
            :class="{'opacity-50 cursor-not-allowed': !isConnected}">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 sm:h-3.5 sm:w-3.5 mr-0.5 sm:mr-1" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
            </svg>
            <span class="whitespace-nowrap">Kết thúc</span>
          </button>
        </transition>
      </div>
    </div>
    
    <!-- Main content -->
    <div class="flex-1 flex flex-col overflow-hidden bg-gradient-to-r from-white to-secondary-50/50 dark:from-gray-950 dark:to-gray-900">
      <!-- Loading spinner centered in the screen -->
      <div v-if="isConnecting" class="loading-overlay">
        <div class="loading-spinner">
          <div class="w-16 h-16 sm:w-20 sm:h-20 border-4 border-secondary-300/30 border-t-secondary-500 rounded-full animate-spin mb-4"></div>
          <div class="loading-text flex flex-col items-center">
            <span class="text-base sm:text-lg font-medium text-secondary-600 dark:text-secondary-400 mb-2">Đang kết nối...</span>
            <span class="text-xs sm:text-sm text-gray-500 dark:text-gray-400">Đang tìm máy chủ gần nhất</span>
          </div>
        </div>
      </div>
      
      <!-- Chat messages -->
      <div v-if="isPaired || (!isPaired && messages.length > 0 && !isSearching)" class="flex-1 overflow-y-auto p-2 sm:p-4 md:p-6 messages-container dark:bg-gray-800/90 relative" ref="chatMessages">
        <!-- Date separators -->
        <div v-for="(dateGroup, date) in groupedMessages" :key="date" class="message-group mb-3 sm:mb-4">
          <div class="date-separator flex items-center justify-center mb-2 sm:mb-3">
            <div class="h-px bg-gray-200 dark:bg-gray-700 flex-grow"></div>
            <div class="mx-2 sm:mx-4 text-2xs sm:text-xs text-gray-500 dark:text-gray-400 font-medium">{{ formatDate(date) }}</div>
            <div class="h-px bg-gray-200 dark:bg-gray-700 flex-grow"></div>
          </div>
          
          <transition-group name="fade" tag="div" class="space-y-1.5 sm:space-y-2">
            <div v-for="msg in dateGroup" :key="msg.id" class="mb-1.5 sm:mb-2">
              <!-- System message -->
              <div v-if="msg.sender === 'system'" class="flex justify-center mb-2 sm:mb-3 message-system">
                <div class="bg-gray-200/80 dark:bg-gray-700/80 backdrop-blur-xs px-3 sm:px-5 py-1.5 sm:py-2 text-2xs sm:text-sm rounded-full text-gray-700 dark:text-gray-300 shadow-sm border border-gray-300/30 dark:border-gray-600/30 system-message">
                  {{ msg.content }}
                </div>
              </div>
              
              <!-- User or stranger message - Only render if there's content or image data -->
              <div v-else-if="msg.content || msg.imageData || msg.fileData || msg.voiceData || msg.videoData" :class="{
                'flex': true,
                'justify-end message-user': msg.sender === 'user',
                'justify-start message-stranger': msg.sender !== 'user'
              }">
                <div :class="{
                    'max-w-[80%] sm:max-w-[75%] md:max-w-[70%] p-2 sm:p-3 break-words relative message-bubble': true,
                    'bg-secondary-500 text-white rounded-tl-xl rounded-tr-xl rounded-bl-xl dark:bg-secondary-600': msg.sender === 'user',
                    'bg-white dark:bg-gray-800 shadow-sm text-gray-800 dark:text-gray-200 rounded-tl-xl rounded-tr-xl rounded-br-xl': msg.sender !== 'user'
                  }">
                  <!-- Text message with markdown support -->
                  <div v-if="msg.content" class="whitespace-pre-line message-text text-xs sm:text-sm" v-html="formatMessageContent(msg.content)"></div>
                  
                  <!-- Image message -->
                  <div v-if="msg.imageData" class="mb-1.5 sm:mb-2">
                    <img 
                      :src="msg.imageData" 
                      class="max-w-full rounded-lg cursor-pointer hover:opacity-95 transition-opacity"
                      alt="Shared image"
                      @click="openImagePreview(msg.imageData, msg.selfDestruct, msg.selfDestructTime, msg.id)"
                    />
                    <p v-if="msg.content" class="mt-1.5 sm:mt-2 whitespace-pre-line text-xs sm:text-sm">{{ msg.content }}</p>
                    
                    <!-- Self-destruct indicator -->
                    <div v-if="msg.selfDestruct && !msg.viewStarted && msg.sender !== 'user'" 
                      class="flex items-center mt-1 text-2xs sm:text-xs text-red-500">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-2.5 w-2.5 sm:h-3 sm:w-3 mr-0.5 sm:mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                      </svg>
                      <span>Hình ảnh sẽ tự hủy sau {{ msg.selfDestructTime }} giây</span>
                    </div>
                    
                    <!-- Self-destruct countdown -->
                    <div v-if="msg.viewStarted && !msg.isDestroyed" 
                      class="flex items-center mt-1 text-2xs sm:text-xs text-red-500 animate-pulse">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-2.5 w-2.5 sm:h-3 sm:w-3 mr-0.5 sm:mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      <span>Tự hủy sau {{ msg.countdown || msg.selfDestructTime }} giây</span>
                    </div>
                    
                    <!-- Destroyed indicator -->
                    <div v-if="msg.isDestroyed" 
                      class="flex items-center mt-1 text-2xs sm:text-xs text-gray-500">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-2.5 w-2.5 sm:h-3 sm:w-3 mr-0.5 sm:mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                      </svg>
                      <span>Hình ảnh đã tự hủy</span>
                    </div>
                  </div>
                  
                  <!-- Video message -->
                  <div v-if="msg.videoData" class="mb-1.5 sm:mb-2">
                    <video 
                      :src="msg.videoData" 
                      class="max-w-full rounded-lg cursor-pointer hover:opacity-95 transition-opacity"
                      controls
                      style="max-height: 300px;"
                    ></video>
                    <p v-if="msg.content" class="mt-1.5 sm:mt-2 whitespace-pre-line text-xs sm:text-sm">{{ msg.content }}</p>
                  </div>
                  
                  <div class="message-meta flex items-center justify-end mt-0.5 sm:mt-1 space-x-1">
                    <!-- Message time -->
                    <span class="text-3xs sm:text-2xs opacity-70">{{ formatTime(msg.timestamp) }}</span>
                    
                    <!-- Read status (for user messages only) -->
                    <span v-if="msg.sender === 'user'" class="ml-1">
                      <svg v-if="msg.isRead" xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 text-blue-500" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                      </svg>
                      <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm-1.707-6.707a1 1 0 01-1.414-1.414l3-3a1 1 0 011.414 0l3 3a1 1 0 01-1.414 1.414L11 9.414V13a1 1 0 11-2 0V9.414l-1.293 1.293z" clip-rule="evenodd" />
                      </svg>
                    </span>
                  </div>
                </div>
                
                <!-- Message reactions -->
                <div v-if="msg.reactions && msg.reactions.length > 0" 
                  :class="{
                    'message-reactions bg-white dark:bg-gray-800 shadow-sm rounded-full px-2 py-1 flex -mt-3': true,
                    'ml-2': msg.sender !== 'user',
                    'mr-2': msg.sender === 'user'
                  }">
                  <div v-for="(reaction, i) in msg.reactions" :key="i" class="mx-0.5">
                    {{ reaction.emoji }}
                  </div>
                </div>
              </div>
              
              <!-- Reaction options (show on hover/tap) -->
              <div v-if="msg.sender !== 'user' && (msg.content || msg.imageData || msg.fileData || msg.voiceData || msg.videoData)" 
                class="reactions-bar flex justify-start mt-1 ml-4 opacity-0 group-hover:opacity-100 transition-opacity">
                <button 
                  v-for="emoji in quickReactions" 
                  :key="emoji" 
                  @click="addReaction(msg.id, emoji)"
                  class="text-sm p-1 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-full transition-colors"
                >
                  {{ emoji }}
                </button>
              </div>
            </div>
          </transition-group>
        </div>
        
        <div ref="messageEnd"></div>
        
        <!-- Scroll to bottom button -->
        <transition name="fade">
          <button 
            v-if="showScrollButton" 
            @click="scrollToBottom"
            class="fixed bottom-24 right-6 bg-secondary-500 text-white p-3 rounded-full shadow-lg hover:bg-secondary-600 transition-colors z-10"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3" />
            </svg>
          </button>
        </transition>
      </div>
      
      <!-- Not connected placeholder -->
      <div v-else-if="!isConnected && !isConnecting" class="flex-1 flex flex-col items-center justify-center p-8">
        <div class="w-64 h-64 mb-8 flex items-center justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-40 w-40 text-secondary-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.134 9 8z" />
          </svg>
        </div>
        <h3 class="text-xl font-semibold text-secondary-600 dark:text-secondary-400 mb-4">Chào mừng đến với Chat Anonymous!</h3>
        <p class="text-gray-600 dark:text-gray-400 text-center mb-6 max-w-md">Trò chuyện ẩn danh và an toàn với người lạ. Bấm nút bên dưới để bắt đầu.</p>
        <button @click="connectToChat" class="btn-gradient px-6 py-3 rounded-xl shadow-lg hover:shadow-xl transition-all">
          <span class="flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M18 10c0 3.866-3.582 7-8 7a8.841 8.841 0 01-4.083-.98L2 17l1.395-3.72C2.493 12.767 2 11.434 2 10c0-3.866 3.582-7 8-7s8 3.134 8 7zM7 9H5v2h2V9zm8 0h-2v2h2V9zM9 9h2v2H9V9z" clip-rule="evenodd" />
            </svg>
            Bắt đầu trò chuyện
          </span>
        </button>
      </div>
      
      <!-- Searching placeholder -->
      <div v-else-if="(isSearching || (isConnected && !isPaired)) && !isPaired" class="flex-1 flex flex-col items-center justify-center p-8">
        <div class="searching-animation mb-8">
          <div class="w-64 h-64 animate-pulse flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-40 w-40 text-secondary-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
        </div>
        <h3 class="text-xl font-semibold text-secondary-600 dark:text-secondary-400 mb-4">Đang tìm kiếm...</h3>
        <p class="text-gray-600 dark:text-gray-400 text-center mb-6 max-w-md">Chúng tôi đang tìm kiếm người trò chuyện phù hợp cho bạn. Vui lòng đợi trong giây lát.</p>
        <button @click="cancelSearch" class="border border-red-500 text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 px-6 py-2 rounded-xl transition-colors">
          Hủy tìm kiếm
        </button>
      </div>

      <!-- Connected but not paired placeholder - We don't need this anymore as we're showing the searching animation instead -->
      <div v-else-if="false" class="flex-1 flex flex-col items-center justify-center p-8">
      </div>
      
      <!-- Placeholder when connecting -->
      <div v-else-if="isConnecting" class="flex-1 flex flex-col items-center justify-center p-6 text-center">
        <div class="animate-bounce-light mb-4">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 text-gray-400 dark:text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
          </svg>
        </div>
        <h3 class="text-xl font-semibold text-gray-700 dark:text-gray-300 mb-2">Đang kết nối...</h3>
        <p class="text-gray-500 dark:text-gray-400 max-w-md">Vui lòng đợi trong khi chúng tôi thiết lập kết nối an toàn. Điều này có thể mất vài giây.</p>
      </div>
      
      <!-- Placeholder when waiting for pairing -->
      <div v-else-if="isConnected && ((isSearching && !isPaired) || (!isPaired && messages.length === 0 && !cancelAutoSearch))" class="w-full h-full bg-gradient-to-br from-blue-50 to-indigo-50 dark:from-gray-900 dark:to-blue-950 flex flex-col items-center justify-center px-4">
        <div class="relative flex flex-col items-center justify-center mb-8">
          <!-- Animated searching illustration -->
          <div class="w-20 h-20 border-4 border-blue-300 dark:border-blue-500 rounded-full flex items-center justify-center">
            <div class="w-16 h-16 border-4 border-indigo-400 dark:border-indigo-600 rounded-full animate-spin flex items-center justify-center" style="animation-duration: 3s">
              <div class="w-6 h-6 bg-blue-500 dark:bg-blue-400 rounded-full animate-ping-slow"></div>
            </div>
          </div>
          <div class="w-32 h-32 border border-blue-200 dark:border-blue-700 rounded-full absolute animate-ping-slow opacity-40"></div>
          <div class="w-40 h-40 border border-indigo-100 dark:border-indigo-900 rounded-full absolute animate-ping-slow opacity-20" style="animation-delay: 1.5s"></div>
        </div>

        <!-- Title with pulse animation -->
        <h2 class="text-2xl font-bold text-gray-800 dark:text-white mb-2 animate-pulse">Đang tìm kiếm...</h2>
        
        <!-- Info card with better styling -->
        <div class="glass-card dark:glass-card-dark rounded-xl p-6 max-w-md mt-4 text-center animate-fadeIn" style="animation-duration: 0.5s">
          <p class="text-gray-700 dark:text-gray-300 mb-4">
            Hệ thống đang tìm kiếm người trò chuyện phù hợp cho bạn. Vui lòng đợi trong giây lát.
          </p>
          <div class="flex items-center justify-center mt-2 text-sm">
            <div class="flex space-x-1 mr-2">
              <div class="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
              <div class="w-2 h-2 bg-green-500 rounded-full animate-pulse" style="animation-delay: 0.2s"></div>
              <div class="w-2 h-2 bg-green-500 rounded-full animate-pulse" style="animation-delay: 0.4s"></div>
            </div>
            <span class="text-green-600 dark:text-green-400 font-medium">Đang hoạt động</span>
          </div>
        </div>
      </div>
      
      <!-- Image preview modal -->
      <div v-if="previewImage" class="image-preview-modal fixed inset-0 flex items-center justify-center z-50 bg-black/80 p-4" @click="closeImagePreview">
        <div class="relative max-w-4xl max-h-[90vh] overflow-hidden rounded-2xl border-8 border-transparent bg-origin-border bg-clip-padding" style="background-image: linear-gradient(to right, var(--tw-gradient-stops)); --tw-gradient-from: rgb(99, 102, 241); --tw-gradient-to: rgb(139, 92, 246); --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to);">
          <img :src="previewImage" class="max-w-full max-h-[90vh] object-contain bg-white dark:bg-gray-900" alt="Full size image" />
          <button @click.stop="closeImagePreview" class="absolute top-2 right-2 bg-black/50 text-white rounded-full p-2 hover:bg-black/70 transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
          <!-- Self-destruct countdown -->
          <div v-if="isSelfDestruct && !isDestroyed" class="absolute bottom-4 left-0 right-0 flex justify-center">
            <div class="bg-gradient-to-r from-secondary-500 to-primary-500 text-white px-4 py-2 rounded-full flex items-center shadow-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-white animate-pulse" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span>Hình ảnh sẽ tự động hủy sau {{ countdown }} giây</span>
            </div>
          </div>
          <!-- Destroyed message -->
          <div v-if="isDestroyed" class="absolute inset-0 flex items-center justify-center bg-black/90">
            <div class="text-white text-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto mb-4 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
              <h3 class="text-2xl font-bold mb-2">Hình ảnh đã tự hủy</h3>
              <p>Hình ảnh này đã bị xóa và không thể xem lại.</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Self-destruct setup for outgoing images -->
      <div v-if="selectedImage" class="p-4 border-t border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800">
        <div class="flex items-center mb-2">
          <div class="flex-1">
            <h3 class="text-sm font-medium text-gray-700 dark:text-gray-300">Hình ảnh đã chọn</h3>
          </div>
          <button @click="cancelImage" class="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </button>
        </div>
        
        <div class="flex flex-col sm:flex-row space-y-3 sm:space-y-0 sm:space-x-3">
          <div class="flex-shrink-0 w-32 h-32 bg-gray-100 dark:bg-gray-700 rounded-lg overflow-hidden">
            <img :src="selectedImage" class="w-full h-full object-cover" alt="Selected image" />
          </div>
          
          <div class="flex-1">
            <div class="mb-2">
              <label class="flex items-center space-x-2 cursor-pointer">
                <input type="checkbox" v-model="isSelfDestruct" class="form-checkbox h-4 w-4 text-secondary-500 rounded focus:ring-secondary-500 border-gray-300 dark:border-gray-600 dark:bg-gray-700">
                <span class="text-sm text-gray-700 dark:text-gray-300">Tự động hủy sau khi xem</span>
              </label>
            </div>
            
            <div v-if="isSelfDestruct" class="mb-3">
              <label class="block text-sm text-gray-700 dark:text-gray-300 mb-1">Thời gian tự hủy (giây)</label>
              <input type="range" v-model="selfDestructTime" min="3" max="30" step="1" class="w-full h-2 bg-gray-200 dark:bg-gray-700 rounded-lg appearance-none cursor-pointer">
              <div class="flex justify-between text-xs text-gray-500 dark:text-gray-400 mt-1">
                <span>3s</span>
                <span>{{ selfDestructTime }}s</span>
                <span>30s</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Chat input area - fixed at bottom with no flex growing -->
      <div class="border-t border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 shadow-lg">
        <div v-if="isPaired" class="p-2 md:p-3">
          <div class="flex items-end space-x-2">
            <!-- Emoji picker -->
            <div class="relative">
              <button @click="toggleEmojiPicker" class="p-2 rounded-full text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </button>
              
              <!-- Emoji picker dropdown -->
              <div v-if="showEmojiPicker" class="absolute bottom-full mb-2 left-0 bg-white dark:bg-gray-800 rounded-lg shadow-lg border border-gray-200 dark:border-gray-700 p-2 w-64 h-48 overflow-y-auto z-10">
                <div class="flex border-b border-gray-200 dark:border-gray-700 pb-1 mb-1">
                  <button 
                    v-for="category in ['😊', '🙌', '🔥', '❤️']" 
                    :key="category"
                    @click="filterEmojis(category)"
                    class="p-1 text-lg rounded hover:bg-gray-100 dark:hover:bg-gray-700"
                  >
                    {{ category }}
                  </button>
                </div>
                <div class="grid grid-cols-7 gap-1">
                  <button 
                    v-for="emoji in filteredEmojis" 
                    :key="emoji"
                    @click="addEmoji(emoji)"
                    class="p-1 text-xl hover:bg-gray-100 dark:hover:bg-gray-700 rounded"
                  >
                    {{ emoji }}
                  </button>
                </div>
              </div>
            </div>
            
            <!-- Attachment button -->
            <div class="relative">
              <button @click="toggleAttachmentOptions" class="p-2 rounded-full text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13" />
                </svg>
              </button>
              
              <!-- Attachment options dropdown -->
              <div v-if="showAttachmentOptions" class="absolute bottom-full mb-3 left-0 attachment-menu glass-card dark:glass-card-dark rounded-xl p-2 z-10">
                <div class="flex space-x-2">
                  <button @click="triggerImageUpload" class="h-12 w-12 rounded-full flex items-center justify-center bg-gradient-to-br from-green-400 to-teal-400 transition-transform duration-200 hover:scale-110" title="Gửi hình ảnh">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-white" viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" clip-rule="evenodd" />
                    </svg>
                  </button>
                  
                  <button @click="triggerVideoUpload" class="h-12 w-12 rounded-full flex items-center justify-center bg-gradient-to-br from-blue-400 to-purple-400 transition-transform duration-200 hover:scale-110" title="Gửi video">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-white" viewBox="0 0 20 20" fill="currentColor">
                      <path d="M2 6a2 2 0 012-2h6a2 2 0 012 2v8a2 2 0 01-2 2H4a2 2 0 01-2-2V6z" />
                      <path d="M14.553 7.106A1 1 0 0014 8v4a1 1 0 00.553.894l2 1A1 1 0 0018 13V7a1 1 0 00-1.447-.894l-2 1z" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
            
            <!-- Text input -->
            <div class="flex-1 rounded-lg border border-gray-300 dark:border-gray-600 dark:bg-gray-700 transition-colors focus-within:border-secondary-500 dark:focus-within:border-secondary-400">
              <textarea 
                v-model="newMessage" 
                ref="messageInput"
                :placeholder="isPaired ? 'Nhập tin nhắn...' : 'Đang chờ kết nối với người lạ...'" 
                rows="1"
                @keydown.enter.prevent="sendMessage"
                @input="handleTypingEvent"
                class="block w-full px-3 py-2 bg-transparent text-gray-700 dark:text-gray-200 placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none resize-none"
              ></textarea>
            </div>
            
            <!-- Send button -->
            <button 
              @click="sendMessage" 
              :disabled="!isPaired || (!newMessage && !selectedImage && !selectedFile)"
              :class="{ 
                'p-2 rounded-full transition-all': true,
                'bg-secondary-500 text-white hover:bg-secondary-600': isPaired && (newMessage || selectedImage || selectedFile),
                'bg-gray-200 text-gray-400 dark:bg-gray-700 dark:text-gray-500 cursor-not-allowed': !isPaired || (!newMessage && !selectedImage && !selectedFile)
              }"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
              </svg>
            </button>
            
            <!-- File inputs (hidden) -->
            <input type="file" ref="imageInput" @change="onImageSelected" accept="image/*" class="hidden" />
            <input type="file" ref="fileInput" @change="onFileSelected" class="hidden" />
            <input
              type="file"
              ref="videoInput"
              accept="video/*"
              style="display: none"
              @change="onVideoSelected"
            />
          </div>
        </div>
        
        <div v-else-if="isConnected && !isPaired" class="p-4 text-center">
          <div v-if="isSearching" class="animate-pulse text-gray-600 dark:text-gray-400 mb-2">
            <span class="text-sm">Đang tìm kiếm người trò chuyện...</span>
          </div>
          <div v-else class="text-gray-600 dark:text-gray-400 mb-2">
            <button 
              @click="findNewPartner" 
              class="px-4 py-2 rounded-md bg-gradient-to-r from-secondary-500 to-primary-500 text-white hover:from-secondary-600 hover:to-primary-600 transition duration-300 ease-in-out focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
            >
              Tìm người mới
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatContainer',
  data() {
    return {
      socket: null,
      sessionId: null,
      isConnected: false,
      isConnecting: false,
      isSearching: false,
      isPaired: false,
      newMessage: '',
      messages: [],
      showEmojiPicker: false,
      selectedEmoji: null,
      copySuccess: false,
      selectedImage: null,
      selectedFile: null,
      selectedFileName: '',
      selectedFileType: '',
      selectedFileSize: null,
      typingTimeout: null,
      isTyping: false,
      showAttachmentOptions: false,
      uploadProgress: 0,
      showUploadProgress: false,
      isDragging: false,
      maxFileSize: 15 * 1024 * 1024, // 15MB in bytes
      maxReconnectAttempts: 5,
      reconnectAttempts: 0,
      windowFocused: true,
      unreadMessages: 0,
      lastTypingSent: 0,
      typingThrottleTime: 1000, // 1 second
      darkMode: localStorage.getItem('darkMode') === 'true',
      isSelfDestruct: false,
      selfDestructTime: '10',
      recorder: null,
      recording: false,
      recordingStartTime: null,
      recordingDuration: 0,
      recordingStream: null,
      audioChunks: [],
      recordingInterval: null,
      maxRecordingTime: 60, // 60 seconds
      emojiCategory: 'smileys',
      hasNotificationPermission: false,
      cancelAutoSearch: false, // Add this to manage auto search state
      
      // Stats
      onlineCount: 0,
      waitingUsers: 0,
      chattingCount: 0,
      
      // Voice recording
      isRecordingVoice: false,
      mediaRecorder: null,
      recordedChunks: [],
      recordingTime: 0,
      recordingInterval: null,
      
      // Notification
      notificationSound: new Audio('/notification.mp3'),
      isTyping: false,
      quickReactions: ['👍', '❤️', '😂', '😮', '😢', '🙏'],
      
      // Add new data properties for scroll management
      isNearBottom: true,
      scrollThreshold: 100,
      
      // Add new data property for showing scroll to bottom button
      showScrollButton: false,
      
      selectedVideo: null,
      isDestroyed: false,
      countdown: 0,
      countdownInterval: null,
      currentPreviewMessageId: null,
      previewImage: null,
      // Add emojis property
      emojis: ['😀', '😃', '😄', '😁', '😆', '😅', '😂', '🤣', '☺️', '😊', '😇', '🙂', '🙃', '😉', '😌', '😍', '🥰', '😘', '😗', '😙', '😚', '😋', '😛', '😝', '😜', '🤪', '🤨', '🧐', '🤓', '😎', '🤩', '🥳', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣', '😖', '😫', '😩', '🥺', '😢', '😭', '😮', '😱', '😨', '😰', '😯', '😦', '😧', '😲', '😵', '😳', '🤯', '👍', '👎', '👏', '🙌', '👐', '🤝', '👊', '✊', '🤛', '🤜', '✌️', '👌', '🤙', '👈', '👉', '👉', '👆', '👇', '☝️', '✋', '🤚', '🖐️', '🖖', '👋', '🤗', '🤔', '🤭', '🤫', '🤥', '🔥', '💯', '💥', '⭐', '🌟', '✨', '💫', '🎉', '🎊', '❤️', '🧡', '💛', '💚', '💙', '💜', '🖤', '🤍', '🤎'],
      filteredEmojis: []
    }
  },
  computed: {
    connectionStatus() {
      if (this.isConnected) {
        if (this.isPaired) {
          return 'Đang trò chuyện với người lạ'
        } else if (this.isSearching) {
          return 'Đang tìm kiếm'
        } else {
          return 'Kết nối thành công. Hãy bắt đầu tìm người trò chuyện!'
        }
      } else if (this.isConnecting) {
        return 'Đang kết nối...'
      } else {
        return 'Chưa kết nối'
      }
    },
    groupedMessages() {
      // Group messages by date for better organization
      const groups = {};
      
      this.messages.forEach(msg => {
        const date = new Date(msg.timestamp).toLocaleDateString();
        if (!groups[date]) {
          groups[date] = [];
        }
        groups[date].push(msg);
      });
      
      return groups;
    },
    showHeaderButtons() {
      // Chỉ hiển thị các nút trong header nếu đã kết nối và chưa có tin nhắn kết thúc
      if (!this.isConnected) return false;
      
      // Always show the "Find new" button when connected, even if chat has ended
      if (this.isConnected && !this.isPaired && this.messages.length > 0) {
        return true;
      }
      
      // For any other case, follow previous logic - show buttons when paired or when no end message exists
      // Kiểm tra xem người dùng đã kết thúc cuộc trò chuyện chưa
      const hasEndChatMessage = this.messages.some(msg => 
        msg.sender === 'system' && 
        (msg.content.includes('Người trò chuyện đã kết thúc') || 
         msg.content.includes('Bạn đã kết thúc') ||
         msg.content.includes('người lạ đã ngắt kết nối'))
      );
      
      // Trường hợp đã kết nối nhưng chưa ghép cặp, hoặc không có tin nhắn kết thúc
      return this.isPaired || !hasEndChatMessage;
    }
  },
  methods: {
    connectToChat() {
      // Keep the existing method but don't set isConnecting here
      // since we already set it in startChatAnimation
      if (!this.isConnecting) {
        this.isConnecting = true;
      }
      this.initWebSocket();
    },
    
    fetchStats() {
      // Determine the appropriate API URL based on environment
      let apiUrl;
      
      if (process.env.NODE_ENV === 'development') {
        apiUrl = 'http://localhost:8080/api/stats';
      } else {
        apiUrl = '/api/stats';
      }
      
      fetch(apiUrl)
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => {
          // Update the stats variables
          this.onlineCount = data.onlineCount || 0;
          this.waitingUsers = data.waitingUsers || 0;
          this.chattingCount = data.chattingCount || 0;
          
          console.log('Stats updated:', {
            online: this.onlineCount,
            waiting: this.waitingUsers,
            chatting: this.chattingCount
          });
        })
        .catch(error => {
          console.error('Error fetching stats:', error);
        });
    },
    
    initWebSocket() {
      // Close existing connection if any
      if (this.socket) {
        this.socket.close();
      }
      
      try {
        // Create new WebSocket connection
        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        // Xây dựng URL dựa trên host hiện tại hoặc kết nối trực tiếp với backend
        let wsUrl;
        
        // Trong môi trường development, kết nối trực tiếp với backend
        if (process.env.NODE_ENV === 'development') {
          wsUrl = 'ws://localhost:8080/chat';
        } else {
          // Trong môi trường production, sử dụng URL tương đối
          wsUrl = `${protocol}//${window.location.host}/ws/chat`;
        }
        
        console.log('Connecting to WebSocket at:', wsUrl);
        this.socket = new WebSocket(wsUrl);
        
        this.socket.onopen = () => {
          this.isConnected = true;
          this.isConnecting = false;
          this.reconnectAttempts = 0;
          console.log('WebSocket connection established');
          
          // Remove the loading center class when connection is established
          const buttonContainer = document.querySelector('.p-4.text-center');
          if (buttonContainer) {
            buttonContainer.classList.remove('loading-center');
          }
        }
        
        this.socket.onmessage = (event) => {
          try {
            const message = JSON.parse(event.data);
            console.log('Received message:', message);
            this.handleMessage(message);
          } catch (error) {
            console.error('Error parsing message:', error, event.data);
          }
        }
        
        this.socket.onclose = (event) => {
          this.isConnected = false;
          this.isPaired = false;
          console.log('WebSocket connection closed', event);
          
          // Attempt to reconnect if not intentionally closed
          if (!event.wasClean && this.reconnectAttempts < this.maxReconnectAttempts) {
            this.reconnectAttempts++;
            const delay = Math.min(1000 * Math.pow(2, this.reconnectAttempts), 30000);
            console.log(`Attempting reconnect in ${delay}ms (attempt ${this.reconnectAttempts})`);
            setTimeout(() => this.initWebSocket(), delay);
          }
        }
        
        this.socket.onerror = (error) => {
          console.error('WebSocket error:', error);
        }
      } catch (err) {
        console.error('Error creating WebSocket:', err);
        this.isConnecting = false;
        
        // Reset button position if connection fails
        const buttonContainer = document.querySelector('.p-4.text-center');
        if (buttonContainer) {
          buttonContainer.classList.remove('loading-center');
        }
        
        // Retry connection after delay
        if (this.reconnectAttempts < this.maxReconnectAttempts) {
          this.reconnectAttempts++;
          const delay = Math.min(1000 * Math.pow(2, this.reconnectAttempts), 30000);
          console.log(`Retrying connection in ${delay}ms (attempt ${this.reconnectAttempts})`);
          setTimeout(() => this.initWebSocket(), delay);
        }
      }
    },
    
    handleMessage(message) {
      console.log('Processing message:', message);
      
      // Debug thêm thông tin về hình ảnh nếu có
      if (message.imageData) {
        console.log('Received message with image, size:', message.imageData.length, 'bytes');
        console.log('Image message details:', {
          type: message.type,
          hasContent: !!message.content,
          sender: message.sender,
          selfDestruct: message.selfDestruct,
          selfDestructTime: message.selfDestructTime,
        });
        
        if (message.selfDestruct) {
          console.log('Image has self-destruct enabled, time:', message.selfDestructTime, 'seconds');
        }
      }
      
      switch (message.type) {
        case 'session':
          this.sessionId = message.sessionId;
          // Save session ID to localStorage for reconnections
          localStorage.setItem('chat_session_id', message.sessionId);
          console.log('Session established with ID:', message.sessionId);
          break;
          
        case 'connected':
          this.isPaired = true;
          this.isSearching = false; // No longer searching
          
          // Clear all messages first
          this.messages = [];
          
          // Display system message about connection
          this.messages.push({
            id: 'system-' + Date.now(),
            sender: 'system',
            content: 'Bạn đã được kết nối với người lạ. Hãy bắt đầu trò chuyện!',
            timestamp: Date.now(),
            isSystemMessage: true
          });
          
          // Always scroll to bottom for system messages
          this.$nextTick(() => {
            this.scrollToBottom();
            if (this.$refs.messageInput) {
              this.$refs.messageInput.focus();
            }
          });
          break;
          
        case 'status':
          // Check for error messages from the server
          if (message.content && message.content.includes('không được hỗ trợ')) {
            // Log the error but don't show it to the user
            console.log('Server error:', message.content);
            return; // Skip adding this message
          }
          
          // If status message contains "cancel_search", process it as a cancel_search message
          if (message.content && message.content.includes('cancel_search')) {
            // Handle as cancel_search
            this.isSearching = false;
            
            // Add a system message about canceling search if not already displayed
            this.messages.push({
              id: 'system-' + Date.now(),
              sender: 'system',
              content: 'Bạn đã hủy tìm kiếm. Bấm nút "Tìm người mới" khi bạn muốn tìm người trò chuyện.',
              timestamp: Date.now(),
              isSystemMessage: true
            });
          } 
          // If status message contains "image_destroyed", handle it appropriately
          else if (message.content && message.content.includes('image_destroyed')) {
            // Don't display the raw error message to the user
            console.log('Received image_destroyed status message:', message.content);
            
            // Extract message ID if possible
            const messageIdMatch = message.content.match(/message_id=([a-zA-Z0-9-_]+)/);
            if (messageIdMatch && messageIdMatch[1]) {
              const messageId = messageIdMatch[1];
              const targetMsg = this.messages.find(msg => msg.id === messageId);
              if (targetMsg) {
                targetMsg.isDestroyed = true;
                targetMsg.viewStarted = false;
                targetMsg.countdown = 0;
                targetMsg.imageData = null; // Clear the image data
              }
            }
          }
          else {
            // If we've cancelled search but server sends "waiting" message, ignore it
            if (message.content.includes('Đang chờ kết nối với người lạ') && this.cancelAutoSearch) {
              console.log('Ignoring "waiting" message after search cancellation');
              return; // Skip adding this message
            }
            
            // Display the status message but don't initiate any search
            this.messages.push({
              id: 'system-' + Date.now(),
              sender: 'system',
              content: message.content,
              timestamp: Date.now(),
              isSystemMessage: true
            });
            
            // Set isSearching based on message content
            if (message.content.includes('Đang chờ kết nối với người lạ')) {
              this.isSearching = true;
              // Don't add this system message to the messages array
              return; // Skip adding this message
            }
            
            // If this is the "Đang tìm kiếm" status but we're in a disconnected state,
            // override the automatic search
            if (message.content.includes('Đang tìm') && !this.isPaired && this.cancelAutoSearch) {
              // Cancel the search and show disconnected state
              this.endSearch();
            }
          }
          
          this.$nextTick(() => {
            this.scrollToBottom();
          });
          break;
          
        case 'disconnected':
          this.isPaired = false;
          
          // Display system message about disconnection
          this.messages.push({
            id: 'system-' + Date.now(),
            sender: 'system',
            content: 'Người trò chuyện đã ngắt kết nối. Bạn có thể bấm nút "Tìm người mới" khi muốn bắt đầu cuộc trò chuyện mới.',
            timestamp: Date.now(),
            isSystemMessage: true
          });
          
          this.$nextTick(() => {
            this.scrollToBottom();
          });
          break;
          
        case 'leave':
          this.isPaired = false;
          
          // Check if we initiated the leave (for search cancellation)
          if (this.cancelAutoSearch) {
            // This is our own leave message for cancelling search, already handled in cancelSearch
            console.log('Search cancellation confirmed');
            return;
          }
          
          // Show message about partner leaving
          this.messages.push({
            id: 'system-' + Date.now(),
            sender: 'system',
            content: 'Người trò chuyện đã kết thúc cuộc hội thoại.',
            timestamp: Date.now(),
            isSystemMessage: true
          });
          
          // Scroll to bottom for system messages
          this.$nextTick(() => {
            this.scrollToBottom();
            
            // Do not automatically find a new partner if user has canceled auto-search
            if (!this.cancelAutoSearch) {
              // Add message about auto-searching
              this.messages.push({
                id: 'system-' + Date.now(),
                sender: 'system',
                content: 'Đang tự động tìm người mới...',
                timestamp: Date.now(),
                isSystemMessage: true
              });
              
              // Clear messages and start search after a short delay
              setTimeout(() => {
                this.messages = [];
                this.findNewPartner();
              }, 2000);
            }
          });
          break;
          
        case 'message':
          if (message.sender && (message.content || message.imageData || message.fileData || message.voiceData || message.videoData)) {
            // If the message is from the current user, no need to add it again
            // as we've already added it when sending
            if (message.sender !== this.sessionId && message.sender !== 'user') {
              console.log('Adding message from stranger:', {
                hasContent: !!message.content,
                hasImage: !!message.imageData,
                hasFile: !!message.fileData,
                hasVoice: !!message.voiceData,
                hasVideo: !!message.videoData,
                selfDestruct: !!message.selfDestruct,
                selfDestructTime: message.selfDestructTime
              });
              
              // Play notification sound if window is not focused
              if (!this.windowFocused) {
                this.playNotificationSound();
                this.unreadMessages++;
                document.title = `(${this.unreadMessages}) Chat Anonymous`;
              }
              
              const newMessage = {
                id: message.id || 'msg-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9),
                sender: message.sender,
                content: message.content,
                imageData: message.imageData,
                fileData: message.fileData,
                voiceData: message.voiceData,
                videoData: message.videoData,
                fileName: message.fileName,
                fileType: message.fileType,
                fileSize: message.fileSize,
                selfDestruct: message.selfDestruct,
                selfDestructTime: message.selfDestructTime,
                viewStarted: false,
                isDestroyed: false,
                countdown: message.selfDestructTime,
                reactions: [],
                timestamp: message.timestamp || Date.now()
              };
              
              this.messages.push(newMessage);
              
              // Set isRead on previous messages from the user
              this.messages.forEach(msg => {
                if (msg.sender === 'user' && !msg.isRead) {
                  msg.isRead = true;
                }
              });
              
              // Start countdown for self-destructing images immediately
              if (message.imageData && message.selfDestruct) {
                // Start the countdown immediately without requiring viewing
                newMessage.viewStarted = true;
                this.startCountdownForMessage(newMessage.id, message.selfDestructTime);
              }
              
              // Check if user is near bottom before scrolling
              this.$nextTick(() => {
                if (this.isUserNearBottom()) {
                  this.scrollToBottom();
                }
              });
              
              // Send read receipt
              this.sendReadReceipt();
            }
          }
          break;
          
        case 'typing':
          // Display typing indicator
          if (message.sender !== this.sessionId && message.sender !== 'user') {
            this.isTyping = true;
            
            // Clear existing timeout if any
            if (this.typingTimeout) {
              clearTimeout(this.typingTimeout);
            }
            
            // Hide typing indicator after 3 seconds of inactivity
            this.typingTimeout = setTimeout(() => {
              this.isTyping = false;
            }, 3000);
          }
          break;
          
        case 'read_receipt':
          // Mark messages as read
          this.messages.forEach(msg => {
            if (msg.sender === 'user' && !msg.isRead) {
              msg.isRead = true;
            }
          });
          break;
          
        case 'reaction':
          // Add reaction to a message
          if (message.messageId && message.reaction) {
            const targetMsg = this.messages.find(msg => msg.id === message.messageId);
            if (targetMsg) {
              const exists = targetMsg.reactions.some(r => 
                r.emoji === message.reaction && r.sender === message.sender
              );
              
              if (!exists) {
                targetMsg.reactions.push({
                  emoji: message.reaction,
                  sender: message.sender
                });
              }
            }
          }
          break;
          
        case 'cancel_search':
          // Handle the cancel_search message type
          this.isSearching = false;
          
          // Add a system message about canceling search if not already displayed
          if (!this.messages.some(msg => msg.content && msg.content.includes('hủy tìm kiếm'))) {
            this.messages.push({
              id: 'system-' + Date.now(),
              sender: 'system',
              content: message.content || 'Bạn đã hủy tìm kiếm. Bấm nút "Tìm người mới" khi bạn muốn tìm người trò chuyện.',
              timestamp: Date.now(),
              isSystemMessage: true
            });
          }
          
          this.$nextTick(() => {
            this.scrollToBottom();
          });
          break;
          
        case 'image_destroyed':
          // Handle image destruction notification
          if (message.messageId) {
            const targetMsg = this.messages.find(msg => msg.id === message.messageId);
            if (targetMsg) {
              console.log('Image destroyed for message ID:', message.messageId);
              targetMsg.isDestroyed = true;
              targetMsg.viewStarted = false;
              targetMsg.countdown = 0;
              targetMsg.imageData = null; // Clear the image data to free memory
            }
          }
          break;
          
        default:
          console.log('Unknown message type:', message.type);
      }
    },
    
    sendMessage() {
      // Check if socket connection is open
      if (!this.socket || this.socket.readyState !== WebSocket.OPEN) {
        this.showToast('Đang kết nối lại...');
        return;
      }
      
      // Check if user is paired with someone before sending a message
      if (!this.isPaired) {
        this.showToast('Đang chờ kết nối với người lạ, không thể gửi tin nhắn...');
        return;
      }

      try {
        // Handle image with self-destruct
        if (this.selectedImage) {
          // Xử lý tin nhắn có hình ảnh
          console.log("Sending image with selfDestruct:", this.isSelfDestruct, 
                     "time:", this.selfDestructTime, 
                     "image length:", this.selectedImage.length);
          
          const messageId = this.generateMessageId();
          
          const message = {
            type: 'message',
            id: messageId,
            content: this.newMessage,
            imageData: this.selectedImage,
            selfDestruct: this.isSelfDestruct === true,  // Ensure proper boolean
            selfDestructTime: this.isSelfDestruct ? parseInt(this.selfDestructTime, 10) : null,  // Ensure proper number
            timestamp: new Date().getTime()
          };
          
          // Check size before sending
          const jsonMessage = JSON.stringify(message);
          console.log("Image message JSON size:", jsonMessage.length, "bytes");
          
          if (jsonMessage.length > 750000) {
            console.warn("Message too large:", jsonMessage.length, "bytes");
            this.showToast('Hình ảnh quá lớn, đang cố gắng nén lại...');
            
            this.recompressLargeImage(this.selectedImage)
              .then(compressedImage => {
                console.log("Recompressed from", this.selectedImage.length, "to", compressedImage.length);
                this.selectedImage = compressedImage;
                this.sendMessage(); // Try again with compressed image
              })
              .catch(err => {
                console.error("Compression failed:", err);
                this.showToast('Không thể gửi hình ảnh do kích thước quá lớn');
              });
            return;
          }
          
          this.socket.send(jsonMessage);
          
          // Thêm tin nhắn vào danh sách để hiển thị
          const newMsg = {
            id: messageId,
            content: this.newMessage,
            imageData: this.selectedImage,
            selfDestruct: message.selfDestruct,
            selfDestructTime: message.selfDestructTime,
            timestamp: message.timestamp,
            countdown: message.selfDestructTime,
            viewStarted: false,
            isDestroyed: false,
            sender: 'user'
          };
          
          this.messages.push(newMsg);
          
          // Start self-destruct countdown for our own messages too
          if (this.isSelfDestruct) {
            newMsg.viewStarted = true;
            this.startCountdownForMessage(messageId, this.selfDestructTime);
          }
          
          // Reset after sending
          this.newMessage = '';
          this.selectedImage = null;
          this.isSelfDestruct = false;
          
          // Save chat
          this.saveChat();
          
          // Ensure scrolling to bottom after sending
          this.$nextTick(() => {
            this.scrollToBottom();
          });
          
          return; // Exit since we've handled the image case
        } else if (this.selectedFile) {
          // Xử lý tin nhắn có file
          const message = {
            type: 'message',
            content: this.newMessage,
            fileData: this.selectedFile.data,
            fileName: this.selectedFile.name,
            fileType: this.selectedFile.type,
            fileSize: this.selectedFile.size,
            timestamp: new Date().getTime()
          };
          
          this.socket.send(JSON.stringify(message));
          
          // Thêm tin nhắn vào danh sách để hiển thị
          this.messages.push({
            id: this.generateMessageId(),
            content: this.newMessage,
            fileData: this.selectedFile.data,
            fileName: this.selectedFile.name,
            fileType: this.selectedFile.type,
            fileSize: this.selectedFile.size,
            timestamp: message.timestamp,
            sender: 'user'
          });
          
          // Add scrolling after file message is sent
          this.$nextTick(() => {
            this.scrollToBottom();
          });
        } else {
          // Xử lý tin nhắn text
          const message = {
            type: 'message',
            content: this.newMessage,
            timestamp: new Date().getTime()
          };
          
          this.socket.send(JSON.stringify(message));
          
          // Thêm tin nhắn vào danh sách để hiển thị
          this.messages.push({
            id: this.generateMessageId(),
            content: this.newMessage,
            timestamp: message.timestamp,
            sender: 'user'
          });
          
          // Add scrolling after text message is sent
          this.$nextTick(() => {
            this.scrollToBottom();
          });
        }
        
        // Lưu lịch sử chat
        this.saveChat();
        
        // Reset message input and ensure scrolling to newest messages
        this.newMessage = '';
        this.$nextTick(() => {
          this.scrollToBottom();
          if (this.$refs.messageInput) {
            this.$refs.messageInput.focus();
          }
        });
      } catch (error) {
        console.error('Lỗi khi gửi tin nhắn:', error);
        this.showToast('Không thể gửi tin nhắn. Vui lòng thử lại.');
      }
    },
    
    sendReadReceipt() {
      if (this.socket && this.isPaired) {
        const message = {
          type: 'read_receipt',
          sender: 'user'
        };
        this.socket.send(JSON.stringify(message));
      }
    },
    
    handleTypingEvent() {
      // Send typing indicator
      if (this.socket && this.isPaired) {
        // Limit sending typing events to avoid flooding
        if (!this._typingThrottled) {
          const message = {
            type: 'typing',
            sender: 'user'
          };
          this.socket.send(JSON.stringify(message));
          
          this._typingThrottled = true;
          setTimeout(() => {
            this._typingThrottled = false;
          }, 2000); // Only send typing event every 2 seconds max
        }
      }
    },
    
    findNewPartner() {
      // Reset the cancel flag when intentionally searching
      this.cancelAutoSearch = false;
      
      // Clear all messages first
      this.messages = [];
      
      // Set searching state
      this.isSearching = true;
      
      // Add system message about searching
      this.messages.push({
        id: 'system-' + Date.now(),
        sender: 'system',
        content: 'Đang tìm kiếm người trò chuyện mới...',
        timestamp: Date.now(),
        isSystemMessage: true
      });
      
      // Then proceed with finding a new partner
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(JSON.stringify({ 
          type: 'find',
          content: '',
          sender: 'user'
        }));
      } else {
        this.connectToChat();
      }
      
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },
    
    endChat() {
      if (this.socket && this.isConnected) {
        // Clear all messages first
        this.messages = [];
        
        // Set searching state only if auto-search is enabled
        this.isSearching = !this.cancelAutoSearch;
        
        // Add system message about ending chat
        this.messages.push({
          id: 'system-' + Date.now(),
          sender: 'system',
          content: 'Bạn đã kết thúc cuộc trò chuyện.',
          timestamp: Date.now(),
          isSystemMessage: true
        });
        
        const message = {
          type: 'leave',
          content: '',
          sender: 'user'
        };
        this.socket.send(JSON.stringify(message));
        this.isPaired = false;
        
        this.$nextTick(() => {
          this.scrollToBottom();
          
          // Only automatically find a new partner if auto-search is not canceled
          if (!this.cancelAutoSearch) {
            // Add message about auto-searching
            this.messages.push({
              id: 'system-' + Date.now(),
              sender: 'system',
              content: 'Đang tự động tìm người mới...',
              timestamp: Date.now(),
              isSystemMessage: true
            });
            
            // Automatically find a new partner after a short delay
            setTimeout(() => {
              this.findNewPartner();
            }, 1000);
          }
        });
      }
    },
    
    scrollToBottom() {
      if (this.$refs.messageEnd) {
        this.$refs.messageEnd.scrollIntoView({ behavior: 'smooth' });
      }
      
      // Also add direct scrolling to the container as a fallback
      if (this.$refs.chatMessages) {
        setTimeout(() => {
          this.$refs.chatMessages.scrollTop = this.$refs.chatMessages.scrollHeight;
        }, 100);
      }
    },
    
    formatTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    },
    
    formatDate(dateString) {
      const date = new Date(dateString);
      const today = new Date();
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      
      if (date.toDateString() === today.toDateString()) {
        return 'Hôm nay';
      } else if (date.toDateString() === yesterday.toDateString()) {
        return 'Hôm qua';
      } else {
        return date.toLocaleDateString('vi-VN', { 
          day: 'numeric', 
          month: 'numeric',
          year: 'numeric'
        });
      }
    },
    
    formatFileSize(bytes) {
      if (bytes < 1024) {
        return bytes + ' bytes';
      } else if (bytes < 1024 * 1024) {
        return (bytes / 1024).toFixed(1) + ' KB';
      } else {
        return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
      }
    },
    
    formatRecordingTime(seconds) {
      const minutes = Math.floor(seconds / 60);
      const remainingSeconds = seconds % 60;
      return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
    },
    
    toggleEmojiPicker() {
      this.showEmojiPicker = !this.showEmojiPicker;
      if (this.showEmojiPicker) {
        this.filteredEmojis = [...this.emojis];
        this.showAttachmentOptions = false;
      }
    },
    
    toggleAttachmentOptions() {
      this.showAttachmentOptions = !this.showAttachmentOptions;
      if (this.showAttachmentOptions) {
        this.showEmojiPicker = false;
      }
    },
    
    addEmoji(emoji) {
      this.newMessage += emoji;
      this.showEmojiPicker = false;
      this.$refs.messageInput.focus();
    },
    
    filterEmojis(category) {
      // This is a simplified filter, in a real app you'd have proper emoji categories
      if (category === '😊') {
        this.filteredEmojis = this.emojis.filter(e => /😀|😃|😄|😁|😆|😅|😂|🤣|😊|😇/.test(e));
      } else if (category === '🙌') {
        this.filteredEmojis = this.emojis.filter(e => /👍|👏|🙌|👌|🤝|✌️/.test(e));
      } else if (category === '🔥') {
        this.filteredEmojis = this.emojis.filter(e => /🔥|💥|⭐|✨|💫|💯/.test(e));
      } else {
        this.filteredEmojis = [...this.emojis];
      }
    },
    
    addReaction(messageId, emoji) {
      if (this.socket && this.isPaired) {
        const message = {
          type: 'reaction',
          messageId: messageId,
          reaction: emoji,
          sender: 'user'
        };
        this.socket.send(JSON.stringify(message));
        
        // Add to local message immediately
        const targetMsg = this.messages.find(msg => msg.id === messageId);
        if (targetMsg) {
          const exists = targetMsg.reactions.some(r => 
            r.emoji === emoji && r.sender === 'user'
          );
          
          if (!exists) {
            targetMsg.reactions.push({
              emoji: emoji,
              sender: 'user'
            });
          }
        }
      }
    },
    
    triggerImageUpload() {
      if (this.$refs.imageInput) {
        this.$refs.imageInput.click();
      }
      this.showAttachmentOptions = false;
    },
    
    triggerVideoUpload() {
      if (this.$refs.videoInput) {
        this.$refs.videoInput.click();
      }
      this.showAttachmentOptions = false;
    },
    
    triggerFileUpload() {
      if (this.$refs.fileInput) {
        this.$refs.fileInput.click();
      }
      this.showAttachmentOptions = false;
    },
    
    onImageSelected(event) {
      const file = event.target.files[0];
      if (!file) return;
      
      // Check file type and size
      if (!file.type.match('image.*')) {
        alert('Vui lòng chọn file hình ảnh');
        return;
      }
      
      if (file.size > 5 * 1024 * 1024) { // 5MB limit
        alert('Kích thước hình ảnh không được vượt quá 5MB');
        return;
      }
      
      console.log('Image selected:', file.name, file.size, 'bytes, type:', file.type);
      
      // Reset self-destruct options when selecting a new image
      this.isSelfDestruct = false;
      this.selfDestructTime = 5;
      
      // Compress image before sending
      this.compressImage(file)
        .then(compressedImage => {
          console.log('Image compressed successfully', 'Original:', file.size, 
                    'bytes, Compressed size:', Math.round(compressedImage.length * 0.75), 
                    'bytes, Ratio:', Math.round(compressedImage.length * 0.75 / file.size * 100) + '%');
          
          // Validate the image data format
          if (!compressedImage.startsWith('data:image/')) {
            console.error('Invalid image format after compression:', compressedImage.substring(0, 30) + '...');
            this.showToast('Lỗi khi xử lý hình ảnh. Vui lòng thử lại.');
            return;
          }
          
          this.selectedImage = compressedImage;
        })
        .catch(error => {
          console.error('Error compressing image:', error);
          
          // Fallback to original image if compression fails
          const reader = new FileReader();
          reader.onload = (e) => {
            this.selectedImage = e.target.result;
          };
          reader.readAsDataURL(file);
        });
      
      // Reset the input so the same file can be selected again
      event.target.value = '';
    },
    
    // Thêm lại phương thức nén hình ảnh
    compressImage(file) {
      return new Promise((resolve, reject) => {
        const maxWidth = 1000;
        const maxHeight = 1000;
        const maxQuality = 0.7; // Giảm chất lượng xuống để giảm kích thước
        
        const img = new Image();
        img.src = URL.createObjectURL(file);
        
        img.onload = () => {
          // Create canvas
          const canvas = document.createElement('canvas');
          
          // Calculate new dimensions
          let width = img.width;
          let height = img.height;
          
          // Scale down if image is too large
          if (width > maxWidth || height > maxHeight) {
            const ratio = Math.min(maxWidth / width, maxHeight / height);
            width = Math.floor(width * ratio);
            height = Math.floor(height * ratio);
          }
          
          // Set canvas dimensions
          canvas.width = width;
          canvas.height = height;
          
          // Draw image on canvas
          const ctx = canvas.getContext('2d');
          ctx.drawImage(img, 0, 0, width, height);
          
          // Return as base64 string directly instead of blob
          const dataUrl = canvas.toDataURL('image/jpeg', maxQuality);
          console.log('Kích thước trước khi nén:', file.size, 'bytes, sau khi nén:', Math.round(dataUrl.length * 0.75), 'bytes (estimate), tỷ lệ nén:', Math.round(dataUrl.length * 0.75 / file.size * 100) + '%');
          resolve(dataUrl);
        };
        
        img.onerror = () => {
          reject(new Error('Lỗi khi tải hình ảnh'));
        };
      });
    },
    
    onFileSelected(event) {
      const file = event.target.files[0];
      if (!file) return;
      
      // Check file size
      if (file.size > 10 * 1024 * 1024) { // 10MB limit
        alert('Kích thước tệp không được vượt quá 10MB');
        return;
      }
      
      this.selectedFile = file;
      
      const reader = new FileReader();
      reader.onload = (e) => {
        this.fileData = e.target.result;
      }
      reader.readAsDataURL(file);
      
      // Reset the input so the same file can be selected again
      event.target.value = '';
    },
    
    cancelImage() {
      this.selectedImage = null;
    },
    
    cancelFile() {
      this.selectedFile = null;
      this.fileData = null;
    },
    
    openImagePreview(imageUrl, selfDestruct, selfDestructTime, messageId) {
      this.previewImage = imageUrl;
      this.currentPreviewMessageId = messageId;
      this.isDestroyed = false;
      this.isSelfDestruct = selfDestruct;
      this.selfDestructTime = selfDestructTime;
      
      // Get the current message
      const message = this.messages.find(m => m.id === messageId);
      if (!message) return;
      
      // Use the current countdown if already started, otherwise use initial time
      this.countdown = message.countdown || selfDestructTime;
      
      // If the image hasn't been destroyed yet, just sync with the existing countdown
      // We don't need to start a new countdown since it should already be running
      if (selfDestruct && !message.isDestroyed) {
        // The countdown should already be running automatically
        // Just make sure we're displaying the correct countdown
        if (!message.viewStarted) {
          message.viewStarted = true;
          
          // If the countdown wasn't started yet, start it
          if (message.sender !== 'user') {
            this.sendImageViewedNotification(messageId);
          }
        }
      }
    },
    
    closeImagePreview() {
      // Nếu hình ảnh đang hiển thị và tự hủy, cập nhật thông báo đã hủy
      if (this.previewImage && this.isSelfDestruct) {
        // Dừng bộ đếm ngược
        clearInterval(this.countdownInterval);
        
        // Cập nhật trạng thái tin nhắn
        const messageId = this.currentPreviewMessageId;
        if (messageId) {
          const message = this.messages.find(m => m.id === messageId);
          if (message) {
            if (this.isDestroyed) {
              message.isDestroyed = true;
              message.imageData = null; // Xóa hình ảnh khỏi bộ nhớ
              
              // Gửi thông báo tự hủy đến người gửi
              if (message.sender !== 'user') {
                this.sendImageDestroyedNotification(messageId);
              }
            }
          }
        }
      }
      
      this.previewImage = null;
      this.currentPreviewMessageId = null;
      this.isSelfDestruct = false;
      this.isDestroyed = false;
      this.countdown = 0;
      clearInterval(this.countdownInterval);
    },
    
    formatMessageContent(content) {
      if (!content) return '';
      
      // Convert URLs to clickable links
      content = content.replace(
        /(https?:\/\/[^\s]+)/g, 
        '<a href="$1" target="_blank" class="text-blue-500 hover:underline">$1</a>'
      );
      
      // Convert markdown bold
      content = content.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
      
      // Convert markdown italic
      content = content.replace(/\*(.*?)\*/g, '<em>$1</em>');
      
      // Convert markdown code
      content = content.replace(/`(.*?)`/g, '<code class="bg-gray-100 dark:bg-gray-700 px-1 rounded text-sm">$1</code>');
      
      return content;
    },
    
    startVoiceRecording() {
      if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
        alert('Trình duyệt của bạn không hỗ trợ ghi âm.');
        return;
      }
      
      navigator.mediaDevices.getUserMedia({ audio: true })
        .then(stream => {
          this.isRecordingVoice = true;
          this.recordedChunks = [];
          this.recordingTime = 0;
          
          this.mediaRecorder = new MediaRecorder(stream);
          
          this.mediaRecorder.ondataavailable = (e) => {
            if (e.data.size > 0) {
              this.recordedChunks.push(e.data);
            }
          };
          
          this.mediaRecorder.onstop = () => {
            const blob = new Blob(this.recordedChunks, { type: 'audio/webm' });
            const reader = new FileReader();
            reader.onload = (e) => {
              // Send voice message
              if (this.socket && this.isPaired) {
                const message = {
                  type: 'message',
                  content: '',
                  voiceData: e.target.result,
                  sender: 'user',
                  id: 'msg-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9)
                };
                
                this.socket.send(JSON.stringify(message));
                
                // Add locally
                this.messages.push({
                  id: message.id,
                  sender: 'user',
                  content: '',
                  voiceData: e.target.result,
                  reactions: [],
                  isRead: false,
                  timestamp: Date.now()
                });
                
                this.$nextTick(() => {
                  this.scrollToBottom();
                });
              }
            };
            reader.readAsDataURL(blob);
            
            // Clean up
            stream.getTracks().forEach(track => track.stop());
          };
          
          this.mediaRecorder.start();
          
          // Update recording time
          this.recordingInterval = setInterval(() => {
            this.recordingTime++;
            
            // Auto stop after 1 minute
            if (this.recordingTime >= 60) {
              this.finishVoiceRecording();
            }
          }, 1000);
        })
        .catch(error => {
          console.error('Error accessing microphone:', error);
          alert('Không thể truy cập microphone. Vui lòng kiểm tra quyền truy cập.');
        });
    },
    
    cancelVoiceRecording() {
      if (this.mediaRecorder && this.isRecordingVoice) {
        this.mediaRecorder.stop();
        clearInterval(this.recordingInterval);
        this.isRecordingVoice = false;
        this.recordingTime = 0;
      }
    },
    
    finishVoiceRecording() {
      if (this.mediaRecorder && this.isRecordingVoice) {
        this.mediaRecorder.stop();
        clearInterval(this.recordingInterval);
        this.isRecordingVoice = false;
      }
    },
    
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode ? 'true' : 'false');
      
      // Apply dark mode to document
      if (this.darkMode) {
        document.documentElement.classList.add('dark');
      } else {
        document.documentElement.classList.remove('dark');
      }
    },
    
    playNotificationSound() {
      // Check if sound exists and play it
      try {
        this.notificationSound.play();
      } catch (error) {
        console.error('Error playing notification sound:', error);
      }
    },
    
    // Nén hình ảnh thêm lần nữa với chất lượng thấp hơn
    recompressLargeImage(dataUrl) {
      return new Promise((resolve, reject) => {
        if (!dataUrl || typeof dataUrl !== 'string') {
          reject(new Error('Invalid image data provided'));
          return;
        }

        // Check if it's already a data URL
        if (!dataUrl.startsWith('data:image/')) {
          reject(new Error('Image data doesn\'t have the expected format'));
          return;
        }

        const img = new Image();
        img.onload = () => {
          console.log("Original dimensions for recompression:", img.width, "x", img.height);
          
          // Create canvas
          const canvas = document.createElement('canvas');
          
          // Calculate new dimensions (reduce size)
          const maxDimension = 800;
          let width = img.width;
          let height = img.height;
          
          if (width > height) {
            if (width > maxDimension) {
              height = Math.round(height * maxDimension / width);
              width = maxDimension;
            }
          } else {
            if (height > maxDimension) {
              width = Math.round(width * maxDimension / height);
              height = maxDimension;
            }
          }
          
          console.log("Recompressing to dimensions:", width, "x", height);
          
          canvas.width = width;
          canvas.height = height;
          
          const ctx = canvas.getContext('2d');
          ctx.drawImage(img, 0, 0, width, height);
          
          // Use lower quality setting for more compression
          const compressedDataUrl = canvas.toDataURL('image/jpeg', 0.5);
          
          console.log('Recompression: original size:', dataUrl.length, 
                     'bytes, new size:', compressedDataUrl.length, 
                     'bytes, ratio:', Math.round(compressedDataUrl.length / dataUrl.length * 100) + '%');
          
          // Validate format again
          if (!compressedDataUrl.startsWith('data:image/')) {
            reject(new Error('Failed to produce valid image format after compression'));
            return;
          }
          
          resolve(compressedDataUrl);
        };
        
        img.onerror = () => {
          reject(new Error('Error loading image for recompression'));
        };
        
        img.src = dataUrl;
      });
    },
    
    // Hàm riêng để gửi tin nhắn (để tái sử dụng trong trường hợp nén hình ảnh)
    doSendMessage(imageData) {
      // Generate unique ID for the message
      const messageId = 'msg-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
      
      // Prepare the message
      const message = {
        type: 'message',
        content: this.newMessage,
        sender: 'user',
        id: messageId
      };
      
      // If there's an image, add it to the message
      if (imageData) {
        message.imageData = imageData;
        console.log('Gửi tin nhắn có hình ảnh, kích thước sau khi nén:', imageData.length, 'bytes');
      }
      
      try {
        // Send the message
        this.socket.send(JSON.stringify(message));
        
        // Add to messages locally immediately
        this.messages.push({
          id: messageId,
          sender: 'user',
          content: this.newMessage,
          imageData: imageData,
          reactions: [],
          isRead: false,
          timestamp: Date.now()
        });
        
        // Reset input fields
        this.newMessage = '';
        this.selectedImage = null;
        
        this.$nextTick(() => {
          this.scrollToBottom();
          if (this.$refs.messageInput) {
            this.$refs.messageInput.focus();
          }
        });
      } catch (error) {
        console.error('Lỗi khi gửi tin nhắn:', error);
        alert('Có lỗi khi gửi tin nhắn. Vui lòng thử lại.');
      }
    },
    
    startCountdown(messageId) {
      // Xóa interval cũ nếu có
      if (this.countdownInterval) {
        clearInterval(this.countdownInterval);
      }
      
      this.countdownInterval = setInterval(() => {
        // Giảm bộ đếm ngược
        this.countdown--;
        
        // Cập nhật bộ đếm ngược trong tin nhắn
        const message = this.messages.find(m => m.id === messageId);
        if (message) {
          message.countdown = this.countdown;
        }
        
        // Kiểm tra nếu hết thời gian
        if (this.countdown <= 0) {
          clearInterval(this.countdownInterval);
          this.isDestroyed = true;
          
          // Cập nhật tin nhắn để đánh dấu đã hủy
          if (message) {
            message.isDestroyed = true;
            message.imageData = null; // Xóa hình ảnh khỏi bộ nhớ
            
            // Gửi thông báo tự hủy đến người gửi
            if (message.sender !== 'user') {
              this.sendImageDestroyedNotification(messageId);
            }
          }
        }
      }, 1000);
    },
    
    sendImageViewedNotification(messageId) {
      if (this.socket && this.isPaired) {
        const notification = {
          type: 'image_viewed',
          messageId: messageId,
          sender: 'user'
        };
        this.socket.send(JSON.stringify(notification));
      }
    },
    
    sendImageDestroyedNotification(messageId) {
      if (this.socket && this.isPaired) {
        const notification = {
          type: 'image_destroyed',
          messageId: messageId,
          sender: 'user'
        };
        this.socket.send(JSON.stringify(notification));
      }
    },
    
    generateMessageId() {
      return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
    },
    
    saveChat() {
      // Implement chat saving logic
      console.log('Chat saved:', this.messages);
    },
    
    showToast(message) {
      // Implement toast notification logic
      console.log(message);
    },
    
    // Add a dedicated method for sending an image message
    sendImageMessage() {
      if (!this.selectedImage || !this.socket || this.socket.readyState !== WebSocket.OPEN) {
        return;
      }
      
      console.log("Sending image message with self-destruct:", this.isSelfDestruct, 
                 "time:", this.selfDestructTime, 
                 "imageLength:", this.selectedImage.length);
      
      // Create the message with proper type conversion
      const message = {
        type: 'message',
        content: this.newMessage,
        imageData: this.selectedImage,
        selfDestruct: this.isSelfDestruct === true,  // Ensure boolean
        selfDestructTime: this.isSelfDestruct ? Number(this.selfDestructTime) : null,  // Ensure number
        timestamp: new Date().getTime()
      };
      
      try {
        // Convert to JSON and check size
        const jsonMessage = JSON.stringify(message);
        console.log("Image message JSON size:", jsonMessage.length, "bytes");
        
        if (jsonMessage.length > 750000) {
          console.warn("Image message too large:", jsonMessage.length, "bytes");
          this.showToast('Hình ảnh quá lớn, đang cố gắng nén lại...');
          
          // Try to recompress
          this.recompressLargeImage(this.selectedImage)
            .then(compressedImage => {
              console.log("Recompressed image from", this.selectedImage.length, 
                         "to", compressedImage.length, "bytes");
              this.selectedImage = compressedImage;
              this.sendImageMessage(); // Try again with the compressed image
            })
            .catch(err => {
              console.error("Failed to recompress image:", err);
              this.showToast('Không thể gửi hình ảnh do kích thước quá lớn');
            });
          return;
        }
        
        // Send the message
        this.socket.send(jsonMessage);
        
        // Add to local messages
        this.messages.push({
          id: this.generateMessageId(),
          content: this.newMessage,
          imageData: this.selectedImage,
          selfDestruct: message.selfDestruct,
          selfDestructTime: message.selfDestructTime,
          timestamp: message.timestamp,
          sender: 'user'
        });
        
        // Reset input
        this.newMessage = '';
        this.selectedImage = null;
        this.isSelfDestruct = false;
        
        // Save chat
        this.saveChat();
        
        // Use both setTimeout and nextTick to ensure scrolling happens after DOM update
        this.$nextTick(() => {
          this.scrollToBottom();
          
          // Add a second delayed scroll to handle any pending image loading
          setTimeout(() => {
            this.scrollToBottom();
          }, 300);
        });
      } catch (error) {
        console.error("Error sending image message:", error);
        this.showToast('Lỗi khi gửi hình ảnh: ' + error.message);
      }
    },
    
    // Add new method to handle starting countdown for a specific message
    startCountdownForMessage(messageId, duration) {
      // Create a unique interval ID for this message
      const intervalKey = `countdown_${messageId}`;
      
      // Clear any existing interval for this message
      if (this[intervalKey]) {
        clearInterval(this[intervalKey]);
      }
      
      // Set initial countdown value
      const message = this.messages.find(m => m.id === messageId);
      if (message) {
        message.countdown = duration;
      }
      
      // Start the countdown
      this[intervalKey] = setInterval(() => {
        const message = this.messages.find(m => m.id === messageId);
        if (!message) {
          clearInterval(this[intervalKey]);
          return;
        }
        
        // Decrease the countdown
        message.countdown--;
        
        // Check if countdown has ended
        if (message.countdown <= 0) {
          clearInterval(this[intervalKey]);
          
          // Destroy the image
          message.isDestroyed = true;
          message.imageData = null;
          
          // Notify the sender
          if (message.sender !== 'user') {
            this.sendImageDestroyedNotification(messageId);
          }
        }
      }, 1000);
    },
    
    isUserNearBottom() {
      if (!this.$refs.chatMessages) return true;
      
      const container = this.$refs.chatMessages;
      const atBottom = container.scrollHeight - container.clientHeight - container.scrollTop;
      return atBottom < this.scrollThreshold;
    },
    
    scrollToBottomIfNearBottom() {
      // Only auto-scroll if the user was already near the bottom
      if (this.isNearBottom) {
        this.scrollToBottom();
      } else {
        // If user is scrolled up, show scroll button
        this.showScrollButton = true;
      }
    },
    
    handleScroll() {
      // Update the isNearBottom flag when user scrolls
      this.isNearBottom = this.isUserNearBottom();
      
      // Hide the scroll button if user has scrolled to bottom
      if (this.isNearBottom) {
        this.showScrollButton = false;
      }
    },
    
    startChatAnimation() {
      // First show animation, then start connection
      this.isConnecting = true;
      
      // Center the button container
      const buttonContainer = document.querySelector('.p-4.text-center');
      if (buttonContainer) {
        buttonContainer.classList.add('loading-center');
      }
      
      // Connect after animation starts
      setTimeout(() => {
        this.connectToChat();
      }, 300);
    },
    
    onVideoSelected(event) {
      const file = event.target.files[0];
      if (!file) return;
      
      // Check file type and size
      if (!file.type.match('video.*')) {
        alert('Vui lòng chọn file video');
        return;
      }
      
      if (file.size > 50 * 1024 * 1024) { // 50MB limit
        alert('Kích thước video không được vượt quá 50MB');
        return;
      }
      
      console.log('Video selected:', file.name, file.size, 'bytes, type:', file.type);
      
      const reader = new FileReader();
      reader.onload = (e) => {
        this.selectedVideo = e.target.result;
        this.sendVideoMessage();
      };
      reader.readAsDataURL(file);
      
      // Reset the input so the same file can be selected again
      event.target.value = '';
    },
    
    sendVideoMessage() {
      if (!this.selectedVideo || !this.socket || this.socket.readyState !== WebSocket.OPEN) {
        return;
      }
      
      console.log("Sending video message, size:", this.selectedVideo.length);
      
      // Create the message
      const message = {
        type: 'message',
        content: this.newMessage,
        videoData: this.selectedVideo,
        timestamp: new Date().getTime()
      };
      
      try {
        // Convert to JSON and check size
        const jsonMessage = JSON.stringify(message);
        console.log("Video message JSON size:", jsonMessage.length, "bytes");
        
        if (jsonMessage.length > 50000000) { // 50MB limit
          console.warn("Video message too large:", jsonMessage.length, "bytes");
          this.showToast('Video quá lớn, vui lòng chọn video nhỏ hơn');
          return;
        }
        
        // Send the message
        this.socket.send(jsonMessage);
        
        // Add to local messages
        this.messages.push({
          id: this.generateMessageId(),
          content: this.newMessage,
          videoData: this.selectedVideo,
          timestamp: message.timestamp,
          sender: 'user'
        });
        
        // Reset input
        this.newMessage = '';
        this.selectedVideo = null;
        
        // Save chat
        this.saveChat();
        
        // Scroll to bottom
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      } catch (error) {
        console.error("Error sending video message:", error);
        this.showToast('Lỗi khi gửi video: ' + error.message);
      }
    },
    
    // Add a new method to cancel an automatic search
    endSearch() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        // Send a message to leave the chat/queue instead of cancel_search
        this.socket.send(JSON.stringify({ 
          type: 'leave',  // Use 'leave' instead of 'cancel_search'
          content: '',
          sender: 'user'
        }));
        
        // Update UI to show we're disconnected but not searching
        this.messages = [];
        this.messages.push({
          id: 'system-' + Date.now(),
          sender: 'system',
          content: 'Bạn đã kết thúc tìm kiếm. Bấm nút "Tìm người mới" khi bạn muốn bắt đầu cuộc trò chuyện mới.',
          timestamp: Date.now(),
          isSystemMessage: true
        });
        
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
      
      // Set UI state to not searching
      this.isSearching = false;
      this.cancelAutoSearch = true; // Prevent auto-search from happening
    },
    
    cancelSearch() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        // Send a message to leave the chat/queue instead of cancel_search
        this.socket.send(JSON.stringify({ 
          type: 'leave',  // Use 'leave' instead of 'cancel_search'
          content: '',
          sender: 'user'
        }));
      }
      
      // Update UI to show we're no longer searching
      this.isSearching = false;
      this.cancelAutoSearch = true; // This prevents auto-search from happening
      
      // If the user was connected but not paired, they remain connected but not paired
      
      // Clear existing messages and add the cancellation message
      this.messages = [];
      this.messages.push({
        id: 'system-' + Date.now(),
        sender: 'system',
        content: 'Bạn đã hủy tìm kiếm. Bấm nút "Tìm người mới" khi bạn muốn tìm người trò chuyện.',
        timestamp: Date.now(),
        isSystemMessage: true
      });
      
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },
    
    fetchStats() {
      // Implement stats fetching logic
      console.log('Fetching stats...');
    }
  },
  
  mounted() {
    // Check for dark mode preference
    const savedDarkMode = localStorage.getItem('darkMode');
    if (savedDarkMode === 'true') {
      this.darkMode = true;
      document.documentElement.classList.add('dark');
    }
    
    // Initialize emojis
    this.filteredEmojis = [...this.emojis];
    
    // Check for existing connection in localStorage
    const savedSessionId = localStorage.getItem('chat_session_id');
    if (savedSessionId) {
      this.sessionId = savedSessionId;
      this.connectToChat();
    }
    
    // Listen for window focus/blur for notifications
    window.addEventListener('focus', () => {
      this.windowFocused = true;
      this.unreadMessages = 0;
      document.title = 'Chat Anonymous';
      
      // Send read receipts when window is focused
      if (this.isPaired) {
        this.sendReadReceipt();
      }
      
      // Fetch stats when window becomes active
      this.fetchStats();
    });
    
    window.addEventListener('blur', () => {
      this.windowFocused = false;
    });
    
    // Add scroll event listener
    this.$nextTick(() => {
      if (this.$refs.chatMessages) {
        this.$refs.chatMessages.addEventListener('scroll', this.handleScroll);
      }
    });
    
    // Initial stats fetch
    this.fetchStats();
    
    // Set up stats refresh interval
    this.statsInterval = setInterval(() => {
      this.fetchStats();
    }, 30000); // Refresh every 30 seconds
  },
  
  beforeUnmount() {
    if (this.socket) {
      this.socket.close();
    }
    
    // Clear intervals and timeouts
    if (this.typingTimeout) {
      clearTimeout(this.typingTimeout);
    }
    
    if (this.recordingInterval) {
      clearInterval(this.recordingInterval);
    }
    
    if (this.statsInterval) {
      clearInterval(this.statsInterval);
    }
    
    // Remove event listeners
    window.removeEventListener('focus', () => {});
    window.removeEventListener('blur', () => {});
    
    // Remove scroll event listener
    if (this.$refs.chatMessages) {
      this.$refs.chatMessages.removeEventListener('scroll', this.handleScroll);
    }
  }
}
</script>

<style scoped>
/* Responsive container styles */
.chat-container {
  height: 100%;
  max-height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: transparent;
}

/* Dark mode specific fixes */
:deep(.dark-mode) {
  background-color: transparent;
}

/* Mobile height fix for iOS Safari */
@supports (-webkit-touch-callout: none) {
  .chat-container {
    height: -webkit-fill-available;
  }
}

/* Ensure messages area takes available space */
.messages-container {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch; /* Smooth scrolling on iOS */
}

/* Loading overlay should fill the container */
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

/* Make sure input area is properly sized */
.chat-input-container {
  width: 100%;
  min-height: 60px;
  padding: 8px 12px;
}

/* Adjust for different mobile devices */
@media (max-height: 568px) {
  /* For smaller phones */
  .messages-container {
    padding: 8px !important;
  }

  .chat-input-container {
    padding: 4px 8px;
    min-height: 50px;
  }
}

/* Extra adjustments for landscape mode */
@media (max-height: 450px) and (orientation: landscape) {
  .header {
    padding-top: 4px;
    padding-bottom: 4px;
  }
}

/* Online badge styles */
.glass-btn, .dark\:glass-btn-dark {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(5px);
  transition: all 0.2s ease;
}

.glass-btn:hover, .dark\:glass-btn-dark:hover {
  transform: translateY(-1px);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
}

/* Thêm kiểu cho text cực nhỏ trên thiết bị di động */
.text-2xs {
  font-size: 0.7rem;
  line-height: 1rem;
}

.text-3xs {
  font-size: 0.6rem;
  line-height: 0.9rem;
}

/* Sửa lỗi cho tin nhắn trên màn hình nhỏ */
@media (max-width: 360px) {
  .message-bubble {
    max-width: 95%;
    padding: 0.5rem;
    font-size: 0.75rem;
  }
  
  .message-meta {
    font-size: 0.6rem;
  }
}

/* Responsive cho chế độ landscape */
@media (max-height: 500px) and (orientation: landscape) {
  .chat-container {
    max-height: calc(100vh - 10px);
  }
  
  .loading-spinner {
    transform: scale(0.7);
  }
  
  .messages-container {
    padding: 0.5rem;
  }
}

/* Điều chỉnh cho iPad và tablet */
@media (min-width: 768px) and (max-width: 1024px) {
  .message-bubble {
    max-width: 70%;
  }
}

/* Typing indicator dots */
.dot {
  width: 4px;
  height: 4px;
  background-color: currentColor;
  border-radius: 50%;
  display: inline-block;
  margin: 0 1px;
}

@media (min-width: 640px) {
  .dot {
    width: 5px;
    height: 5px;
    margin: 0 2px;
  }
}

/* Fix overlay khi loading */
.loading-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.8);
  z-index: 10;
}

.dark .loading-overlay {
  background-color: rgba(0, 0, 0, 0.7);
}
</style> 