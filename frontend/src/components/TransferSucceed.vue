<template>
  <div class="main-container">
    <div class="confirm-form-container">
      <div class="confirm-form">
        <div class="d-flex flex-column">
          <div class="p-2">
            <iframe
              src="https://giphy.com/embed/grQgrKjbnzo7s29qGj"
              width="80"
              height="80"
              style=""
              frameBorder="0"
              class="giphy-embed"
              allowFullScreen
            ></iframe>
          </div>
          <div class="p-2">
            <!-- XX님께 -->
            <div id="transfer-to">XX님께</div>
            <!-- XX원-->
            <div id="transfer-amount">XX원</div>
            <!-- 이체가 완료되었습니다. -->
            <div id="success-msg">이체가 완료되었습니다.</div>
          </div>
          <div class="p-2">
            <!-- 송금한 계좌 -->
            <div id="account-number">
              <div class="d-flex">
                <div class="flex-grow-1">국민 618701-04-192595</div>
                <div id="star-btn" class="justify-content-end">
                  <img src="@/assets/images/emptystar.png" alt="" />
                  <!-- <img src="@/assets/images/fullstar.png" alt="" /> -->
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="button-section">
      <router-link class="chat-button" to="/chat">시작하기</router-link>
    </div>
  </div>
</template>

<script>
import '@/assets/main.css';
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
