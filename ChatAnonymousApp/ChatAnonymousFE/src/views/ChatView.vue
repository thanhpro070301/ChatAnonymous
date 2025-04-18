<template>
  <div class="h-[calc(100vh-4rem)] p-0 flex flex-col">
    <div class="flex flex-1 h-full">
      <div class="flex-1 overflow-hidden">
        <transition name="fade" mode="out-in">
          <div class="h-full overflow-hidden bg-[#f0f2f5] dark:bg-black rounded-xl shadow-md dark:shadow-none">
            <ChatContainer />
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import ChatContainer from '@/components/ChatContainer.vue';

export default {
  name: 'ChatView',
  components: {
    ChatContainer
  }
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: scale(0.95);
}

@media (max-width: 640px) {
  .h-\[calc\(100vh-4rem\)\] {
    height: calc(100vh - 4rem);
  }
}

/* Ensure the component stretches to full available height */
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

/* Dark mode specific fixes */
:deep(.dark) .rounded-xl {
  background-color: #000000 !important;
  border: none !important;
  outline: none !important;
}
</style> 