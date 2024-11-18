<script setup>
import Header from '../../components/Header.vue';

import { useRouter } from 'vue-router';

const router = useRouter();

function navigateTo(id) {
  router.push({ name: 'ChatbotListDetail', params: { id } });
}
</script>
<template>
  <Header />
  <div class="main-container">
    <div class="col-12">
      <div class="card mt-4 mb-3 py-3" @click="navigateTo(1)">
        <div class="card-body d-flex align-items-center py-0">
          <div class="col-3 text-truncate">서비스타입</div>

          <div class="col-7 px-0">
            <div class="text-truncate">챗봇 title 명이 긴경우는 온점이</div>
          </div>
          <div class="col-2 ms-3">
            <span class="text-truncate">날짜</span>
          </div>
        </div>
      </div>

      <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center mt-5">
          <li class="page-item">
            <a class="page-link" href="#">Previous</a>
          </li>
          <li class="page-item active">
            <a class="page-link" href="#">1</a>
          </li>
          <li class="page-item">
            <a class="page-link" href="#">2</a>
          </li>
          <li class="page-item">
            <a class="page-link" href="#">3</a>
          </li>
          <li class="page-item">
            <a class="page-link" href="#">4</a>
          </li>
          <li class="page-item">
            <a class="page-link" href="#">5</a>
          </li>
          <li class="page-item">
            <a class="page-link" href="#">Next</a>
          </li>
        </ul>
      </nav>
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

#main-logo {
  justify-content: center;
  align-items: center;
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

.form-group {
  margin-bottom: 1rem;
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
.py-0 {
  padding-top: 0 !important;
}
.py-3 {
  padding-bottom: 1rem !important;
}
.text-truncate {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.card:hover {
  border-color: #007bff; /* hover 시 테두리 색상 변경 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* hover 시 부각 효과 */
}

.card-hover:active {
  border-color: #0056b3; /* 클릭 시 테두리 색상 변경 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); /* 클릭 시 그림자 효과 증가 */
}
.pagination {
  display: flex;
  padding-left: 0;
  list-style: none;
  border-radius: 0.45rem;
}
.page-link {
  position: relative;
  display: block;
  padding: 0.5rem 0.75rem;
  margin-left: -0.0625rem;
  line-height: 1.25;
  color: #4e5079;
  background-color: #ffffff;
  border: 0.0625rem solid #edf0f7;
}

.page-link:hover {
  z-index: 2;
  color: #4e5079;
  text-decoration: none;
  background-color: #edf0f7;
  border-color: #edf0f7;
}

.page-link:focus {
  z-index: 3;
  outline: 0;
  box-shadow: 0 0 0 0.2rem rgba(69, 77, 103, 0.5);
}
.page-item:first-child .page-link {
  margin-left: 0;
  border-top-left-radius: 0.45rem;
  border-bottom-left-radius: 0.45rem;
}

.page-item:last-child .page-link {
  border-top-right-radius: 0.45rem;
  border-bottom-right-radius: 0.45rem;
}

.page-item.active .page-link {
  z-index: 3;
  color: #ffffff;
  background-color: #0d1b48;
  border-color: #0d1b48;
}

.page-item.disabled .page-link {
  color: #4e5079;
  pointer-events: none;
  cursor: auto;
  background-color: #ffffff;
  border-color: #f5f9fe;
}
</style>
