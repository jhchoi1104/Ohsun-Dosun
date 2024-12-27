<template>
  <div class="main-container">
    <div class="sub-container">
      <div id="main-logo">
        <img src="@/assets/images/ohsundosunlogo.png" alt="" />
      </div>
      <div id="main-character">
        <img src="@/assets/images/sooni.png" alt="" />
      </div>
    </div>
    <div class="button-section">
      <button class="chat-button" @click="checkLogin">시작하기</button>
    </div>
  </div>
</template>

<script setup>
import '@/assets/main.css';
import { useAuthStore } from '@/stores/auth.js'; // auth store import
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

console.log('로그인 상태:', authStore.isLogin);

const checkLogin = () => {
  if (!authStore.isLogin) {
    // 로그인되지 않은 상태라면 로그인 페이지로 리다이렉트
    router.push('/login');
  } else {
    // 로그인 상태라면 채팅 페이지로 이동
    router.push('/chat');
  }
};
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between; /* 요소 간의 공간을 균등하게 분배 */
  /* border: 1px solid blue; */
  height: 100vh; /* 전체 화면 높이에 맞춤 */
}

.sub-container {
  padding: 20px;
  width: 100%;
  text-align: center;
  gap: 100px;
  /* border: 1px solid red; */
}

.button-section {
  margin-bottom: 30px; /* 하단에서 20px 간격 설정 */
  width: 100%; /* 버튼 섹션의 너비를 100%로 설정 */
  padding: 0 20px; /* 좌우 패딩 20px 추가 */
}

.chat-button {
  background-color: #ef5554;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  cursor: pointer;
  width: 100%;
  height: 55px;
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  display: inline-block; /* block으로 변경하여 너비 조정 */
  width: 100%; /* 부모 요소 크기를 기반으로 하므로, 100%로 설정 */
  text-decoration: none; /* 링크 기본 스타일 제거 */
}

#main-logo,
#main-character {
  justify-content: center;
  align-items: center;
}

#main-character > img {
  width: 270px;
}

#main-character {
  position: absolute;
  bottom: 190px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1;
}

#main-logo {
  justify-content: center;
  align-items: center;
  margin-top: 30px; /* 상단에서 150px 떨어지도록 추가 */
}

#main-logo > img {
  width: 300px; /* 로고 이미지의 너비 설정 */
  height: auto; /* 비율 유지 */
}
</style>
