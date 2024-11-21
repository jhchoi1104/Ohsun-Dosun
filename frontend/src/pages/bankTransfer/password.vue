<script setup>
import Header from '@/components/Header.vue';
import { ref } from 'vue';

// 입력된 비밀번호 상태 관리
const pin = ref('');

// 키패드 키 정의
const keys = [
  { label: '1' },
  { label: '2' },
  { label: '3' },
  { label: '4' },
  { label: '5' },
  { label: '6' },
  { label: '7' },
  { label: '8' },
  { label: '9' },
  { label: '전체 삭제', action: 'clear' },
  { label: '0' },
  { label: '⌫', action: 'backspace' },
];

// 키패드 클릭 이벤트 핸들러
const handleKeyPress = (key) => {
  if (key.action === 'clear') {
    pin.value = ''; // 전체 삭제
  } else if (key.action === 'backspace') {
    pin.value = pin.value.slice(0, -1); // 마지막 문자 삭제
  } else if (pin.value.length < 6) {
    pin.value += key.label; // 숫자 추가
  }
};
</script>

<template>
  <Header />
  <div class="main-container">
    <!-- 비밀번호 입력 상태 표시 -->
    <div class="pin-indicator pin-input">
      <h2 class="mb-4">비밀번호 입력</h2>
      <div class="dots">
        <span
          v-for="(dot, index) in 4"
          :key="index"
          class="dot"
          :class="{ filled: index < pin.length }"
        ></span>
      </div>
    </div>

    <!-- 비밀번호 분실 안내 -->
    <p class="forgot-password">비밀번호를 잊어버리셨나요?</p>

    <!-- 숫자 키패드 -->
    <div class="keypad">
      <div
        v-for="key in keys"
        :key="key.label"
        class="key"
        :class="{ 'delete-all': key.label === '전체 삭제', action: key.action }"
        @click="handleKeyPress(key)"
      >
        {{ key.label }}
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
.pin-input {
  text-align: center;
  font-family: Arial, sans-serif;
  padding: 0px;
}
.pin-indicator {
  margin: 80px 0px 0px 0px;
}
.pin-input {
  text-align: center;
  font-family: Arial, sans-serif;
  padding: 0px;
}

.dots {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.dot {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: #f0f0f0;
}

.dot.filled {
  background-color: #e74c3c;
}

.forgot-password {
  margin: 0px 0;
  color: #666;
  font-size: 14px;
}

.keypad {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 3개의 열로 균등하게 분할 */
  height: 50vh; /* 화면의 전체 높이를 차지 */
  gap: 0; /* 칸 간격을 0으로 설정 */
  width: 100%; /* 전체 너비를 화면에 맞춤 */
  padding: 0 10px; /* 화면 양쪽에 약간의 여백 추가 (원하는 경우 조정) */
  box-sizing: border-box; /* 패딩과 테두리를 포함한 크기 계산 */
}

.key {
  background-color: #ef5554;
  color: white;
  font-weight: bold;
  font-size: 26px;
  border: 0.5px solid white; /* 칸을 구분하는 경계선 */
  box-sizing: border-box; /* 패딩과 경계선을 포함하여 크기 계산 */
  cursor: pointer;
  text-align: center;
  line-height: 84px; /* 키의 높이를 적절하게 조정 */
  width: 100%;
  height: 84px; /* 키 높이를 조정하여 너무 크지 않게 함 */
}
/* '전체 삭제' 텍스트의 폰트 크기 줄이기 */
.key.delete-all {
  font-size: 20px; /* '전체 삭제' 텍스트의 크기만 줄임 */
}

.key.action {
  background-color: #ef5554;
}
</style>
