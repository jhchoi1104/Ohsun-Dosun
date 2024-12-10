<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import authApi from '@/api/authAPI.js';

const router = useRouter();

const member = reactive({
  username: '',
  password: '',
  password2: '',
});

const join = async () => {
  if (member.password != member.password2) {
    return alert('비밀번호가 다릅니다.');
  }

  console.log('회원가입 정보:', member);
  try {
    //회원가입 정보 전송
    await authApi.create(member);
    localStorage.setItem('username', member.username); // username 저장
    localStorage.setItem('password', member.password); // password 저장
    router.push({ name: 'Main' });
  } catch (e) {
    console.error(e);
  }
};

//패스워드 보이기/감추기 상태 메서드
const passwordHidden = ref(true);
const togglePasswordVisibility = () => {
  passwordHidden.value = !passwordHidden.value;
};
const passwordHidden2 = ref(true);
const togglePasswordVisibility2 = () => {
  passwordHidden2.value = !passwordHidden2.value;
};

//패스워드 입력 시 패스워드 보이기/감추기 상태 메서드 보임
const passwordHiddenshow = ref(false);
const togglePasswordShow = () => {
  passwordHiddenshow.value = member.password.length > 0;
};
//패스워드 확인 입력 시 패스워드 보이기/감추기 상태 메서드 보임
const passwordHiddenshow2 = ref(false);
const togglePasswordShow2 = () => {
  passwordHiddenshow2.value = member.password2.length > 0;
};
</script>

<template>
  <div class="main-container">
    <div class="sub-container">
      <div class="header">
        <RouterLink to="/">
          <img
            src="@/assets/images/ohsundosunlogo.png"
            alt="logo"
            style="max-height: 70px"
          />
        </RouterLink>
      </div>
      <div class="login mt-4">
        <form @submit.prevent="join">
          <!-- 아이디 입력 -->
          <div class="mb-4">
            <label class="form-label">아이디</label>
            <input
              class="form-input"
              type="email"
              v-model="member.username"
              placeholder=" 이메일을 입력하세요."
            />
          </div>
          <!-- 비밀번호 입력 -->
          <div class="password-container mb-4">
            <label class="form-label" for="password">비밀번호</label>
            <input
              class="form-input"
              :type="passwordHidden ? 'password' : 'text'"
              v-model="member.password"
              @input="togglePasswordShow"
              placeholder=" 비밀번호를 입력하세요"
            />
            <span
              v-if="passwordHiddenshow"
              :class="
                passwordHidden ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'
              "
              @click="togglePasswordVisibility"
              class="password-icon"
              style="
                position: absolute;
                right: 10px;
                top: 50%;
                transform: translateY(-50%);
                cursor: pointer;
              "
            ></span>
          </div>
          <!-- 비밀번호 확인 -->
          <div class="password-container">
            <label class="form-label" for="password2">비밀번호 확인</label>
            <input
              class="form-input"
              :type="passwordHidden2 ? 'password' : 'text'"
              v-model="member.password2"
              @input="togglePasswordShow2"
              placeholder=" 비밀번호를 입력하세요"
            />
            <span
              v-if="passwordHiddenshow2"
              :class="
                passwordHidden2 ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'
              "
              @click="togglePasswordVisibility2"
              class="password-icon"
              style="
                position: absolute;
                right: 10px;
                top: 50%;
                transform: translateY(-50%);
                cursor: pointer;
              "
            ></span>
          </div>
          <button class="login-form">회원가입</button>
        </form>
      </div>
    </div>
  </div>
</template>

<style>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between; /* 요소 간의 공간을 균등하게 분배 */
  height: 100vh; /* 전체 화면 높이에 맞춤 */
}
.form-label {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  font-weight: bold;
  font-size: 25px;
}
.form-input {
  border-radius: 4px; /*모서리 둥글게*/
  width: 100%;
  height: 50px;
  font-size: 35px;
}
.password-container {
  position: relative; /* 필요한 위치 지정 */
}
.password-icon {
  position: absolute;
  margin-top: 23px; /* 위쪽으로 30px 이동 */
  right: 10px;
  cursor: pointer;
}
.login-form {
  background-color: #ef5554;
  color: white;
  border: none;
  border-radius: 5px;
  margin-top: 10%;
  width: 100%;
  padding: 10px;
  font-size: 20px;
  font-weight: bold;
}
</style>
