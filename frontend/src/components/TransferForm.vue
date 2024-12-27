<template>
  <div class="transfer-inputform">
    <button class="close-button" @click="$emit('close')">×</button>
    <div class="input-section">
      <div
        v-for="input in dynamicInputs"
        :key="input.field"
        class="input-group"
      >
        <label :for="input.field">{{ input.label }}</label>
        <input
          :type="input.type"
          :id="input.field"
          class="text-input"
          v-model="input.value"
          :placeholder="input.placeholder"
          style="border-radius: 5px"
        />
      </div>
      <div class="button-section">
        <button class="chat-button" @click="handleSaveAndNext">확인</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useInputStore } from '@/stores/inputStore';
import { useRouter } from 'vue-router';
const router = useRouter();
const emit = defineEmits();
const inputStore = useInputStore();
const dynamicInputs = computed(() => {
  //const currentStep = inputStore.inputs.step; // Pinia 스토어에서 step 값 가져오기
  if (inputStore.inputs.step <= 4) {
    return [
      {
        type: 'text',
        placeholder: '송금 받는 사람 이름',
        value: inputStore.inputs.name || '', // 사용자가 제공한 이름
        label: '이름',
        field: 'name',
      },
      {
        type: 'text',
        placeholder: '송금 계좌번호의 은행명',
        value: inputStore.inputs.recieveraccountbank || '', // 사용자가 제공한 은행명
        label: '은행명',
        field: 'recieveraccountbank',
      },
      {
        type: 'text',
        placeholder: '송금할 계좌번호',
        value: inputStore.inputs.recieveraccountnumber || '', // 사용자가 제공한 계좌번호
        label: '계좌번호',
        field: 'recieveraccountnumber',
      },
    ];
  }

  // Step 6의 입력 필드 설정
  if (inputStore.inputs.step === 6) {
    return [
      {
        type: 'text',
        placeholder: '송금 금액',
        value: inputStore.inputs.amount || '', // 사용자가 제공한 금액
        label: '금액',
        field: 'amount',
      },
    ];
  }

  return []; // 다른 경우에는 빈 배열 반환
});

const saveInputs = () => {
  dynamicInputs.value.forEach((input) => {
    inputStore.updateInput(input.field, input.value);
  });
  console.log('Saved inputs:', inputStore.$state); // 전역 상태 확인
};

// ** 송금 요청 보내는 함수(사용자가 확인 버튼 누르면 함수 호출)
// const handleSendMoney = async () => {
//   try {
//     const response = await axios.post('/api/transfer/send', {
//       senderId: inputStore.inputs.senderid,
//       receiverId: inputStore.inputs.recieverid,
//       receiverAccountBank: inputStore.inputs.recieveraccountbank,
//       receiverAccountNumber: inputStore.inputs.recieveraccountnumber,
//       // ** amount: inputStore.inputs.amount,
//       amount: parseInt(inputStore.inputs.amount), // 금액은 정수로 변환
//     });
//     console.log(response.data); // "transfer succeed" 메시지 확인
//     // 송금 성공 후 TransferSucceed.vue로 이동
//     router.push('/transferSucceed');
//   } catch (error) {
//     console.error('송금 실패:', error);
//     alert('송금 처리 중 오류가 발생했습니다.');
//   }
// };

const nextStep = () => {
  //const currentStep = inputStore.inputs.step; // 현재 스텝을 가져옴
  if (inputStore.inputs.step < 6) {
    inputStore.inputs.step = 6; // 로컬 상태 업데이트
    inputStore.updateInput('step', inputStore.inputs.step);
  } else {
    emit('close'); // 모달 닫기
    saveInputs(); // ** 입력값 저장
    // handleSendMoney(); // ** 송금 요청 전송
    console.log('inputs' + inputStore.inputs);
    inputStore.inputs.step = 7;
    inputStore.updateInput('step', inputStore.inputs.step);
    // TransferConfirmation.vue 호출

    // location.reload(); // 페이지 새로고침
    // localStorage에 step 값 저장
    router.push('/transferConfirm');
  }
};

const handleSaveAndNext = () => {
  saveInputs(); // 입력값 저장
  nextStep(); // 다음 단계로 이동
};
</script>

<style scoped>
.transfer-inputform {
  width: calc(100vh * 9 / 16);
  max-width: 400px;
  height: auto;
  max-height: 98vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  z-index: 9999;
  margin: 0 auto;
  background-color: white;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.input-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 36px;
  overflow-y: auto;
  height: calc(100% - 100px);
}

.input-group {
  margin: 20px 0;
  width: 80%;
  position: relative;
  z-index: 10000;
}

.button-section {
  position: sticky;
  bottom: 0;
  width: 100%;
  padding: 20px;
  background-color: white;
  z-index: 10001;
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
  display: block;
}

.text-input {
  width: 100%;
  padding: 10px;
  border: none;
  border-bottom: 2px solid transparent;
  border-radius: 5px;
  background-color: #f5f5f5;
  z-index: 10000;
}

.text-input:focus {
  outline: none;
  border-bottom: 2px solid #ef5554;
  transition: border-bottom-color 0.3s ease, background-color 0.3s ease;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 5px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  z-index: 10002;
}

.close-button:hover {
  background-color: rgba(0, 0, 0, 0.05);
}
</style>
