<template>
  <div class="app-container min-h-screen bg-gradient-to-br from-white to-secondary-50 dark:from-black dark:to-gray-950 flex flex-col">
    <!-- Navigation bar -->
    <nav class="sticky top-0 z-50 backdrop-blur-sm bg-white/80 dark:bg-black/80 shadow-sm dark:shadow-black mb-2">
      <div class="max-w-7xl mx-auto px-2 sm:px-4 lg:px-6">
        <div class="flex flex-wrap justify-between h-auto sm:h-16 py-2 sm:py-0">
          <div class="flex items-center w-full sm:w-auto justify-center sm:justify-start mb-2 sm:mb-0">
            <router-link to="/" class="flex items-center hover:scale-105 transition-transform duration-300">
              <div class="w-8 h-8 sm:w-10 sm:h-10 bg-gradient-to-r from-secondary-500 to-primary-500 rounded-full flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 sm:h-6 sm:w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z" />
                </svg>
              </div>
              <span class="ml-2 text-base sm:text-xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-secondary-600 to-primary-600">Chat Anonymous</span>
            </router-link>
          </div>
          <div class="flex items-center w-full sm:w-auto justify-center sm:justify-end">
            <nav class="flex space-x-1">
              <router-link 
                to="/chat" 
                class="px-3 py-1.5 sm:px-4 sm:py-2 rounded-md text-xs sm:text-sm font-medium transition-all duration-300"
                :class="[$route.path === '/chat' ? 'bg-gradient-to-r from-secondary-100 to-primary-100 text-secondary-700 dark:from-secondary-900/40 dark:to-primary-900/40 dark:text-secondary-300' : 'hover:bg-gray-100 text-gray-700 dark:hover:bg-gray-800 dark:text-gray-300']"
              >
                Chat
              </router-link>
              <router-link 
                to="/stats" 
                class="px-3 py-1.5 sm:px-4 sm:py-2 rounded-md text-xs sm:text-sm font-medium transition-all duration-300"
                :class="[$route.path === '/stats' ? 'bg-gradient-to-r from-secondary-100 to-primary-100 text-secondary-700 dark:from-secondary-900/40 dark:to-primary-900/40 dark:text-secondary-300' : 'hover:bg-gray-100 text-gray-700 dark:hover:bg-gray-800 dark:text-gray-300']"
              >
                Thống kê
              </router-link>
              <router-link 
                to="/about" 
                class="px-3 py-1.5 sm:px-4 sm:py-2 rounded-md text-xs sm:text-sm font-medium transition-all duration-300"
                :class="[$route.path === '/about' ? 'bg-gradient-to-r from-secondary-100 to-primary-100 text-secondary-700 dark:from-secondary-900/40 dark:to-primary-900/40 dark:text-secondary-300' : 'hover:bg-gray-100 text-gray-700 dark:hover:bg-gray-800 dark:text-gray-300']"
              >
                Giới thiệu
              </router-link>
            </nav>
          </div>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <main class="flex-1 overflow-auto">
      <div class="max-w-7xl mx-auto px-2 sm:px-4 lg:px-6 h-full">
        <router-view class="h-full" v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: 'App',
  mounted() {
    // Check for dark mode preference
    const savedDarkMode = localStorage.getItem('darkMode');
    if (savedDarkMode === 'true') {
      document.documentElement.classList.add('dark');
    }
  },
  methods: {
    compressImage(file) {
      return new Promise((resolve, reject) => {
        const maxWidth = 1280;
        const maxHeight = 1280;
        const maxQuality = 0.7; // Reduced quality for smaller size
        
        const img = new Image();
        img.src = URL.createObjectURL(file);
        
        img.onload = () => {
          console.log("Original image dimensions:", img.width, "x", img.height);
          
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
            console.log("Resizing to:", width, "x", height);
          }
          
          // Set canvas dimensions
          canvas.width = width;
          canvas.height = height;
          
          // Draw image on canvas
          const ctx = canvas.getContext('2d');
          ctx.drawImage(img, 0, 0, width, height);
          
          // Get compressed data URL
          const compressedDataUrl = canvas.toDataURL('image/jpeg', maxQuality);
          console.log("Compressed image data length:", compressedDataUrl.length, 
                     "format:", compressedDataUrl.substring(0, 30) + "...");
          
          // Validate the data URL format
          if (!compressedDataUrl.startsWith('data:image/')) {
            reject(new Error('Invalid compressed image format'));
            return;
          }
          
          resolve(compressedDataUrl);
        };
        
        img.onerror = () => {
          reject(new Error('Failed to load image for compression'));
        };
      });
    }
  }
}
</script>

<style>
/* Base reset for viewport fitting */
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow-y: auto;
}

/* Fix for dark mode */
.dark body, .dark #app {
  background-color: #000000; /* Pure black to ensure no white edges */
}

.app-container {
  min-height: 100vh; /* Fallback */
  min-height: -webkit-fill-available;
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* Mobile viewport height fix for iOS */
@supports (-webkit-touch-callout: none) {
  .app-container {
    height: -webkit-fill-available;
  }
}

/* Responsive fixes for different orientations */
@media screen and (orientation: portrait) {
  .app-container {
    max-height: 100vh;
  }
}

@media screen and (orientation: landscape) and (max-height: 500px) {
  nav {
    max-height: 50px;
    overflow: hidden;
  }
  
  .app-container {
    max-height: 100vh;
  }
}

/* Small devices and landscape fixes */
@media (max-width: 640px) and (orientation: landscape) {
  .app-container {
    min-height: 100%;
  }
  
  nav {
    padding: 0.25rem 0;
  }
}

/* Các hiệu ứng chuyển tiếp */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

/* Glass effect styling */
.glass-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(209, 213, 219, 0.3);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.dark .glass-card {
  background: rgba(17, 25, 40, 0.75);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(71, 85, 105, 0.3);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.2), 0 2px 4px -1px rgba(0, 0, 0, 0.1);
}

.glass-btn {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.glass-btn:hover {
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 8px rgba(31, 38, 135, 0.1);
}

.dark .glass-btn {
  background: rgba(30, 41, 59, 0.6);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.dark .glass-btn:hover {
  background: rgba(30, 41, 59, 0.8);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* Responsive improvements */
@media (max-width: 640px) {
  .neuro-btn {
    padding: 0.5rem 0.75rem;
  }
  
  .glass-card {
    padding: 1rem;
  }
}

/* Better touch targets for mobile */
@media (max-width: 768px) {
  button, a {
    min-height: 44px;
    min-width: 44px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
}

/* Fix for landscape on small devices */
@media (max-height: 500px) and (orientation: landscape) {
  .app-container {
    overflow-y: auto;
  }
}

/* Import any necessary fonts */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
</style> 