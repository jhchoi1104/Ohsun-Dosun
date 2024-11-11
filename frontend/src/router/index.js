import { createRouter, createWebHistory } from 'vue-router';
import Main from '@/pages/main/Main.vue'; // 메인페이지
import List from '@/pages/list/List.vue'; //조회페이지

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
      //조회페이지
      path:'/list',
      name:'List',
      component:List,
    }
  ],
});

export default router;
