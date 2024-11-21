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
                <div id="star-btn" class="justify-content-end">
                  <img src="@/assets/images/emptystar.png" alt="" />
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
</template>

<script setup>
import '@/assets/main.css';
import Header from '@/components/Header.vue';
import { useInputStore } from '@/stores/inputStore'; // 스토어 임포트

const inputStore = useInputStore(); // 스토어 인스턴스 생성
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  /* border: 1px solid blue; */
  height: 100vh;
  position: relative;
}
.confirm-form-container {
  display: flex;
  align-items: center;
  justify-content: center;
  /* border: 1px solid green; */
  width: 100%;
  height: 50%;
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
  width: 100%;
  text-decoration: none;
}
</style>
