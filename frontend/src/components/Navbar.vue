<template>
  <nav class="navbar navbar-light">
    <div class="container-fluid nav-container">
      <span class="navbar-brand" href="#" id="logotext" @click="goToPage('/')">
        오순도순
        <img
          src="../assets/로고이미지.png"
          alt=""
          width="30"
          height="24"
          class="d-inline-block align-text-top"
        />
      </span>
      <button
        @click="closeNav"
        id="menu"
        type="button"
        aria-label="Close"
        width="100%"
      >
        X
      </button>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'; // 페이지 이동을 위한 router 사용
import { useMenuStore } from '@/stores/close.js';

const router = useRouter(); // router 인스턴스 생성
const menuStore = useMenuStore();
const closeNav = menuStore.closeNav;

const isMenuOpen = ref(false); // 메뉴의 열림/닫힘 상태를 저장하는 변수
console.log(isMenuOpen.value);

// 페이지 이동 함수
const goToPage = (path) => {
  router.push(path).then(() => {
    //네비게이션 완료 후 메뉴 상태 초기화
    menuStore.isNavShow = false;
  }); // path로 이동
};

// 메뉴 토글 함수
const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value; // 메뉴 상태 변경
  closeNav(); // 메뉴를 닫는 액션 호출 (store에서 정의된 대로)
};
</script>

<style>
/* 네비게이션 바 스타일 */
.nav-container {
  display: flex; /* flexbox 활성화 */
  justify-content: space-between; /* 요소를 양쪽 끝으로 배치 */
  align-items: center; /* 세로 방향 가운데 정렬 */
  padding: 0 16px; /* 좌우 여백 추가 */
  outline: 1px;
  width: 100%;
}

#logotext {
  flex: 1;
  text-align: left;
  color: red;
  cursor: pointer;
}

#menu {
  flex: 0;
  background-color: #ffffff;
  border-style: none;
  color: black;
  padding: 8px 20px;
}
</style>
