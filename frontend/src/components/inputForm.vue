<template>
    <div :class="['inputform-modal', { hidden: !show }]">
      <div class="inputform-container">
        <span class="close-button" @click="$emit('close')">&times;</span>
        <div class="input-section">
          <div v-for="(input, index) in dynamicInputs" :key="index" class="input-group" v-if="props.step !== 3">
            <label :for="'input-' + index" class="input-label">{{ input.label }}</label>
            <input
              :id="'input-' + index"
              :type="input.type"
              :placeholder="input.placeholder"
              v-model="input.value"
              class="input-field"
            />
          </div>
          <div v-if="props.step === 3" class="list-section">
            <ul class="option-list">
              <li class="option-item" @click="selectOption('지점 수령')">지점 수령</li>
              <li class="option-item" @click="selectOption('STM 수령')">STM 수령</li>
              <li class="option-item" @click="selectOption('등기 우편 수령')">등기 우편 수령</li>
            </ul>
          </div>
        </div>
        <div class="button-section">
          <router-link class="chat-button" to="/chat">확인</router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineProps, defineEmits, computed } from 'vue';
  
  const props = defineProps({
    show: {
      type: Boolean,
      required: true,
    },
    step: {
      type: Number,
      default: 3,
      required: true,
    },
  });
  
  const emit = defineEmits(['close']);
  
  const dynamicInputs = computed(() => {
    if (props.step === 1) {
      return [
        { type: 'text', placeholder: '이름', value: '', label: '이름' },
        { type: 'number', placeholder: '주민등록번호 13자리를 입력해주세요.', value: '', label: '주민등록번호' },
      ];
    } else if (props.step === 2) {
      return [
        { type: 'text', placeholder: '이름', value: '', label: '이름' },
        { type: 'text', placeholder: '생년월일', value: '', label: '생년월일' },
        { type: 'text', placeholder: '휴대폰 번호', value: '', label: '휴대폰 번호' },
      ];
    } else if (props.step === 3) {
      return [];
    } else {
      return [
        { type: 'text', placeholder: '기본 입력 1', value: '', label: '기본 입력 1' },
        { type: 'text', placeholder: '기본 입력 2', value: '', label: '기본 입력 2' },
      ];
    }
  });
  
  const selectOption = (option) => {
    console.log(`${option} 선택됨`);
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
    align-items: flex-end; /* 하단에서 시작 */
    z-index: 1000;
    transition: transform 0.3s ease; /* 부드러운 애니메이션 */
    transform: translateY(100%); /* 초기 위치: 화면 아래 */
  }
  
  .inputform-modal:not(.hidden) {
    transform: translateY(0); /* 화면에 표시 */
  }
  
  .inputform-container {
    background-color: white;
    padding: 10px;
    border-radius: 10px 10px 0 0; /* 상단 모서리만 둥글게 */
    width: 100%;
    max-width: 500px; /* 최대 너비 설정 */
    height: 60vh; /* 높이 늘리기 */
  }
  
  .close-button {
    font-size: 1.5rem;
    font-weight: bold;
    cursor: pointer;
    text-align: right;
    display: block;
  }
  
  .input-section {
    display: flex;
    flex-direction: column;
    gap: 15px; /* 입력 필드 간 간격 */
  }
  
  .input-group {
    display: flex;
    flex-direction: column;
  }
  
  .input-label {
    margin-bottom: 5px;
    font-weight: bold;
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
    background-color: #ef5554; /* 색상 변경 */
    color: white;
    border: none;
    border-radius: 10px; /* 모서리 둥글게 */
    padding: 10px 20px; /* 패딩 조정 */
    text-align: center;
    font-size: 1.5rem; /* 글자 크기 */
    cursor: pointer;
    width: 100%; /* 너비를 자동으로 설정 */
    max-width: 300px; /* 최대 너비를 설정하여 버튼 길이를 제한 */
    height: 55px; /* 높이를 55px로 설정 */
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
  