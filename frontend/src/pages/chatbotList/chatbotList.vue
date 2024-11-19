<script setup>
import Header from '../../components/Header.vue';
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const chatRoomList = ref([]); // 메시지를 저장할 변수

// 메시지 데이터를 가져오는 함수
const fetchChatRoomList = async () => {
  try {
    // GET 요청을 통해 데이터 가져오기
    const response = await axios.get('http://localhost:8080/api/chatbotRoom');
    chatRoomList.value = response.data; // 서버 응답 데이터를 messages에 저장
    console.log(chatRoomList.value); // 콘솔에 응답 데이터 출력
  } catch (error) {
    console.error('Error fetching messages:', error); // 오류 처리
  }
};

const formatDate = (dateString) => {
  const now = new Date();
  const date = new Date(dateString);

  // 날짜 차이를 계산 (밀리초 단위)
  const diffTime = now - date;
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24)); // 밀리초를 일 단위로 변환

  const year = date.getFullYear();
  const month = date.getMonth() + 1; // 0-based month (January is 0)
  const day = date.getDate();

  // 오늘, 어제, 지난 7일 이내, 같은 해의 월, 그 외 경우 처리
  if (diffDays === 0) {
    return '오늘'; // 오늘 날짜
  } else if (diffDays === 1) {
    return '어제'; // 어제 날짜
  } else if (diffDays <= 7) {
    return `지난 7일`; // 이틀에서 일주일 이내
  } else if (now.getFullYear() === year) {
    return `${month}월`; // 같은 해에 속하는 날짜
  } else {
    return `${year}년`; // 다른 해의 날짜
  }
};

// 컴포넌트가 마운트될 때 fetchMessages 함수 실행
onMounted(() => {
  fetchChatRoomList();
});

const router = useRouter();
function navigateTo(id) {
  router.push({ name: 'ChatbotListDetail', params: { id } });
}
</script>
<template>
  <Header />
  <div class="main-container">
    <div class="col-12">
      <div
        v-for="(chatRoom, index) in chatRoomList"
        :key="index"
        class="card mt-4 mb-3 py-3"
        @click="navigateTo(chatRoom.sessionId)"
      >
        <div class="card-body d-flex align-items-center py-0">
          <div class="col-9 px-0">
            <div class="text-truncate">
              {{ chatRoom.title }}
            </div>
          </div>
          <div class="col-3 d-flex justify-content-center ms-3">
            <span class="text-truncate text-muted">
              {{ formatDate(chatRoom.endTime) }}
              <!-- 날짜 포맷 -->
            </span>
          </div>
        </div>
      </div>
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
