<script setup>
import Header from '../../components/Header.vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const id = route.params.id; // 동적 경로의 id 가져오기
</script>
<template>
  <Header />
  <div class="main-container mt-5">
    <div class="card chatbot-response bg-white border-light p-4 mb-4">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <span class="font-small">
          <img
            class="avatar-sm img-fluid rounded-circle mr-2"
            src="@/assets/images/sooni.png"
            alt="avatar"
          />
          <span class="font-weight-bold">쑤니</span>
          <span class="ml-2 ms-3">March 26, 19:25</span>
        </span>
      </div>
      <p class="m-0">
        Hi Chris! Thanks a lot for this very useful template. Saved me a lot of
        time and searches on the internet.
      </p>
    </div>
    <div
      class="card user-request bg-primary text-white border-light p-4 ml-md-5 ml-lg-6 mb-4"
    >
      <div class="d-flex justify-content-between align-items-center mb-2">
        <span class="font-small">
          <span class="font-weight-bold">사용자</span>
          <span class="ml-2 ms-3">March 26, 19:25</span>
        </span>
      </div>
      <p class="m-0">
        Hi Neil, thanks for sharing your thoughts regarding Spaces. Hi Neil,
        thanks for sharing your thoughts regarding Spaces.
      </p>
    </div>
    <div class="card chatbot-response bg-white border-light p-4 mb-4">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <span class="font-small">
          <img
            class="avatar-sm img-fluid rounded-circle mr-2"
            src="@/assets/images/sooni.png"
            alt="avatar"
          />
          <span class="font-weight-bold">쑤니</span>
          <span class="ml-2 ms-3">March 26, 19:25</span>
        </span>
      </div>
      <p class="m-0">
        Hi Chris! Thanks a lot for this very useful template. Saved me a lot of
        time and searches on the internet.
      </p>
    </div>
    <div
      class="card user-request bg-primary text-white border-light p-4 ml-md-5 ml-lg-6 mb-4"
    >
      <div class="d-flex justify-content-between align-items-center mb-2">
        <span class="font-small">
          <span class="font-weight-bold">Your Answer</span>
          <span class="ml-2">March 26, 19:25</span>
        </span>
      </div>
      <p class="m-0">
        Hi Neil, thanks for sharing your thoughts regarding Spaces. Hi Neil,
        thanks for sharing your thoughts regarding Spaces.
      </p>
    </div>
  </div>
</template>
<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between; /* 요소 간의 공간을 균등하게 분배 */
  /* border: 1px solid blue; */
  height: 100vh; /* 전체 화면 높이에 맞춤 */
  height: calc(100vh - 70px); /* 화면에서 Header를 제외한 나머지 높이 */
}
.card {
  position: relative;
  display: flex;
  flex-direction: column;
  min-width: 0;
  word-wrap: break-word;
  background-color: #ffffff;
  background-clip: border-box;
  border: 0.0625rem solid rgba(0, 0, 0, 0.1);
  border-radius: 0.45rem;
}

.card-body {
  flex: 1 1 auto;
  min-height: 1px;
  padding: 1.5rem;
}
.avatar-sm {
  width: 1.5rem;
  height: 1.5rem;
}
.img-fluid {
  max-width: 100%;
  height: auto;
}
.rounded-circle {
  border-radius: 50% !important;
}
.mr-2 {
  margin-right: 0.5rem !important;
}
.font-weight-bold {
  font-weight: 500 !important;
}
.p-4 {
  padding: 1.5rem !important;
}
.text-white {
  color: #ffffff !important;
}
.bg-primary {
  background-color: #0d1b48 !important;
}

/* 챗봇 응답: 오른쪽 여백 추가 */
.chatbot-response {
  margin-left: auto; /* 자동으로 왼쪽에 여백 추가 */
  margin-right: 50px; /* 오른쪽 여백 */
  text-align: left; /* 왼쪽 정렬 */
}

/* 사용자 요청: 왼쪽 여백 추가 */
.user-request {
  margin-right: auto; /* 자동으로 오른쪽에 여백 추가 */
  margin-left: 50px; /* 왼쪽 여백 */
  text-align: left; /* 왼쪽 정렬 */
}

.bg-white {
  background-color: white !important;
}
.border-light {
  border-color: #edf0f7 !important;
}
</style>
