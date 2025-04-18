<template>
  <div class="max-w-6xl mx-auto px-2 sm:px-4">
    <!-- Hero Section -->
    <section class="text-center py-8 sm:py-16 px-2 sm:px-4">
      <div class="mb-6 sm:mb-8 relative">
        <div class="absolute inset-0 -z-10 bg-gradient-to-r from-primary-100/50 to-secondary-100/50 rounded-3xl blur-xl transform -translate-y-4"></div>
        <h1 class="text-3xl sm:text-4xl md:text-5xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-primary-600 to-secondary-600 mb-3 sm:mb-4 animate-fade-in">Chat ẩn danh với người lạ</h1>
        <p class="text-base sm:text-xl text-gray-600 max-w-3xl mx-auto mb-8 sm:mb-12 animate-slide-up px-2">Kết nối và trò chuyện ngẫu nhiên với người lạ trên khắp thế giới!</p>
      </div>
      
      <!-- Stats -->
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 sm:gap-6 max-w-4xl mx-auto mb-8 sm:mb-16 px-2 sm:px-4">
        <div class="glass-card rounded-2xl p-4 sm:p-6 flex flex-col items-center transform hover:scale-105 transition-all duration-300">
          <div class="p-3 bg-gradient-to-r from-blue-100 to-blue-200 text-blue-600 rounded-full mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-7 sm:w-7" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2M9 7a4 4 0 1 0 0 8 4 4 0 0 0 0-8zM23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" />
            </svg>
          </div>
          <div class="text-2xl sm:text-3xl font-bold text-gray-800 mb-1">{{ stats.onlineCount || 0 }}</div>
          <div class="text-xs sm:text-sm text-gray-500">Người trực tuyến</div>
        </div>
        
        <div class="glass-card rounded-2xl p-4 sm:p-6 flex flex-col items-center transform hover:scale-105 transition-all duration-300">
          <div class="p-3 bg-gradient-to-r from-green-100 to-green-200 text-green-600 rounded-full mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-7 sm:w-7" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
            </svg>
          </div>
          <div class="text-2xl sm:text-3xl font-bold text-gray-800 mb-1">{{ stats.activePairs || 0 }}</div>
          <div class="text-xs sm:text-sm text-gray-500">Cuộc trò chuyện</div>
        </div>
        
        <div v-if="advancedStats.totalConnections" class="glass-card rounded-2xl p-4 sm:p-6 flex flex-col items-center transform hover:scale-105 transition-all duration-300">
          <div class="p-3 bg-gradient-to-r from-purple-100 to-purple-200 text-purple-600 rounded-full mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-7 sm:w-7" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <div class="text-2xl sm:text-3xl font-bold text-gray-800 mb-1">{{ advancedStats.totalConnections || 0 }}</div>
          <div class="text-xs sm:text-sm text-gray-500">Tổng kết nối</div>
        </div>
      </div>
      
      <!-- CTA Buttons -->
      <div class="flex flex-wrap justify-center gap-3 sm:gap-4 mb-10 sm:mb-16">
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
    
    <!-- Features Section -->
    <section class="py-10 sm:py-16 px-2 sm:px-4 bg-gradient-to-t from-white to-secondary-50 rounded-xl sm:rounded-3xl my-6 sm:my-8">
      <h2 class="text-2xl sm:text-3xl font-bold text-gray-800 text-center mb-8 sm:mb-12">Tính năng nổi bật</h2>
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 sm:gap-8 max-w-6xl mx-auto">
        <div class="glass-card rounded-xl sm:rounded-2xl p-6 sm:p-8 flex flex-col items-center text-center transform hover:scale-105 transition-all duration-300">
          <div class="p-3 sm:p-4 bg-gradient-to-r from-blue-100 to-blue-200 text-blue-600 rounded-full mb-4 sm:mb-6">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-8 sm:w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          </div>
          <h3 class="text-lg sm:text-xl font-semibold mb-2 sm:mb-3 text-gray-800">Kết nối ngay lập tức</h3>
          <p class="text-sm sm:text-base text-gray-600">Trò chuyện với người lạ chỉ trong vài giây, không cần đăng ký hay cung cấp thông tin cá nhân.</p>
        </div>
        
        <div class="glass-card rounded-xl sm:rounded-2xl p-6 sm:p-8 flex flex-col items-center text-center transform hover:scale-105 transition-all duration-300">
          <div class="p-3 sm:p-4 bg-gradient-to-r from-green-100 to-green-200 text-green-600 rounded-full mb-4 sm:mb-6">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-8 sm:w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
          </div>
          <h3 class="text-lg sm:text-xl font-semibold mb-2 sm:mb-3 text-gray-800">Hoàn toàn ẩn danh</h3>
          <p class="text-sm sm:text-base text-gray-600">Không lưu trữ lịch sử chat, không ghi lại thông tin cá nhân, giữ cho cuộc trò chuyện của bạn riêng tư.</p>
        </div>
        
        <div class="glass-card rounded-xl sm:rounded-2xl p-6 sm:p-8 flex flex-col items-center text-center transform hover:scale-105 transition-all duration-300">
          <div class="p-3 sm:p-4 bg-gradient-to-r from-purple-100 to-purple-200 text-purple-600 rounded-full mb-4 sm:mb-6">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-8 sm:w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h3 class="text-lg sm:text-xl font-semibold mb-2 sm:mb-3 text-gray-800">Đa tính năng</h3>
          <p class="text-sm sm:text-base text-gray-600">Chia sẻ văn bản, hình ảnh và kết nối với nhiều người khác nhau trong cộng đồng trò chuyện ẩn danh.</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
  data() {
    return {
      stats: {
        onlineCount: 0,
        activePairs: 0
      },
      advancedStats: {
        totalConnections: 0,
        totalMessages: 0
      },
      loading: true
    }
  },
  methods: {
    async fetchBasicStats() {
      try {
        const response = await fetch('/api/stats')
        if (response.ok) {
          const data = await response.json()
          this.stats = data
        }
      } catch (error) {
        console.error('Không thể tải thống kê cơ bản:', error)
      }
    },
    async fetchAdvancedStats() {
      try {
        const response = await fetch('/api/stats/advanced')
        if (response.ok) {
          const data = await response.json()
          this.advancedStats = data
        }
      } catch (error) {
        console.error('Không thể tải thống kê nâng cao:', error)
      } finally {
        this.loading = false
      }
    }
  },
  mounted() {
    this.fetchBasicStats()
    this.fetchAdvancedStats()
  }
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