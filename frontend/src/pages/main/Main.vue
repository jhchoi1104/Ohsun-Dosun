<template>
  <div class="main-container">
    <div class="sub-container">
      <div id="main-logo">
        <img src="@/assets/images/logo.png" alt="" />
      </div>
      <div id="main-character">
        <img src="@/assets/images/sooni.png" alt="" />
      </div>
    </div>
    <div class="button-section">
      <button class="chat-button" @click="goToChat">시작하기</button>
    </div>
  </div>
</template>

<script>
import '@/assets/main.css';

export default {
  name: 'Main',
  methods: {
    goToChat() {
      this.$router.push('/chat');
    },
  },
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
  background-color: red;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  cursor: pointer;
  width: 100%;
  height: 55px;
  font-size: 1.5rem;
  font-weight: bold;
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
  width: 250px; /* 로고 이미지의 너비 설정 */
  height: auto; /* 비율 유지 */
}
</style>
