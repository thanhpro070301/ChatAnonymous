<template>
  <div class="stats-view bg-gray-50 px-2 sm:px-4 py-4 sm:py-6">
    <div class="max-w-7xl mx-auto">
      <!-- Header -->
      <div class="text-center mb-4 sm:mb-8">
        <h1 class="text-2xl sm:text-3xl md:text-4xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-primary-600 to-secondary-600 animate-fade-in">
          Thống kê hệ thống
        </h1>
        <p class="text-sm sm:text-base text-gray-600 mt-2 animate-fade-in animate-delay-100">
          Dữ liệu thời gian thực về hoạt động của hệ thống Chat Anonymous
        </p>
      </div>

      <!-- Loading state -->
      <div v-if="loading" class="flex flex-col items-center justify-center py-8 sm:py-16">
        <div class="w-14 h-14 sm:w-20 sm:h-20 border-4 border-primary-200 border-t-primary-500 rounded-full animate-spin mb-4"></div>
        <p class="text-gray-600 text-sm sm:text-base">Đang tải dữ liệu thống kê...</p>
      </div>

      <!-- Error state -->
      <div v-else-if="error" class="bg-red-50 text-red-700 p-4 sm:p-6 rounded-xl sm:rounded-2xl shadow text-center mb-6">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mx-auto mb-3 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <h3 class="text-lg sm:text-xl font-semibold mb-2">Không thể tải dữ liệu</h3>
        <p class="text-sm sm:text-base">{{ error }}</p>
        <button 
          @click="fetchStats" 
          class="mt-4 px-4 py-2 bg-red-100 hover:bg-red-200 text-red-700 rounded-lg transition-colors duration-300 text-sm sm:text-base"
        >
          Thử lại
        </button>
      </div>

      <!-- Stats Content -->
      <div v-else class="space-y-4 sm:space-y-8">
        <!-- Overall Stats Cards -->
        <div class="mb-4 sm:mb-8">
          <h2 class="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4">Tổng quan</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-3 sm:gap-4">
            <!-- Active Users Card -->
            <div class="glass-card p-3 sm:p-5 rounded-xl">
              <div class="flex items-center">
                <div class="p-2 sm:p-3 bg-blue-100 rounded-lg mr-3">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 sm:h-7 sm:w-7 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                  </svg>
                </div>
                <div>
                  <h3 class="text-xs sm:text-sm text-gray-500 whitespace-nowrap">Người dùng trực tuyến</h3>
                  <p class="text-xl sm:text-2xl font-bold text-blue-600">{{ stats.activeUsers }}</p>
                </div>
              </div>
            </div>
            
            <!-- Active Chats Card -->
            <div class="glass-card p-3 sm:p-5 rounded-xl">
              <div class="flex items-center">
                <div class="p-2 sm:p-3 bg-green-100 rounded-lg mr-3">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 sm:h-7 sm:w-7 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z" />
                  </svg>
                </div>
                <div>
                  <h3 class="text-xs sm:text-sm text-gray-500 whitespace-nowrap">Cuộc trò chuyện đang diễn ra</h3>
                  <p class="text-xl sm:text-2xl font-bold text-green-600">{{ stats.activeChats }}</p>
                </div>
              </div>
            </div>
            
            <!-- Messages Today Card -->
            <div class="glass-card p-3 sm:p-5 rounded-xl">
              <div class="flex items-center">
                <div class="p-2 sm:p-3 bg-purple-100 rounded-lg mr-3">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 sm:h-7 sm:w-7 text-purple-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                  </svg>
                </div>
                <div>
                  <h3 class="text-xs sm:text-sm text-gray-500 whitespace-nowrap">Tin nhắn hôm nay</h3>
                  <p class="text-xl sm:text-2xl font-bold text-purple-600">{{ stats.messagesToday.toLocaleString() }}</p>
                </div>
              </div>
            </div>
            
            <!-- Server Uptime Card -->
            <div class="glass-card p-3 sm:p-5 rounded-xl">
              <div class="flex items-center">
                <div class="p-2 sm:p-3 bg-amber-100 rounded-lg mr-3">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 sm:h-7 sm:w-7 text-amber-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h14M5 12a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v4a2 2 0 01-2 2M5 12a2 2 0 00-2 2v4a2 2 0 002 2h14a2 2 0 002-2v-4a2 2 0 00-2-2m-2-4h.01M17 16h.01" />
                  </svg>
                </div>
                <div>
                  <h3 class="text-xs sm:text-sm text-gray-500 whitespace-nowrap">Thời gian hoạt động</h3>
                  <p class="text-xl sm:text-2xl font-bold text-amber-600">{{ formatUptime(stats.serverUptime) }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Hourly Activity Chart -->
        <div class="glass-card p-3 sm:p-6 rounded-xl mb-4 sm:mb-8">
          <h2 class="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-5">Hoạt động theo giờ</h2>
          <div class="h-48 sm:h-64 md:h-80">
            <canvas ref="hourlyChart"></canvas>
          </div>
        </div>
        
        <!-- Two Column Layout for remaining stats -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 sm:gap-8">
          <!-- User Agents -->
          <div class="glass-card p-3 sm:p-6 rounded-xl">
            <h2 class="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4">Trình duyệt người dùng</h2>
            <div class="h-52 sm:h-64">
              <canvas ref="userAgentChart"></canvas>
            </div>
          </div>
          
          <!-- System Health -->
          <div class="glass-card p-3 sm:p-6 rounded-xl">
            <h2 class="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4">Sức khỏe hệ thống</h2>
            <div class="space-y-3 sm:space-y-4">
              <!-- CPU Usage -->
              <div>
                <div class="flex justify-between items-center mb-1">
                  <span class="text-xs sm:text-sm text-gray-600">CPU: {{ stats.systemHealth.cpu }}%</span>
                  <span class="text-xs sm:text-sm" :class="getCPUHealthClass">{{ getCPUHealthStatus }}</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2 sm:h-2.5">
                  <div class="h-2 sm:h-2.5 rounded-full" 
                      :class="getCPUBarClass" 
                      :style="{ width: `${stats.systemHealth.cpu}%` }"></div>
                </div>
              </div>
              
              <!-- Memory Usage -->
              <div>
                <div class="flex justify-between items-center mb-1">
                  <span class="text-xs sm:text-sm text-gray-600">Bộ nhớ: {{ stats.systemHealth.memory }}%</span>
                  <span class="text-xs sm:text-sm" :class="getMemoryHealthClass">{{ getMemoryHealthStatus }}</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2 sm:h-2.5">
                  <div class="h-2 sm:h-2.5 rounded-full" 
                      :class="getMemoryBarClass" 
                      :style="{ width: `${stats.systemHealth.memory}%` }"></div>
                </div>
              </div>
              
              <!-- Disk Usage -->
              <div>
                <div class="flex justify-between items-center mb-1">
                  <span class="text-xs sm:text-sm text-gray-600">Ổ đĩa: {{ stats.systemHealth.disk }}%</span>
                  <span class="text-xs sm:text-sm" :class="getDiskHealthClass">{{ getDiskHealthStatus }}</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2 sm:h-2.5">
                  <div class="h-2 sm:h-2.5 rounded-full" 
                      :class="getDiskBarClass" 
                      :style="{ width: `${stats.systemHealth.disk}%` }"></div>
                </div>
              </div>
              
              <!-- Network Status -->
              <div class="mt-4 flex items-center justify-between">
                <div class="flex items-center text-xs sm:text-sm text-gray-600">
                  <div class="w-2 h-2 sm:w-3 sm:h-3 rounded-full mr-2" 
                      :class="{'bg-green-500': stats.systemHealth.networkStatus === 'good', 
                              'bg-yellow-500': stats.systemHealth.networkStatus === 'degraded',
                              'bg-red-500': stats.systemHealth.networkStatus === 'poor'}"></div>
                  <span>Trạng thái mạng:</span>
                </div>
                <span class="text-xs sm:text-sm" :class="getNetworkHealthClass">{{ getNetworkHealthText }}</span>
              </div>
            </div>
          </div>
          
          <!-- Country Distribution -->
          <div class="glass-card p-3 sm:p-6 rounded-xl">
            <h2 class="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4">Phân bố theo quốc gia</h2>
            <div class="h-52 sm:h-64">
              <canvas ref="countryChart"></canvas>
            </div>
          </div>
          
          <!-- Latest Stats -->
          <div class="glass-card p-3 sm:p-6 rounded-xl">
            <h2 class="text-lg sm:text-xl font-semibold text-gray-800 mb-3 sm:mb-4">Thông số gần đây</h2>
            <ul class="space-y-2 sm:space-y-3">
              <li v-for="(stat, index) in stats.latestActivity" :key="index" class="flex items-center text-sm border-b border-gray-100 pb-2">
                <div :class="`w-2 h-2 sm:w-3 sm:h-3 rounded-full mr-2 ${getActivityColor(stat.type)}`"></div>
                <div class="flex-1 text-xs sm:text-sm">
                  <span class="text-gray-800">{{ stat.description }}</span>
                  <span class="text-gray-500 text-xs sm:text-xs"> - {{ formatTime(stat.timestamp) }}</span>
                </div>
              </li>
            </ul>
            <div class="text-center mt-3 sm:mt-4">
              <button class="text-xs sm:text-sm text-primary-600 hover:text-primary-800 font-medium">
                Xem thêm thống kê
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Last updated info at the bottom -->
      <div v-if="stats" class="text-center text-xs sm:text-sm text-gray-500 mt-6 sm:mt-8">
        Cập nhật lần cuối: {{ formatLastUpdated(stats.lastUpdated) }}
      </div>
    </div>
  </div>
</template>

<script>
import Chart from 'chart.js/auto'

export default {
  name: 'StatsView',
  data() {
    return {
      loading: true,
      error: null,
      stats: null,
      charts: {
        hourly: null,
        userAgent: null,
        country: null
      }
    }
  },
  computed: {
    getCPUHealthClass() {
      const cpu = this.stats?.systemHealth.cpu || 0
      if (cpu < 60) return 'text-green-600'
      if (cpu < 85) return 'text-yellow-600'
      return 'text-red-600'
    },
    getCPUHealthStatus() {
      const cpu = this.stats?.systemHealth.cpu || 0
      if (cpu < 60) return 'Tốt'
      if (cpu < 85) return 'Bình thường'
      return 'Cao'
    },
    getCPUBarClass() {
      const cpu = this.stats?.systemHealth.cpu || 0
      if (cpu < 60) return 'bg-green-500'
      if (cpu < 85) return 'bg-yellow-500'
      return 'bg-red-500'
    },
    getMemoryHealthClass() {
      const memory = this.stats?.systemHealth.memory || 0
      if (memory < 60) return 'text-green-600'
      if (memory < 85) return 'text-yellow-600'
      return 'text-red-600'
    },
    getMemoryHealthStatus() {
      const memory = this.stats?.systemHealth.memory || 0
      if (memory < 60) return 'Tốt'
      if (memory < 85) return 'Bình thường'
      return 'Cao'
    },
    getMemoryBarClass() {
      const memory = this.stats?.systemHealth.memory || 0
      if (memory < 60) return 'bg-green-500'
      if (memory < 85) return 'bg-yellow-500'
      return 'bg-red-500'
    },
    getDiskHealthClass() {
      const disk = this.stats?.systemHealth.disk || 0
      if (disk < 60) return 'text-green-600'
      if (disk < 85) return 'text-yellow-600'
      return 'text-red-600'
    },
    getDiskHealthStatus() {
      const disk = this.stats?.systemHealth.disk || 0
      if (disk < 60) return 'Tốt'
      if (disk < 85) return 'Bình thường'
      return 'Cao'
    },
    getDiskBarClass() {
      const disk = this.stats?.systemHealth.disk || 0
      if (disk < 60) return 'bg-green-500'
      if (disk < 85) return 'bg-yellow-500'
      return 'bg-red-500'
    },
    getNetworkHealthClass() {
      const status = this.stats?.systemHealth.networkStatus || 'poor'
      if (status === 'good') return 'text-green-600'
      if (status === 'degraded') return 'text-yellow-600'
      return 'text-red-600'
    },
    getNetworkHealthText() {
      const status = this.stats?.systemHealth.networkStatus || 'poor'
      if (status === 'good') return 'Tốt'
      if (status === 'degraded') return 'Bị suy giảm'
      return 'Kém'
    }
  },
  methods: {
    async fetchStats() {
      this.loading = true
      this.error = null
      
      try {
        // Simulate API call with a delay
        await new Promise(resolve => setTimeout(resolve, 1500))
        
        // Mock data for demonstration
        this.stats = {
          activeUsers: 1245,
          activeChats: 586,
          messagesToday: 28735,
          serverUptime: 1209600, // 14 days in seconds
          hourlyActivity: [
            10, 12, 15, 18, 22, 30, 45, 78, 125, 132, 145, 148, 
            150, 135, 124, 114, 120, 130, 138, 142, 120, 98, 65, 35
          ],
          userAgents: {
            'Chrome': 64,
            'Firefox': 15,
            'Safari': 12,
            'Edge': 7,
            'Other': 2
          },
          countries: {
            'Vietnam': 45,
            'United States': 18,
            'India': 12,
            'Germany': 6,
            'United Kingdom': 5,
            'Other': 14
          },
          systemHealth: {
            cpu: 42,
            memory: 58,
            disk: 67,
            networkStatus: 'good'
          },
          latestActivity: [
            { type: 'user', description: 'Người dùng mới đăng ký', timestamp: Date.now() - 2 * 60 * 1000 },
            { type: 'chat', description: 'Cuộc trò chuyện dài nhất hôm nay: 45 phút', timestamp: Date.now() - 15 * 60 * 1000 },
            { type: 'system', description: 'Cập nhật bảo mật hệ thống', timestamp: Date.now() - 55 * 60 * 1000 },
            { type: 'message', description: 'Đạt 10,000 tin nhắn trong giờ cao điểm', timestamp: Date.now() - 3 * 60 * 60 * 1000 },
            { type: 'server', description: 'Khởi động lại máy chủ thành công', timestamp: Date.now() - 12 * 60 * 60 * 1000 }
          ],
          lastUpdated: new Date()
        }
      } catch (err) {
        this.error = 'Không thể kết nối với máy chủ. Vui lòng thử lại sau.'
        console.error('Error fetching stats:', err)
      } finally {
        this.loading = false
      }
    },
    formatUptime(seconds) {
      const days = Math.floor(seconds / (3600 * 24))
      const hours = Math.floor((seconds % (3600 * 24)) / 3600)
      
      return `${days}d ${hours}h`
    },
    formatTime(timestamp) {
      const date = new Date(timestamp)
      const now = new Date()
      const diffMinutes = Math.floor((now - date) / (1000 * 60))
      
      if (diffMinutes < 1) return 'vừa xong'
      if (diffMinutes < 60) return `${diffMinutes} phút trước`
      
      const diffHours = Math.floor(diffMinutes / 60)
      if (diffHours < 24) return `${diffHours} giờ trước`
      
      const diffDays = Math.floor(diffHours / 24)
      return `${diffDays} ngày trước`
    },
    formatLastUpdated(date) {
      return new Date(date).toLocaleString('vi-VN', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      })
    },
    getActivityColor(type) {
      switch(type) {
        case 'user': return 'bg-blue-500'
        case 'chat': return 'bg-green-500'
        case 'message': return 'bg-purple-500'
        case 'system': return 'bg-yellow-500'
        case 'server': return 'bg-red-500'
        default: return 'bg-gray-500'
      }
    },
    initCharts() {
      this.$nextTick(() => {
        if (!this.stats) return
        
        // Destroy existing charts to prevent duplicates
        Object.values(this.charts).forEach(chart => {
          if (chart) chart.destroy()
        })
        
        // Hourly Activity Chart
        const hourlyCtx = this.$refs.hourlyChart.getContext('2d')
        this.charts.hourly = new Chart(hourlyCtx, {
          type: 'line',
          data: {
            labels: Array.from({ length: 24 }, (_, i) => `${i}:00`),
            datasets: [{
              label: 'Người dùng hoạt động',
              data: this.stats.hourlyActivity,
              borderColor: '#8b5cf6',
              backgroundColor: 'rgba(139, 92, 246, 0.1)',
              borderWidth: 2,
              tension: 0.4,
              fill: true
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                display: false
              }
            },
            scales: {
              y: {
                beginAtZero: true,
                grid: {
                  color: 'rgba(0, 0, 0, 0.05)'
                },
                ticks: {
                  font: {
                    size: window.innerWidth < 640 ? 8 : 10
                  }
                }
              },
              x: {
                grid: {
                  display: false
                },
                ticks: {
                  font: {
                    size: window.innerWidth < 640 ? 8 : 10
                  },
                  maxRotation: 45,
                  minRotation: 45
                }
              }
            }
          }
        })
        
        // User Agent Chart
        const userAgentCtx = this.$refs.userAgentChart.getContext('2d')
        this.charts.userAgent = new Chart(userAgentCtx, {
          type: 'doughnut',
          data: {
            labels: Object.keys(this.stats.userAgents),
            datasets: [{
              data: Object.values(this.stats.userAgents),
              backgroundColor: [
                '#3b82f6', // blue
                '#ef4444', // red
                '#10b981', // green
                '#8b5cf6', // purple
                '#6b7280'  // gray
              ],
              borderWidth: 0
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                position: 'bottom',
                labels: {
                  font: {
                    size: window.innerWidth < 640 ? 10 : 12
                  },
                  padding: window.innerWidth < 640 ? 8 : 16
                }
              }
            }
          }
        })
        
        // Country Chart
        const countryCtx = this.$refs.countryChart.getContext('2d')
        this.charts.country = new Chart(countryCtx, {
          type: 'pie',
          data: {
            labels: Object.keys(this.stats.countries),
            datasets: [{
              data: Object.values(this.stats.countries),
              backgroundColor: [
                '#10b981', // green
                '#3b82f6', // blue
                '#f59e0b', // amber
                '#ec4899', // pink
                '#6366f1', // indigo
                '#6b7280'  // gray
              ],
              borderWidth: 0
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              legend: {
                position: 'bottom',
                labels: {
                  font: {
                    size: window.innerWidth < 640 ? 10 : 12
                  },
                  padding: window.innerWidth < 640 ? 8 : 16
                }
              }
            }
          }
        })
      })
    }
  },
  mounted() {
    this.fetchStats().then(() => {
      this.initCharts()
    })
    
    // Re-render charts on window resize
    window.addEventListener('resize', this.initCharts)
  },
  beforeUnmount() {
    // Clean up event listener
    window.removeEventListener('resize', this.initCharts)
    
    // Destroy charts
    Object.values(this.charts).forEach(chart => {
      if (chart) chart.destroy()
    })
  }
}
</script>

<style scoped>
.animate-delay-100 {
  animation-delay: 0.1s;
}

.glass-card {
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.7);
}

/* Định nghĩa class cho text cực nhỏ trên thiết bị di động */
.text-2xs {
  font-size: 0.65rem;
  line-height: 0.75rem;
}

/* Đảm bảo scroll hoạt động đúng */
html, body {
  height: 100%;
  overflow-y: auto;
}

/* Đảm bảo tiêu đề không bị xuống dòng */
.text-xs, .text-sm {
  white-space: nowrap;
}

/* Các card thống kê trên thiết bị nhỏ */
@media (max-width: 480px) {
  .glass-card h3 {
    font-size: 0.65rem;
    white-space: nowrap;
  }
}

/* Thêm media queries cho màn hình nhỏ */
@media (max-width: 360px) {
  .glass-card {
    padding: 0.75rem !important;
  }
  
  h1 {
    font-size: 1.25rem !important;
  }
  
  h2 {
    font-size: 1rem !important;
  }
  
  .text-xs, .text-sm {
    font-size: 0.65rem !important;
  }
  
  .text-base {
    font-size: 0.75rem !important;
  }
  
  .text-xl, .text-2xl {
    font-size: 1.1rem !important;
  }
  
  .h-48, .h-52, .h-64 {
    height: 180px !important;
  }
}

/* Responsive cho chế độ landscape */
@media (max-height: 500px) and (orientation: landscape) {
  .stats-view {
    padding: 0.5rem !important;
  }
  
  .glass-card {
    padding: 0.5rem !important;
  }
  
  .space-y-8 > * + * {
    margin-top: 0.5rem !important;
  }
  
  .mb-8 {
    margin-bottom: 0.5rem !important;
  }
  
  .h-48, .h-52, .h-64, .h-80 {
    height: 150px !important;
  }
  
  .grid-cols-1.md\:grid-cols-2 {
    grid-template-columns: repeat(2, minmax(0, 1fr)) !important;
  }
}

/* Điều chỉnh cho iPad và tablet */
@media (min-width: 768px) and (max-width: 1024px) {
  .glass-card {
    padding: 1rem !important;
  }
}
</style> 