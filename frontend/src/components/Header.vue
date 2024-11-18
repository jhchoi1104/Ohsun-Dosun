<script setup>
// 메뉴 상태를 관리하는 변수 (토글을 위한 상태)
import { ref } from 'vue';

const isNavShow = ref(false);

// 메뉴 상태를 토글하는 함수
const toggleNavShow = () => {
  isNavShow.value = !isNavShow.value;
  console.log('isNavShow.value:', isNavShow.value);
};
</script>
<template>
  <nav class="navbar navbar-expand-sm sticky-top bg-white shadow fixed-height">
    <div class="container-fluid" style="padding: 1%">
      <router-link class="navbar-brand" to="/">
        <img
          src="@/assets/images/logo.png"
          alt="Logo"
          style="height: 100%; max-height: 40px"
        />
      </router-link>

      <!--메뉴 버튼-->
      <button class="navbar-toggler" type="button" @click="toggleNavShow">
        <span class="navbar-toggler-icon"></span>
      </button>
      <!-- 메뉴 항목 (Hamburger Menu) -->
      <div :class="['navbar-collapse', isNavShow ? 'show' : 'collapse']">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/history">
              계좌 내역 조회
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/chatbot">
              챗봇 내역 조회
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/settings">
              환경설정
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
<style scoped>
/* Navbar 관련 스타일 */
.navbar {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
}

.navbar-brand {
  display: flex;
  align-items: center;
  justify-content: center;
}

.navbar-toggler {
  border: none;
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

.navbar-collapse {
  flex-basis: auto;
  display: flex;
  flex-direction: column; /* 메뉴 항목들이 세로로 나열되도록 설정 */
  align-items: center; /* 메뉴 항목들이 중앙 정렬되도록 설정 */
  transition: max-height 0.5s ease-in-out;
  overflow: hidden; /* 숨겨진 상태에서 내용이 넘치지 않도록 설정 */
  max-height: 0; /* 메뉴 항목이 숨겨질 때는 최대 높이를 0으로 설정 */
  position: absolute; /* 메뉴 항목이 헤더 아래에 위치하도록 설정 */
  top: 70px; /* 헤더 바로 아래에 위치하도록 설정 (헤더 높이에 맞게 조정) */
  left: 0;
  width: 100%; /* 메뉴가 화면 가로 크기에 맞게 펼쳐지도록 설정 */
}

.navbar-toggler {
  display: inline-block;
}

.navbar-collapse.show {
  display: flex; /* show 클래스가 있을 때만 display block */
  max-height: 300px; /* 메뉴 항목이 펼쳐질 때 충분한 최대 높이를 설정 */
}

/* 강제로 collapse 상태에서 display:none을 없앰 */
.navbar-collapse.collapse {
  display: none;
}

/* 각 항목 사이 여백 설정 */
.navbar-nav .nav-item {
  margin: 5px 0; /* 항목 간 간격 설정 */
}
</style>
