import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import './assets/main.css'
import './assets/index.css'

// Các component trang chính
import HomeView from './views/HomeView.vue'
import ChatView from './views/ChatView.vue'
import AboutView from './views/AboutView.vue'

const routes = [
  { path: '/', component: HomeView },
  { path: '/chat', component: ChatView },
  { path: '/about', component: AboutView },
  { 
    path: '/stats', 
    name: 'Stats',
    component: () => import('./views/StatsView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const app = createApp(App)
app.use(router)
app.mount('#app') 