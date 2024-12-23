<template>
  <div>
    <p>로그인 처리 중…</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth.js';

const router = useRouter();
const authStore = useAuthStore();

onMounted(async () => {
  authStore.loginWithKakao();
});
</script>
