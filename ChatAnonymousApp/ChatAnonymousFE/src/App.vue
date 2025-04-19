<template>
  <div class="app-container min-h-screen bg-white dark:bg-gray-900 transition-colors duration-300">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  setup() {
    const darkModePreference = localStorage.getItem('darkMode') === 'true';
    if (darkModePreference) {
      document.documentElement.classList.add('dark');
    }
  }
}
</script>

<style>
@import './assets/tailwind.css';

/* Global styles */
html, body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
  overflow: hidden;
  margin: 0;
  padding: 0;
}

.app-container {
  height: 100vh;
  height: -webkit-fill-available; /* For iOS Safari */
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

/* Gradient button styling */
.btn-gradient {
  background: linear-gradient(135deg, var(--tw-gradient-stops));
  --tw-gradient-from: #6366f1;
  --tw-gradient-to: #8b5cf6;
  --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to);
  color: white;
  transition: all 0.3s ease;
}

.btn-gradient:hover {
  --tw-gradient-from: #4f46e5;
  --tw-gradient-to: #7c3aed;
  --tw-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow);
}

/* Hide the default button with ID 'tim-nguoi-moi' */
#tim-nguoi-moi {
  display: none;
}

/* Centered cancel search message container */
.cancel-search-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 2rem;
  text-align: center;
  background: linear-gradient(135deg, rgba(249, 250, 251, 0.9), rgba(243, 244, 246, 0.9));
}

.dark .cancel-search-container {
  background: linear-gradient(135deg, rgba(17, 24, 39, 0.9), rgba(31, 41, 55, 0.9));
}

/* Glass card styling */
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

/* Image and video lightbox styles */
.media-lightbox {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.85);
  backdrop-filter: blur(10px);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.media-lightbox-content {
  position: relative;
  max-width: 90%;
  max-height: 90%;
  transform: scale(0.95);
  opacity: 0;
  transition: all 0.4s cubic-bezier(0.2, 0, 0.2, 1);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
}

.media-lightbox.active .media-lightbox-content {
  transform: scale(1);
  opacity: 1;
}

.media-lightbox-content img,
.media-lightbox-content video {
  max-width: 100%;
  max-height: 90vh;
  border-radius: 8px;
  object-fit: contain;
}

.media-lightbox-close {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  z-index: 10000;
}

.media-lightbox-close:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.media-lightbox-close svg {
  width: 24px;
  height: 24px;
  color: white;
}

.media-lightbox-countdown {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.2);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.media-countdown-pulse {
  animation: countdown-pulse 1s infinite;
}

@keyframes countdown-pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

/* Fix for landscape on small devices */
@media (max-height: 500px) and (orientation: landscape) {
  .app-container {
    overflow-y: auto;
  }
}

/* Remove the fixed bottom button */
.fixed-bottom-button {
  display: none !important;
}

/* Make sure to hide the button with text "Tìm người mới" */
a[id="tim-nguoi-moi"],
button[id="tim-nguoi-moi"],
.tim-nguoi-moi,
.fixed-bottom-tim-nguoi-moi {
  display: none !important;
}

/* Target the specific fixed bottom button shown in the interface */
[class*="btn-"].fixed {
  display: none !important;
}

/* Import any necessary fonts */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
</style> 