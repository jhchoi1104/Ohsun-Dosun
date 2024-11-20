import { createRouter, createWebHistory } from 'vue-router';
import Main from '@/pages/main/Main.vue'; // 메인페이지
import List from '@/pages/list/List.vue'; // 조회페이지
import Chat from '@/pages/chat/Chat.vue'; // 말하기페이지
import ChatbotList from '@/pages/chatbotList/chatbotList.vue'; // 챗봇목록페이지
import ChatbotListDetail from '@/pages/chatbotList/chatbotListDetail.vue';
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
      // 과거 챗봇 대화방 목록 페이지
      path: '/chatbotList',
      name: 'ChatbotList',
      component: ChatbotList,
    },
    {
      // 과거 챗봇 대화방 목록 페이지
      path: '/chatbotListDetail/:id',
      name: 'ChatbotListDetail',
      component: ChatbotListDetail,
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
