<template>
  <div class="h-screen p-0 flex flex-col overflow-hidden bg-white dark:bg-black">
    <div class="flex flex-1 h-full w-full">
      <div class="flex-1 overflow-hidden w-full">
        <transition name="fade" mode="out-in">
          <div class="h-full w-full overflow-hidden bg-white dark:bg-black">
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

/* Ensure the component stretches to full available height */
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

/* Hide any fixed bottom button elements */
:deep(.fixed-bottom) {
  display: none !important;
}
</style> 