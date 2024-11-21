<template>
  <div :class="['inputform-modal', { hidden: !show }]">
    <div class="inputform-container">
      <span class="close-button" @click="$emit('close')">&times;</span>
      <div class="sss">
        <div class="additional-bubble" v-if="chatbotMessage">
          <!-- {{ chatbotMessage }} -->
          <p v-html="chatbotMessage"></p>
        </div>
      </div>
      <div class="input-section" v-if="currentStep !== 4">
        <div
          v-for="(input, index) in dynamicInputs"
          :key="index"
          class="input-group"
          v-if="currentStep !== 3 && currentStep !== 5"
        >
          <label :for="'input-' + index" class="input-label">{{
            input.label
          }}</label>
          <input
            :id="'input-' + index"
            :type="input.type"
            :placeholder="input.placeholder"
            v-model="input.value"
            class="input-field"
          />
        </div>
        <div v-if="currentStep === 3" class="list-section">
          <ul class="option-list">
            <li class="option-item" @click="selectOption('1')">
              국민은행 계좌 인증
            </li>
            <li class="option-item" @click="selectOption('2')">
              1원 입금 인증
            </li>
            <li class="option-item" @click="selectOption('3')">화상 통화</li>
          </ul>
        </div>
      </div>
      <!-- Step 4일 때 Camera 컴포넌트 표시 -->
      <Camera v-if="currentStep === 4" :show="currentStep === 4" />

      <!-- Step 5: 약관 동의 -->
      <div v-if="currentStep === 5" class="terms-section">
        <input type="checkbox" id="terms-agree" v-model="termsAgreed" />
        <label for="terms-agree">
          <a href="#" @click.prevent="openTermsPopup">약관 및 상품 설명서</a>에
          동의합니다.
        </label>
      </div>

      <div class="button-section">
        <button class="chat-button" @click="nextStep">확인</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, computed, ref, watch } from 'vue';
import Camera from './Camera.vue'; // Camera 컴포넌트 임포트

const props = defineProps({
  show: {
    type: Boolean,
    required: true,
  },
  step: {
    type: Number,
    default: 1,
    required: true,
  },
});

// update:step 이벤트를 추가
const emit = defineEmits(['close', 'update:step']);

const currentStep = ref(props.step);
const termsAgreed = ref(false); // 약관 동의 상태

// props의 step이 변경되면 currentStep도 업데이트
watch(
  () => props.step,
  (newStep) => {
    currentStep.value = newStep;
  }
);

// 스텝별로 chatbotMessage 설정
const chatbotMessages = {
  1: '입출금 계좌 개설을 위해 본인 인증이 필요합니다.<br>이름과 주민등록번호를 입력해주세요.',
  2: '입출금 계좌 개설을 위해 다음 단계로 이동합니다.<br>휴대폰 인증, 신분증 확인이 필요합니다.',
  3: '계좌 개설을 위해 추가 본인 인증이 필요합니다.<br>원하는 인증 방법 하나를 고르세요.',
  4: '신분증과 얼굴이 잘 나오게 화면을 봐주세요.',
  5: '마지막 입력입니다!<br>고객 확인 정보를 입력해주세요.',
  6: '축하합니다! 입출금 계좌 개설이 완료되었습니다. 이제 통장을 사용할 수 있습니다.',
};

const chatbotMessage = ref(chatbotMessages[currentStep.value] || '');

// currentStep이 변경될 때마다 chatbotMessage를 업데이트
watch(currentStep, (newStep) => {
  chatbotMessage.value = chatbotMessages[newStep] || '';
});

const dynamicInputs = computed(() => {
  if (currentStep.value === 1) {
    return [
      { type: 'text', placeholder: '이름', value: '', label: '이름' },
      {
        type: 'number',
        placeholder: '주민등록번호 13자리를 입력해주세요.',
        value: '',
        label: '주민등록번호',
      },
    ];
  } else if (currentStep.value === 2) {
    return [
      { type: 'text', placeholder: '이름', value: '', label: '이름' },
      { type: 'text', placeholder: '생년월일', value: '', label: '생년월일' },
      {
        type: 'text',
        placeholder: '휴대폰 번호',
        value: '',
        label: '휴대폰 번호',
      },
    ];
  } else if (currentStep.value === 3) {
    return [];
  } else if (currentStep.value === 4) {
    return [];
  } else if (currentStep.value === 5) {
    return [];
  } else if (currentStep.value === 6) {
    return [];
  }
  return [];
});

// 스텝을 증가시키는 함수
const nextStep = () => {
  if (currentStep.value < 6) {
    currentStep.value += 1; // 로컬 상태 업데이트
    emit('update:step', currentStep.value); // 부모 컴포넌트에 업데이트 알림
  } else {
    emit('close'); // 모달 닫기
    location.reload(); // 페이지 새로고침
  }
};


const selectOption = (option) => {
  console.log(`${option} 선택됨`);
  nextStep(); // 옵션 선택 후 스텝 증가
};

// 팝업으로 PDF 열기 함수
const openTermsPopup = () => {
  window.open(
    'http://localhost:5173/Perms.pdf',
    '약관 및 상품 설명서',
    'width=800,height=600'
  );
};
</script>

<style scoped>
.inputform-modal {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  z-index: 1000;
  transition: transform 0.3s ease;
  transform: translateY(100%);
}

.inputform-modal:not(.hidden) {
  transform: translateY(0);
}

.inputform-container {
  background-color: white;
  padding: 10px;
  border-radius: 10px 10px 0 0;
  width: 100%;
  max-width: 500px;
  height: 80vh; /* 최대 높이를 80vh로 증가시킴 */
  display: flex;
  flex-direction: column;
}

.close-button {
  font-size: 1.5rem;
  font-weight: bold;
  cursor: pointer;
  text-align: right;
  display: block;
}

.sss {
  height: 100px; /* 고정된 높이 설정 */
  overflow-y: auto; /* 텍스트 넘칠 때 스크롤 */
}

.additional-bubble {
  background-color: #efefef; /* 말풍선 배경색 */
  border-radius: 10px; /* 모서리 둥글게 */
  padding: 10px 15px; /* 패딩 추가 */
  max-width: 100%; /* 최대 너비 설정 */
  text-align: center; /* 텍스트 중앙 정렬 */
  margin: 5px auto; /* 위아래 간격을 줄임 */
}

.input-section {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.input-group {
  display: flex;
  flex-direction: column;
}

.input-label {
  margin-bottom: 5px;
  font-weight: bold;
  color: #333; /* Optional: Change color for better visibility */
}

.input-field {
  width: 100%;
  height: 45px;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.input-field:focus {
  border-color: #007bff;
  outline: none;
}

.button-section {
  display: flex;
  justify-content: center;
  margin-top: 15px;
  right: 0;
  gap: 10px;
}

.chat-button {
  background-color: #ef5554;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  text-align: center;
  font-size: 1.5rem;
  cursor: pointer;
  width: 100%;
  max-width: 300px;
  height: 55px;
}

.chat-button:hover {
  background-color: #0056b3;
}

.option-list {
  list-style-type: none;
  padding: 0;
}

.option-item {
  background-color: #f0f0f0;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.option-item:hover {
  background-color: #e0e0e0;
}
</style>
