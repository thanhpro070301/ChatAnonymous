<template>
  <div class="about-page py-4 sm:py-8 px-2 sm:px-4">
    <div class="max-w-4xl mx-auto">
      <!-- Header section with illustration -->
      <div class="mb-6 sm:mb-12 relative">
        <div class="absolute inset-0 -z-10 bg-gradient-to-r from-primary-100/50 to-secondary-100/50 rounded-3xl blur-xl transform -translate-y-4"></div>
        <div class="glass-card rounded-xl sm:rounded-3xl overflow-hidden">
          <div class="bg-gradient-to-r from-white/80 to-secondary-50/80 p-4 sm:p-8 border-b border-secondary-100/30">
            <div class="flex flex-col md:flex-row items-center">
              <div class="w-full md:w-1/2 mb-4 md:mb-0 md:pr-6 sm:md:pr-8">
                <h1 class="text-2xl sm:text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-primary-600 to-secondary-600 mb-3 sm:mb-4 animate-fade-in">Chat Anonymous</h1>
                <p class="text-sm sm:text-lg text-gray-700 mb-3 sm:mb-4 animate-slide-up">
                  Kết nối với người lạ một cách ẩn danh, chia sẻ suy nghĩ mà không lo lắng về danh tính.
                </p>
                <p class="text-xs sm:text-base text-gray-600 animate-slide-up delay-100">
                  Được phát triển với mục tiêu bảo vệ quyền riêng tư trong khi cung cấp trải nghiệm trò chuyện thoải mái, an toàn.
                </p>
              </div>
              <div class="w-full md:w-1/2 flex justify-center">
                <div class="w-48 h-48 sm:w-64 sm:h-64 glass-card rounded-full flex items-center justify-center p-3 sm:p-4 animate-bounce-light bg-gradient-to-r from-secondary-100 to-primary-100 shadow-lg">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-32 w-32 sm:h-40 sm:w-40 text-transparent" fill="none" viewBox="0 0 24 24" stroke="currentColor" style="stroke: url(#chat-gradient)">
                    <defs>
                      <linearGradient id="chat-gradient" x1="0%" y1="0%" x2="100%" y2="100%">
                        <stop offset="0%" stop-color="#0c91ea" />
                        <stop offset="100%" stop-color="#0fb572" />
                      </linearGradient>
                    </defs>
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                  </svg>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Features section -->
      <div class="my-6 sm:my-12">
        <h2 class="text-xl sm:text-2xl font-bold text-center text-transparent bg-clip-text bg-gradient-to-r from-primary-700 to-secondary-700 mb-6 sm:mb-8">Tính năng nổi bật</h2>
        
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-2 gap-4 sm:gap-6 mb-6 sm:mb-8">
          <div class="glass-card p-4 sm:p-6 rounded-xl sm:rounded-2xl transform hover:scale-105 transition-all duration-300">
            <div class="p-2 sm:p-3 bg-gradient-to-r from-blue-100 to-blue-200 text-blue-600 rounded-full w-10 h-10 sm:w-14 sm:h-14 flex items-center justify-center mb-3 sm:mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-8 sm:w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>
            <h3 class="text-base sm:text-lg font-semibold text-transparent bg-clip-text bg-gradient-to-r from-blue-700 to-blue-500 mb-1 sm:mb-2">Hoàn toàn ẩn danh</h3>
            <p class="text-xs sm:text-base text-gray-600">
              Không yêu cầu đăng ký tài khoản, không lưu trữ thông tin cá nhân. Bạn hoàn toàn là một người lạ.
            </p>
          </div>
          
          <div class="glass-card p-4 sm:p-6 rounded-xl sm:rounded-2xl transform hover:scale-105 transition-all duration-300">
            <div class="p-2 sm:p-3 bg-gradient-to-r from-green-100 to-green-200 text-green-600 rounded-full w-10 h-10 sm:w-14 sm:h-14 flex items-center justify-center mb-3 sm:mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-8 sm:w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
            </div>
            <h3 class="text-base sm:text-lg font-semibold text-transparent bg-clip-text bg-gradient-to-r from-green-700 to-green-500 mb-1 sm:mb-2">Kết nối tức thì</h3>
            <p class="text-xs sm:text-base text-gray-600">
              Tự động kết nối với người lạ ngẫu nhiên, không cần chờ đợi lâu. Nếu cuộc trò chuyện không phù hợp, dễ dàng tìm người mới.
            </p>
          </div>
          
          <div class="glass-card p-4 sm:p-6 rounded-xl sm:rounded-2xl transform hover:scale-105 transition-all duration-300">
            <div class="p-2 sm:p-3 bg-gradient-to-r from-purple-100 to-purple-200 text-purple-600 rounded-full w-10 h-10 sm:w-14 sm:h-14 flex items-center justify-center mb-3 sm:mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-8 sm:w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 11.5V14m0-2.5v-6a2.5 2.5 0 015 0v6a2.5 2.5 0 01-5 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.75 10.5a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm2.25 1.5a.75.75 0 100-1.5.75.75 0 000 1.5zM17.25 14.25a.75.75 0 10-1.5 0 .75.75 0 001.5 0z" />
              </svg>
            </div>
            <h3 class="text-base sm:text-lg font-semibold text-transparent bg-clip-text bg-gradient-to-r from-purple-700 to-purple-500 mb-1 sm:mb-2">Giao diện thân thiện</h3>
            <p class="text-xs sm:text-base text-gray-600">
              Thiết kế hiện đại, dễ sử dụng trên mọi thiết bị. Trải nghiệm mượt mà và thân thiện với người dùng.
            </p>
          </div>
          
          <div class="glass-card p-4 sm:p-6 rounded-xl sm:rounded-2xl transform hover:scale-105 transition-all duration-300">
            <div class="p-2 sm:p-3 bg-gradient-to-r from-red-100 to-red-200 text-red-600 rounded-full w-10 h-10 sm:w-14 sm:h-14 flex items-center justify-center mb-3 sm:mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 sm:h-8 sm:w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
              </svg>
            </div>
            <h3 class="text-base sm:text-lg font-semibold text-transparent bg-clip-text bg-gradient-to-r from-red-700 to-red-500 mb-1 sm:mb-2">Bảo mật và an toàn</h3>
            <p class="text-xs sm:text-base text-gray-600">
              Tin nhắn không được lưu trữ lâu dài, không theo dõi hoạt động người dùng và có các biện pháp bảo vệ chống lại nội dung độc hại.
            </p>
          </div>
        </div>
      </div>
      
      <!-- How it works section -->
      <div class="my-6 sm:my-12">
        <div class="glass-card p-4 sm:p-8 rounded-xl sm:rounded-2xl">
          <h2 class="text-xl sm:text-2xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-secondary-700 to-secondary-500 mb-4 sm:mb-6">Cách sử dụng</h2>
          
          <div class="space-y-4 sm:space-y-6 mb-4 sm:mb-8">
            <div class="flex items-start">
              <div class="flex-shrink-0 bg-gradient-to-r from-secondary-100 to-secondary-200 rounded-full p-1.5 sm:p-2 mr-3 sm:mr-4">
                <span class="w-5 h-5 sm:w-6 sm:h-6 flex items-center justify-center text-secondary-600 font-semibold text-sm sm:text-base">1</span>
              </div>
              <div>
                <h3 class="text-base sm:text-lg font-medium text-transparent bg-clip-text bg-gradient-to-r from-secondary-700 to-secondary-500 mb-1">Bắt đầu trò chuyện</h3>
                <p class="text-xs sm:text-base text-gray-600">Nhấn nút "Bắt đầu trò chuyện" để kết nối với máy chủ và tự động được đưa vào hàng đợi chờ ghép cặp.</p>
              </div>
            </div>
            
            <div class="flex items-start">
              <div class="flex-shrink-0 bg-gradient-to-r from-secondary-100 to-secondary-200 rounded-full p-1.5 sm:p-2 mr-3 sm:mr-4">
                <span class="w-5 h-5 sm:w-6 sm:h-6 flex items-center justify-center text-secondary-600 font-semibold text-sm sm:text-base">2</span>
              </div>
              <div>
                <h3 class="text-base sm:text-lg font-medium text-transparent bg-clip-text bg-gradient-to-r from-secondary-700 to-secondary-500 mb-1">Trò chuyện</h3>
                <p class="text-xs sm:text-base text-gray-600">Khi được ghép cặp với một người lạ, bạn có thể bắt đầu trò chuyện ngay lập tức. Hãy lịch sự và tôn trọng người khác.</p>
              </div>
            </div>
            
            <div class="flex items-start">
              <div class="flex-shrink-0 bg-gradient-to-r from-secondary-100 to-secondary-200 rounded-full p-1.5 sm:p-2 mr-3 sm:mr-4">
                <span class="w-5 h-5 sm:w-6 sm:h-6 flex items-center justify-center text-secondary-600 font-semibold text-sm sm:text-base">3</span>
              </div>
              <div>
                <h3 class="text-base sm:text-lg font-medium text-transparent bg-clip-text bg-gradient-to-r from-secondary-700 to-secondary-500 mb-1">Tìm người mới</h3>
                <p class="text-xs sm:text-base text-gray-600">Không hợp với người đang trò chuyện? Dễ dàng kết thúc cuộc trò chuyện hiện tại và tìm người mới bằng nút "Tìm người mới".</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- FAQ section -->
      <div class="my-6 sm:my-12">
        <h2 class="text-xl sm:text-2xl font-bold text-center text-transparent bg-clip-text bg-gradient-to-r from-secondary-700 to-primary-700 mb-4 sm:mb-8">Câu hỏi thường gặp</h2>
        
        <div class="space-y-3 sm:space-y-4">
          <div class="glass-card p-4 sm:p-6 rounded-xl mb-3 sm:mb-4 transform hover:shadow-md transition-all duration-300">
            <h3 class="text-base sm:text-lg font-medium text-transparent bg-clip-text bg-gradient-to-r from-blue-700 to-blue-500 mb-1 sm:mb-2">Dữ liệu của tôi có được lưu trữ không?</h3>
            <p class="text-xs sm:text-base text-gray-600">Không, chúng tôi không lưu trữ nội dung tin nhắn hoặc thông tin cá nhân. Tin nhắn chỉ được giữ trong bộ nhớ tạm thời và sẽ biến mất khi bạn kết thúc trò chuyện.</p>
          </div>
          
          <div class="glass-card p-4 sm:p-6 rounded-xl mb-3 sm:mb-4 transform hover:shadow-md transition-all duration-300">
            <h3 class="text-base sm:text-lg font-medium text-transparent bg-clip-text bg-gradient-to-r from-green-700 to-green-500 mb-1 sm:mb-2">Làm sao để báo cáo người dùng không phù hợp?</h3>
            <p class="text-xs sm:text-base text-gray-600">Hiện tại, bạn có thể kết thúc cuộc trò chuyện bất kỳ lúc nào nếu cảm thấy không thoải mái. Chúng tôi đang phát triển hệ thống báo cáo người dùng trong phiên bản tiếp theo.</p>
          </div>
          
          <div class="glass-card p-4 sm:p-6 rounded-xl transform hover:shadow-md transition-all duration-300">
            <h3 class="text-base sm:text-lg font-medium text-transparent bg-clip-text bg-gradient-to-r from-purple-700 to-purple-500 mb-1 sm:mb-2">Chat Anonymous có an toàn không?</h3>
            <p class="text-xs sm:text-base text-gray-600">Chat Anonymous được thiết kế với ưu tiên hàng đầu về quyền riêng tư và an toàn. Tuy nhiên, hãy luôn cẩn thận khi chia sẻ thông tin với người lạ và không bao giờ tiết lộ thông tin cá nhân của bạn.</p>
          </div>
        </div>
      </div>
      
      <!-- Footer section -->
      <div class="text-center mt-8 sm:mt-16 glass-card p-4 sm:p-6 rounded-xl">
        <div class="bg-gradient-to-r from-secondary-500 to-primary-500 text-transparent bg-clip-text">
          <p class="text-base sm:text-lg font-medium">Bản quyền © {{ new Date().getFullYear() }} Chat Anonymous</p>
          <p class="mt-1 text-sm sm:text-base">Phát triển bởi ThanhPro0703 và Đội ngũ</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AboutView'
}
</script>

<style scoped>
.delay-100 {
  animation-delay: 0.1s;
}

/* Đảm bảo scroll hoạt động đúng */
html, body {
  height: 100%;
  overflow-y: auto !important;
}

.about-page {
  overflow-y: auto;
}

@media (min-width: 768px) {
  .about-page {
    padding: 3rem 2rem;
  }
}

/* Fix cho màn hình nhỏ */
@media (max-width: 360px) {
  h1, h2, h3 {
    font-size: 95%;
  }
  
  .glass-card {
    padding: 0.75rem !important;
  }
}

/* Responsive cho chế độ landscape */
@media (max-height: 500px) and (orientation: landscape) {
  .about-page {
    padding: 0.5rem;
  }
  
  .glass-card {
    padding: 0.5rem !important;
  }
  
  .my-6, .my-12 {
    margin-top: 0.5rem !important;
    margin-bottom: 0.5rem !important;
  }
  
  .mb-6, .mb-12 {
    margin-bottom: 0.5rem !important;
  }
  
  .space-y-6 > * + *, .space-y-4 > * + * {
    margin-top: 0.5rem !important;
  }
}

/* Điều chỉnh cho iPad và tablet */
@media (min-width: 768px) and (max-width: 1024px) and (orientation: portrait) {
  .glass-card {
    padding: 1.5rem !important;
  }
}

/* Tối ưu hiệu ứng bounce trên thiết bị di động */
@media (max-width: 640px) {
  .animate-bounce-light {
    animation-duration: 3s;
  }
}
</style> 