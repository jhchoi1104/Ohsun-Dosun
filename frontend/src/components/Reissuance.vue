<template>
  <div :class="['inputform-modal', { hidden: !show }]">
    <div class="inputform-container">
      <span class="close-button" @click="$emit('close')">&times;</span>
      <div class="sss">
        <div class="additional-bubble" v-if="chatbotMessage">
          <p v-html="chatbotMessage"></p>
        </div>
      </div>
      <div v-if="currentStep === 3" class="input-group">
        <label for="account-select" class="input-label">계좌 번호 선택</label>
        <select
          id="account-select"
          v-model="selectedAccount"
          class="input-field"
        >
          <option
            v-for="(account, index) in accountOptions"
            :key="index"
            :value="account"
          >
            {{ account }}
          </option>
        </select>
      </div>
      <div class="input-section" v-if="currentStep !== 2">
        <div
          v-for="(input, index) in dynamicInputs"
          :key="index"
          class="input-group"
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
      </div>
      <div v-if="currentStep === 2" class="list-section">
        <ul class="option-list">
          <li class="option-item" @click="selectOption('1')">지점 수령</li>
          <li class="option-item" @click="selectOption('2')">STM 수령</li>
          <li class="option-item" @click="selectOption('3')">등기 우편 수령</li>
        </ul>
      </div>

      <div class="button-section">
        <button class="chat-button" @click="nextStep">확인</button>
      </div>

      <div v-if="currentStep === 4 || currentStep === 5" class="additional-image-section">
        <img src="@/assets/images/sooni.png" alt="" class="step-image" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, computed, ref, watch } from 'vue';
import { bringAudioFromServer } from '@/api/TtsApi.js'; // 경로가 올바른지 확인
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

let audio;
// update:step 이벤트를 추가
const emit = defineEmits(['close', 'update:step']);

const accountOptions = [
  'KB나라사랑우대통장 947402-00-094829',
  'KB종합통장-보통예금 535901-01-129583',
];

const currentStep = ref(props.step);

// props의 step이 변경되면 currentStep도 업데이트
watch(
  () => props.step,
  (newStep) => {
    currentStep.value = newStep;
  }
);
const exampleString = {
    1: "통장 재발행 신청을 도와드리겠습니다.<br>먼저 고객님의 성함, 생년월일을 입력해주세요.",
    2: "원하시는 통장 재발급 수령 방법을 선택해 주세요",
    3: "지점수령을 선택하셨군요! 수령할 계좌와 수령하실 분의 수령 방법을 선택해주세요.",
    4: "지점 수령은 KB 국민은행 지점에서 받을 수 있습니다. 신청 후 5일 이내에 받을 수 있으며, 발행 수수료가 부과될 수 있습니다. 동의하시면 확인 버튼을 눌러주세요.",
    5: "통장 재발행 신청이 완료되었습니다. 고객님께서 선택하신 방식으로 통장 수령이 가능합니다. 추가 문의 사항이 있으시면 언제든지 말씀해 주세요." 
  };

// 스텝별로 chatbotMessage 설정
const chatbotMessages = {
  1: '통장 재발행 신청을 도와드리겠습니다.<br>먼저 고객님의 성함, 생년월일을 입력해주세요.',
  2: '통장 재발급 신청을 도와드리겠습니다. 원하시는 통장 재발급 수령 방법을 선택해 주세요.',
  3: '지점 수령을 선택하셨군요! <br> 수령할 계좌와 수령하실 분의 성함을 입력해주세요.',
  4: "지점 수령은 KB 국민은행 지점에서 받을 수 있습니다. 신청후 5일 이내에 받을 수 있으며, 발행 수수료가 발생할 수 있습니다.<br>동의하시면 '확인'을 눌러주세요.",
  5: '통장 재발행 신청이 완료되었습니다!<br> 고객님께서 선택하신 방식으로 통장 수령이 가능합니다. 추가 문의사항이 있으시면 언제든지 말씀해 주세요. 감사합니다',
};


const playAudio = async (step) => {
    try {
                        // 음성 출력이 진행 중이라면 멈추기
                        if (audio && !audio.paused) {
            audio.pause(); // 이전 오디오 중지
            audio.currentTime = 0; // 오디오를 처음으로 되돌리기
        }
        // Get the audio message for the current step
        const audioMessage = exampleString[step];
        console.log(audioMessage);
        // 서버에서 오디오 데이터 가져오기
        const base64Audio = await bringAudioFromServer(audioMessage);

        // Base64 디코딩 및 오디오 재생
        const byteCharacters = atob(base64Audio);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        const audioBlob = new Blob([byteArray], { type: 'audio/wav' });

        // Blob URL 생성 후 오디오 재생
        const audioUrl = URL.createObjectURL(audioBlob);
        console.log(audioUrl);
        audio = new Audio(audioUrl);
        audio.play();
    } catch (error) {
        console.error('TTS 처리 중 오류:', error);
        alert('오류가 발생했습니다. 콘솔을 확인하세요.');
    }
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
        placeholder: '생년월일 8자리를 입력해주세요.',
        value: '',
        label: '생년월일',
      },
    ];
  } else if (currentStep.value === 2) {
    return [];
  } else if (currentStep.value === 3) {
    return [
      { type: 'text', placeholder: '수령인 이름', value: '', label: '수령인 이름' },
    ];
  } else if (currentStep.value === 4) {
    return [];
  } else if (currentStep.value === 5) {
    return [];
  }
  return [];
});

// 스텝을 증가시키는 함수
const nextStep = () => {
    if (currentStep.value < 5) {
        currentStep.value += 1; // 로컬 상태 업데이트
        emit('update:step', currentStep.value); // 부모 컴포넌트에 업데이트 알림
        playAudio(currentStep.value); // 현재 단계를 playAudio에 전달
    } else {
        emit('close'); // 6단계에 도달하면 종료 이벤트 발생
        location.reload(); // 페이지 새로고침 추가
    }
};
const selectOption = (option) => {
  console.log(`${option} 선택됨`);
  nextStep(); // 옵션 선택 후 스텝 증가
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
  height: 85vh; /* 최대 높이를 80vh로 증가시킴 */
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
  height: 150px; /* 고정된 높이 설정 */
  overflow-y: auto; /* 텍스트 넘칠 때 스크롤 */
}

.additional-bubble {
  background-color: #efefef; /* 말풍선 배경색 */
  border-radius: 10px; /* 모서리 둥글게 */
  padding: 10px 15px; /* 패딩 추가 */
  /* max-width: 100%; */ /* Remove or comment out this line */
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

.chat-button:active {
    background-color: #d9534f;
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
.step-image {
  /* justify-content: center; Center the image horizontally */
  align-items: center; /* Center the image vertically */
  max-width: 70%; /* Ensure the image does not exceed the width of its container */
  margin: 10px 0; /* Add some margin for spacing */
  position: absolute; /* Positioning to allow for absolute placement */
  left: 50%; /* Center the image horizontally */
  transform: translateX(-50%); /* Adjust for centering */
  z-index: 1; /* Ensure it appears above other elements */
}

</style>
