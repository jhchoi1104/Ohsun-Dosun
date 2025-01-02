<script setup>
import { useMenuStore } from '@/stores/close.js';
import List from '@/pages/list/List.vue';

const menuStore = useMenuStore();

const toggleNavShow = () => {
  menuStore.toggleNavShow(); // 상태 변경
};
</script>
<template>
  <nav class="navbar navbar-expand-sm sticky-top bg-white shadow fixed-height">
    <div class="container-fluid" style="padding: 1%">
      <router-link class="navbar-brand" to="/">
        <img
          src="@/assets/images/ohsundosunlogo.png"
          alt="Logo"
          style="height: 100%; max-height: 40px"
        />
      </router-link>

      <!--메뉴 버튼-->
      <button class="navbar-toggler" type="button" @click="toggleNavShow">
        <span class="navbar-toggler-icon"></span>
      </button>
    </div>
  </nav>

  <!-- 배경 어두운 오버레이 (슬라이드 메뉴 열릴 때 표시) -->
  <div v-if="menuStore.isNavShow" class="overlay" @click="toggleNavShow"></div>

  <!-- List 페이지를 슬라이드로 표시 -->
  <div :class="['list-slide', menuStore.isNavShow ? 'slide-in' : 'slide-out']">
    <List
      :is-nav-show="menuStore.isNavShow"
      @close-nav="menuStore.toggleNavShow"
    />
  </div>
</template>
<style scoped>
/* Navbar 관련 스타일 */
.navbar {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  z-index: 999;
}

.navbar-brand {
  display: flex;
  align-items: center;
  justify-content: center;
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml;charset=UTF8,%3Csvg viewBox='0 0 30 30' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath stroke='rgba%280, 0, 0, 0.5%29' stroke-width='2' linecap='round' linejoin='round' d='M4 7h22M4 15h22M4 23h22'/%3E%3C/svg%3E");
}

.fixed-height {
  height: 70px; /* 고정 높이 예시 */
}

#main-logo {
  justify-content: center;
  align-items: center;
  margin-top: 30px; /* 상단에서 150px 떨어지도록 추가 */
}

#main-logo > img {
  width: 250px; /* 로고 이미지의 너비 설정 */
  height: auto; /* 비율 유지 */
}

.list-slide {
  position: fixed; /* 메뉴를 화면에 고정 */
  top: 0;
  right: 0;
  height: 100vh;
  width: 92%;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  box-shadow: -5px 0 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out;
  transform: translateX(100%); /* 기본 상태에서는 오른쪽으로 숨겨져 있음 */
  z-index: 1000;
}

.list-slide.slide-in {
  transform: translateX(0); /* 메뉴가 열릴 때 */
}

.list-slide.slide-out {
  transform: translateX(100%); /* 메뉴가 닫힐 때 */
}

.navbar-toggler {
  display: inline-block;
}

.navbar-nav {
  list-style: none;
  padding: 0;
  margin: 0;
  width: 100%;
}

.nav-item {
  margin: 10px 20px;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 18px;
  font-weight: 500;
  display: block;
  width: 100%;
}

.nav-link:hover {
  color: #007bff;
}

/* 모달 콘텐츠 스타일 */
.modal-content {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  z-index: 1000; /* 오버레이 위에 모달을 표시 */
}

/* 모달 닫기 버튼 */
.close-button {
  font-size: 20px;
  background-color: transparent;
  border: none;
  color: #007bff;
}

/* 전체 화면 어두운 배경 */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 어두운 배경 */
  z-index: 999; /* 메뉴나 다른 콘텐츠 위에 표시 */
}
</style>
