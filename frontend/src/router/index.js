import { createRouter, createWebHistory } from 'vue-router';
import Main from '@/pages/main/Main.vue'; // 메인페이지
import List from '@/pages/list/List.vue'; // 조회페이지
import Chat from '@/pages/chat/Chat.vue'; // 말하기페이지
import History from '@/pages/AccountHistory/AccountHistory.vue'; // 계좌 이용내역 조회 페이지
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      // 메인페이지
      path: '/',
      name: 'Main',
      component: Main,
    },
    {
      // 조회페이지
      path: '/list',
      name: 'List',
      component: List,
    },
    {
      // 말하기페이지
      path: '/chat',
      name: 'Chat',
      component: () => import('../pages/chat/Chat.vue'),
    },
    {
      // 조회페이지
      path: '/History',
      name: 'History',
      component: History,
    },
    
  ],
});

export default router;
