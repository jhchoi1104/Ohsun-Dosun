<script setup>
import { reactive, ref } from 'vue';
import { useAuthStore } from '@/stores/auth.js';
import { useRouter } from 'vue-router';

const authStore = useAuthStore(); //pinia 사용
const router = useRouter();

const member = reactive({
  username: 'mink123@naver.com',
  password: 'password123',
});

const login = async () => {
  console.log(member);
  try {
    //로그인 시 AIP호출
    const result = await authStore.login({
      username: member.username,
      password: member.password,
    });

    // API 응답 데이터 확인
    console.log('로그인 성공 응답 데이터:', result);

    //로그인 성공 시 chat 페이지로 이동하는 메소드
    const path = router.currentRoute.value.query.redirect || '/chat';
    router.push(path);
  } catch (e) {
    console.log('에러=====', e);
  }
};

//패스워드 보이기/감추기 상태 메서드
const passwordHidden = ref(true);
const togglePasswordVisibility = () => {
  passwordHidden.value = !passwordHidden.value;
};

//패스워드 입력 시 아이콘 상태 변경
const passwordHiddenshow = ref(false);
const togglePasswordShow = () => {
  passwordHiddenshow.value = member.password.length > 0;
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
        <form @submit.prevent="login">
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
          <div class="password-container">
            <label class="form-label">비밀번호</label>
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
          <div class="kakao">
            <button class="kakao-form">
              <i class="fa-brands fa-facebook-messenger"></i> 카카오 로그인
            </button>
          </div>
          <div class="find">아이디 찾기 &vert; 비밀번호 찾기</div>
          <button class="login-form">로그인</button>
        </form>
        <div class="join">
          <RouterLink class="join-form" to="/join"> 회원가입 </RouterLink>
        </div>
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
.kakao-form {
  background-color: #fee500;
  color: black;
  border: none;
  border-radius: 5px;
  margin-top: 10%;
  width: 100%;
  padding: 5px;
  font-size: 20px;
}
.find {
  margin-top: 25%;
  color: gray;
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
.join {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 10px;
}
.join-form {
  text-decoration: none; /* 밑줄 제거 */
  color: gray;
  font-size: 20px;
}
</style>
