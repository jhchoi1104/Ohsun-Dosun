<template>
  <Header />
  <div class="main-container">
    <div class="confirm-form-container">
      <div class="confirm-form">
        <div class="d-flex flex-column">
          <div class="p-2">
            <iframe
              src="https://giphy.com/embed/grQgrKjbnzo7s29qGj"
              width="80"
              height="80"
              frameBorder="0"
              class="giphy-embed"
              allowFullScreen
            ></iframe>
          </div>
          <div class="p-2">
            <!-- 송금받는 사람 이름 -->
            <div id="transfer-to">{{ inputStore.inputs.name }}님께</div>
            <!-- 송금액 -->
            <div id="transfer-amount">{{ inputStore.inputs.amount }}원</div>
            <!-- 성공 메시지 -->
            <div id="success-msg">이체가 완료되었습니다.</div>
          </div>
          <div class="p-2">
            <!-- 송금한 계좌 -->
            <div id="account-number">
              <div class="d-flex">
                <div class="flex-grow-1">
                  {{ inputStore.inputs.recieveraccountbank }}
                  {{ inputStore.inputs.recieveraccountnumber }}
                </div>
                <div id="star-btn" class="justify-content-end" @click="openModal">
                  <img src="@/assets/images/emptystar.png" alt="favorite" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="button-section">
      <router-link class="chat-button" to="/chat">확인</router-link>
    </div>
  </div>

   <!-- 팝업창 (Modal) -->
   <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
    <div class="modal">
      <h2>별칭 저장하기</h2>
      <input type="text" v-model="nickname" placeholder="별칭을 입력하세요" />
      <div class="modal-buttons">
        <button @click="saveNickname">저장</button>
        <button @click="closeModal">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import '@/assets/main.css';
import Header from '@/components/Header.vue';
import axios from 'axios';
import {ref} from 'vue';
import { useInputStore } from '@/stores/inputStore'; // 스토어 임포트
import { useAuthStore } from '@/stores/auth'; //auth 스토어 임포트

const inputStore = useInputStore(); // 스토어 인스턴스 생성
const authStore = useAuthStore(); // auth 스토어 인스턴스

// 팝업 상태와 별칭 입력 상태 관리
const showModal = ref(false);
const nickname = ref('');

const openModal = () => {
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  nickname.value = ''; // 별칭을 입력하세요:'); 초기화
};

const saveNickname = async () => {
  if (!nickname.value.trim()) {
    alert('별칭을 입력해야 합니다.');
    return;
  }
  
//동적으로 userId가져오기
const userId = authStore.state.userId;
if (!userId){
  alert('로그인 정보가 없습니다.');
  return;
}

  const payload = {
    nickname: nickname.value,
    accountnumber: inputStore.inputs.recieveraccountnumber,
    userId: userId,
  };

  try {
    const response = await axios.post('/api/v1/nickname', payload, {
      headers: { userId: userId}, // 사용자 ID를 헤더에 추가 (예제)
    });
    alert(response.data); // 서버의 응답 메시지 표시
    closeModal();
  } catch (error) {
    console.error('별칭 저장 오류:', error);
    alert('별칭 저장에 실패했습니다.');
  }
};
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  /* border: 1px solid blue; */
  height: 90vh;
  position: relative;
}
.confirm-form-container {
  display: flex;
  align-items: center;
  justify-content: center;
  /* border: 1px solid green; */
  width: 100%;
  line-height: normal;
  position: absolute;
  top: 50%;
  transform: translateY(-60%);
}

.confirm-form {
  width: 100%;
  padding: 20px;
  text-align: center;
}

.confirm-form .d-flex {
  align-items: center;
}

.confirm-form .p-2 {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

#transfer-to,
#transfer-amount {
  font-weight: bold;
  font-size: 1.4rem;
  margin-bottom: 8px;
}

#success-msg {
  font-size: 1.4rem;
}

#account-number {
  border: 1px solid lightgray;
  border-radius: 30px;
  width: 90%;
  padding: 5px 30px 5px 30px;
  font-size: 1.1rem;
}
#star-btn {
  width: 30px;
}
#star-btn img {
  width: 24px;
  height: 24px;
}

.button-section {
  position: absolute;
  /* bottom: 30px; */
  width: 100%;
  padding: 0 20px;
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
  display: inline-block;
  width: 100%;
  text-decoration: none;
}
/* 모달 오버레이 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6); /* 반투명 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

/* 모달 박스 */
.modal {
  background: #ffffff;
  width: 250px; /* 너비와 높이를 동일하게 설정 */
  height: 250px; /* 정사각형 */
  display: flex;
  flex-direction: column;
  justify-content: center; /* 내용 세로 정렬 */
  align-items: center; /* 내용 가로 정렬 */
  border-radius: 10px; /* 모서리 둥글게 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
  text-align: center;
  animation: fadeIn 0.3s ease-in-out; /* 부드러운 나타남 효과 */
  margin: 150px 50px 100px 65px;
}

/* 모달 버튼 영역 */
.modal-buttons {
  margin-top: 15px;
  display: flex;
  gap: 10px; /* 버튼 간 간격 */
}

/* 버튼 스타일 */
.modal-buttons button {
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.modal-buttons button:first-child {
  background-color: #007bff;
  color: #ffffff;
}

.modal-buttons button:first-child:hover {
  background-color: #0056b3;
}

.modal-buttons button:last-child {
  background-color: #f8f9fa;
  color: #212529;
}

.modal-buttons button:last-child:hover {
  background-color: #e2e6ea;
}

/* 입력 필드 스타일 */
.modal input {
  width: 80%; /* 입력 필드 너비 */
  padding: 10px;
  margin-top: 10px;
  border: 1px solid #ced4da;
  border-radius: 5px;
  font-size: 14px;
}

.modal input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 3px rgba(0, 123, 255, 0.5);
}

/* 모달 애니메이션 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}


</style>
