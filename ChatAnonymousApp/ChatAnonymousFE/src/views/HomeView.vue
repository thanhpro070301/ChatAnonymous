<template>
  <div class="max-w-6xl mx-auto px-2 sm:px-4 h-screen flex flex-col justify-center">
    <!-- Hero Section -->
    <section class="text-center px-2 sm:px-4">
      <div class="mb-6 sm:mb-8 relative">
        <div class="absolute inset-0 -z-10 bg-gradient-to-r from-primary-100/50 to-secondary-100/50 rounded-3xl blur-xl transform -translate-y-4"></div>
        <h1 class="text-3xl sm:text-4xl md:text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-primary-600 to-secondary-600 mb-3 sm:mb-4 animate-fade-in">Chat ẩn danh với người lạ</h1>
        <p class="text-base sm:text-xl text-gray-600 max-w-3xl mx-auto mb-4 animate-slide-up px-2">Kết nối và trò chuyện ngẫu nhiên với người lạ trên khắp thế giới!</p>
        <p class="text-sm sm:text-base text-gray-500 max-w-2xl mx-auto mb-8 sm:mb-12 animate-slide-up delay-100 px-2">Nền tảng an toàn, bảo mật giúp bạn thoải mái chia sẻ mà không cần lộ danh tính.</p>
      </div>
      
      <!-- CTA Buttons with animation -->
      <div class="flex flex-wrap justify-center gap-3 sm:gap-4 mb-10 sm:mb-16 animate-fade-in delay-200">
        <router-link to="/chat" class="bg-gradient-to-r from-secondary-500 to-primary-500 text-white py-2.5 sm:py-3 px-6 sm:px-8 rounded-full shadow-lg hover:shadow-xl transition-all duration-300 flex items-center gap-2 transform hover:translate-y-[-2px] text-sm sm:text-base">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 sm:h-5 sm:w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
          </svg>
          Bắt đầu trò chuyện
        </router-link>
        <router-link to="/about" class="glass-btn border border-gray-200 py-2.5 sm:py-3 px-6 sm:px-8 rounded-full transition-all duration-300 flex items-center gap-2 transform hover:translate-y-[-2px] text-sm sm:text-base">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 sm:h-5 sm:w-5 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          Tìm hiểu thêm
        </router-link>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
}
</script>

<style scoped>
@media (max-height: 600px) and (orientation: landscape) {
  section {
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
  }
  
  .glass-card {
    padding: 0.75rem !important;
  }
}
</style> 