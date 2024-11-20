<template>
    <div :class="['inputform-modal', { hidden: !show }]">
      <div class="inputform-container">
        <span class="close-button" @click="$emit('close')">&times;</span>
        <div class="input-section">
          <div v-for="(input, index) in dynamicInputs" :key="index" class="input-group" v-if="props.step !== 3 && props.step !== 5">
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
          <div v-if="props.step === 5" class="input-group">
            <label for="account-select" class="input-label">계좌 번호 선택</label>
            <select id="account-select" v-model="selectedAccount" class="input-field">
              <option v-for="(account, index) in accountOptions" :key="index" :value="account">
                {{ account }}
              </option>
            </select>
          </div>
        </div>
        <div class="button-section">
          <router-link class="chat-button" to="/chat">확인</router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineProps, defineEmits, computed, ref } from 'vue';
  
  const props = defineProps({
    show: {
      type: Boolean,
      required: true,
    },
    step: {
      type: Number,
      default: 5,
      required: true,
    },
  });
  
  const emit = defineEmits(['close']);
  
  const selectedAccount = ref('');
  
  const accountOptions = ['계좌 1', '계좌 2', '계좌 3'];
  
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
    } else if(props.step === 4) {
      return [
        { type: 'text', placeholder: '계좌 번호 선택', value: '', label: '계좌 번호 선택' },
        { type: 'text', placeholder: '이름', value: '', label: '수령인 이름 입력' },
      ];
    } else if(props.step === 5) {
      return [];
    } else if(props.step === 6) {
      return [
        { type: 'text', placeholder: '계좌 번호 선택', value: '', label: '계좌 번호' },
        { type: 'text', placeholder: '주소', value: '', label: '주소 입력' },
        { type: 'text', placeholder: '전화 번호', value: '', label: '전화번호 입력' },
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
    height: 60vh;
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
    gap: 15px;
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
  