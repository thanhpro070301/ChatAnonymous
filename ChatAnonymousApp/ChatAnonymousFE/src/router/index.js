import { createRouter, createWebHistory } from 'vue-router'
import ChatView from '../views/ChatView.vue'

const routes = [
  {
    path: '/',
    name: 'Chat',
    component: ChatView
  },
  {
    path: '/stats',
    name: 'Statistics',
    // Lazy-load the stats view
    component: () => import(/* webpackChunkName: "stats" */ '../views/StatsView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 