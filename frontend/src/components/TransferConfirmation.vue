<template>
  <Header />
  <div class="main-container">
    <div class="confirm-form-container">
      <div class="confirm-form">
        <div class="d-flex flex-column">
          <div class="p-2">
            <div id="bank-to-bank">
              <div class="d-flex justify-content-center">
                <div>
                  <img src="@/assets/images/kblogo.png" alt="KB국민" />
                </div>
                <div>
                  <div id="flow">
                    <img src="@/assets/images/go.gif" alt="" />
                  </div>
                </div>
                <div>
                  <img
                    src="@/assets/images/woorilogo.png"
                    alt="우리"
                    style="width: 45px; height: 45px"
                  />
                </div>
              </div>
            </div>
          </div>
          <div class="p-2">
            <div id="transfer-to">{{ inputs.name }}님께</div>
            <div id="transfer-amount">{{ inputs.amount }}원을 송금합니다.</div>
          </div>
          <div class="p-2">
            <div id="account-number">
              {{ inputs.recieveraccountbank }}
              {{ inputs.recieveraccountnumber }}
            </div>
          </div>
          <div class="p-2">
            <div id="caution-message">
              <img src="@/assets/images/warningicon.png" alt="" />
              입력한 계좌로 최근에 이체한 기록이 없어요
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="button-section">
      <router-link class="chat-button" to="/password">송금하기</router-link>
    </div>
  </div>
</template>

<script setup>
import { useInputStore } from '@/stores/inputStore'; // Pinia 스토어 가져오기
import '@/assets/main.css';
import Header from '@/components/Header.vue';

const inputStore = useInputStore(); // 스토어 인스턴스 생성
const inputs = inputStore.inputs; // 스토어의 inputs 상태 가져오기
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  position: relative;
}
.confirm-form-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 50%;
  line-height: normal;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
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
#bank-to-bank img {
  width: 40px;
  height: 40px;
}
#flow img {
  width: 20px;
  height: 20px;
  margin-right: 15px;
  margin-left: 15px;
}
#transfer-to {
  font-weight: bold;
  font-size: 1.4rem;
  margin-bottom: 8px;
}
#transfer-amount {
  font-size: 1.4rem;
}
#caution-message {
  width: 100%;
  background-color: #f5f5f5;
  padding: 5px;
  border-radius: 3px;
  align-items: center;
}
#caution-message img {
  width: 20px;
  height: 20px;
}
.button-section {
  position: absolute;
  bottom: 30px;
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
  text-decoration: none;
}
</style>
