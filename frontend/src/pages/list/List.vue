<template>
  <Navbar />
  <div class="container">
    <div class="d-flex" id="top">
      <div class="chatbot">
        <img src="../../assets/챗봇.png" alt="챗봇 아이콘" class="icon" />
        챗봇
      </div>

      <div class="call" @click="openModal">
        <img src="../../assets/상담사.png" alt="상담사 아이콘" class="icon" />
        상담사
      </div>
      <div class="modal" v-if="showModal" @click.self="closeModal">
        <div class="modal-content">
          <a :href="'tel:' + phoneNumber" class="phone-number">
            통화 {{ phoneNumber }}</a
          >
        </div>
        <div class="modal-content">
          <button class="close-button" @click="closeModal">취소</button>
        </div>
      </div>
    </div>
    <div class="d-flex flex-column" id="middle">
      <div
        class="middle-item"
        @click="navigateToHistory"
        :style="{ cursor: 'pointer' }"
      >
        <img src="../../assets/계좌내역.png" alt="계좌 아이콘" class="icon" />
        계좌 내역 조회
        <span class="arrow">></span>
      </div>
      <div
        class="middle-item"
        @click="navigateToChatRoom"
        :style="{ cursor: 'pointer' }"
      >
        <img src="../../assets/챗봇.png" alt="챗봇 아이콘" class="icon" />
        챗봇 내역 조회
        <span class="arrow">></span>
      </div>
      <div class="middle-item">
        <img
          src="../../assets/환경설정.png"
          alt="환경 설정 아이콘"
          class="icon"
        />
        환경 설정
        <span class="arrow">></span>
      </div>
    </div>
    <div class="d-flex" id="bottom">
      <div class="ars">
        <div class="bottom-item">
          <img
            src="../../assets/ars.png"
            alt="ars 아이콘"
            class="icon"
          />사고신고 전화(ARS)
        </div>
        <div class="d-flex">
          <div class="ars1">
            <img
              src="../../assets/전화기.png"
              alt="전화기 아이콘"
              class="bottom-icon"
            />
            1588-9999
          </div>
          <div class="ars2">
            <img
              src="../../assets/전화기.png"
              alt="전화기 아이콘"
              class="bottom-icon"
            />
            1599-9999
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import Navbar from '@/components/Navbar.vue';

const props = defineProps({
  isNavShow: Boolean,
});

const emits = defineEmits(['closeNav']); // closeNav 이벤트를 emits로 선언

import Header from '@/components/Header.vue';
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import '@fortawesome/fontawesome-free/css/all.css';

const router = useRouter();

//계좌 내역 조회로 이동
function navigateToHistory() {
  router.push('/history');
}

//챗봇 내역 조회로 이동
function navigateToChatRoom() {
  router.push('/chatbotList');
}

const showModal = ref(false);
const phoneNumber = '1588-9999';

function openModal() {
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}
</script>

<style>
.page-wrapper {
  display: flex;
  flex-direction: column;
  height: 100vh;
}
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 100vh; /* 전체 화면 높이에 맞춤 */
  background-color: #fff5f2;
  padding: 20px; /* 추가적인 패딩 적용 */
  background-color: #fff5f2;
}
#top {
  width: 90%;
  display: flex;
  justify-content: space-around; /* 상단 요소 간의 공간을 균등하게 분배 */
  padding: 0 0 25px 0;
}
.chatbot,
.call {
  background-color: #ffffff;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px 25px;
  width: 45%;
  white-space: nowrap;
  cursor: pointer;
}
.chatbot {
  margin-right: 20px;
}
#middle {
  width: 90%;
  border-radius: 10px;
  background-color: #ffffff;
  padding: 1px;
}
.middle-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #e5e5e5;
}
.middle-item:last-child {
  border-bottom: none;
}
.icon {
  width: 24px;
  height: 24px;
  margin-right: 15px;
}
.arrow {
  margin-left: auto;
  color: #c0c0c0;
}
#bottom {
  width: 90%;
  margin: auto; /* 중앙 정렬 */
  background-color: #ffffff;
  margin-top: 25px;
  border-radius: 10px;
  padding: 1px;
}
.bottom-item {
  padding-bottom: 10px;
}
.ars {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 10px;
}
.ars1,
.ars2 {
  background-color: #ffffff;
  border: groove black 1px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 15px;
  width: 40%;
  white-space: nowrap;
  font-size: 12px;
  /* width: 100%;
        background-color: #FFFFFF;
        border-radius: 10px; 
        padding: 15px; 
        display: flex;
        flex-direction: column;
        align-items: center;*/
}
.ars1 {
  margin-right: 20px;
}
.bottom-icon {
  width: 14px;
  height: 14px;
  margin-right: 10px;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 90vh;
  background-color: #fff5f2;
  padding: 20px;
  width: 100%;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column; /* 세로 정렬 */
  align-items: center;
  justify-content: flex-end;
  z-index: 1000;
}

.modal-content {
  background-color: rgba(0, 0, 0, 0.5);
  padding: 5px;
  border-radius: 10px;
  text-align: center;
  width: 90%;
  max-width: 400px;
  margin-bottom: 10px; /* 아래 content와 간격 */
}

.phone-number {
  display: block;
  margin: 5px 0;
  font-size: 20px;
  color: #007bff;
  text-decoration: none;
}

.close-button {
  font-size: 20px;
  margin: 1px 0;
  background-color: rgba(0, 0, 0, 0);
  color: #007bff;
  border: none;
}

.phone-number:hover {
  text-decoration: underline;
}
</style>
