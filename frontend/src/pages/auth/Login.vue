<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth.js';
import { useRouter } from 'vue-router';
import axios from 'axios';

const authStore = useAuthStore(); //pinia 사용
const router = useRouter();

const member = reactive({
  username: '',
  password: '',
});

//일반 로그인 함수
const login = async () => {
  console.log(member);

  // 비밀번호가 비어있는 경우 처리
  if (!member.password || !member.username) {
    alert('비밀번호와 아이디를 입력해주세요.');
    return;
  }

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
    // 백엔드에서 전달하는 에러 메시지 처리
    if (e.response && e.response.data) {
      alert(e.response.data.message || '비밀번호가 틀렸습니다.');
    } else {
      alert('비밀번호를 다시 입력해주세요.');
    }
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

// 카카오 로그인
const loginWithKakao = () => {
  // 카카오 SDK가 로드된 후 호출
  if (!window.Kakao.isInitialized()) {
    alert('카카오 SDK 초기화 안 됨');
    return;
  }
  Kakao.Auth.authorize({
    redirectUri: `http://localhost:8080/kakao/callback`, //리디렉션 URL을 실제 로그인 처리하는 경로
    fail: (error) => {
      console.error('카카오 인증 실패', error);
      alert('카카오 인증에 실패했습니다. 다시 시도해주세요.');
    },
  });
};
// 페이지 로드 시 인증 코드 처리
onMounted(() => {
  if (window.location.pathname.includes('chat')) {
    // 'chat'이 포함된 모든 경로에서 callback 처리
    handlekakaocallback();
  }
});

const handlekakaocallback = async () => {
  console.log('handlekakaocallback 함수가 호출되었습니다!');
  const code = new URLSearchParams(window.location.search).get('code');
  console.log('현재 URL:', window.location.href); // 현재 URL 확인

  if (code) {
    try {
      const result = await axios.post('http://localhost:8080/kakao/callback', {
        code,
      });

      if (result.status === 200) {
        const path = router.currentRoute.value.query.redirect || '/chat';
        router.push(path);
      } else {
        alert('로그인 실패!');
      }
    } catch (error) {
      console.error('카카오 로그인 실패:', error);
      alert('카카오 로그인에 실패했습니다. 다시 시도해주세요.');
    }
  }
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
            <label class="form-label">로그인</label>
            <input
              class="form-input"
              type="email"
              v-model="member.username"
              placeholder=" 이메일을 입력하세요."
            />
          </div>
          <!-- 비밀번호 입력 -->
          <div class="password-container">
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
          <div class="find">아이디 찾기 &vert; 비밀번호 찾기</div>
          <button class="login-form">로그인</button>
        </form>

        <div class="join">
          <RouterLink class="join-form" to="/join"> 회원가입 </RouterLink>
        </div>
        <div class="simplelogin">간편 로그인</div>
        <div class="kakao mt-3">
          <a @click.prevent="loginWithKakao">
            <img
              src="@/assets/images/kakao_login_large_wide.png"
              alt="카카오 로그인"
              style="width: 100%; cursor: pointer"
            />
          </a>
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
.sub-container {
  width: 100%;
  max-width: 400px;
  padding: 0 10px;
}
.form-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-weight: bold;
  font-size: 35px;
  text-align: center;
  margin-bottom: 5%;
}
.form-input {
  border-radius: 8px; /*모서리 둥글게*/
  width: 100%;
  height: 50px;
  font-size: 30px;
  padding-bottom: 2%;
}
.form-input::placeholder {
  font-size: 20px; /* 원하는 크기로 변경 */
  text-indent: 2%;
}
.password-container {
  position: relative; /* 필요한 위치 지정 */
}
.password-icon {
  position: absolute;
  /* margin-top: 30px; 위쪽으로 30px 이동 */
  right: 10px;
  cursor: pointer;
}
.find {
  margin-top: 7%;
  color: gray;
  display: flex; /* flexbox 사용 */
  justify-content: center; /* 가로 중앙 정렬 */
  align-items: center; /* 세로 중앙 정렬 */
  font-size: 15px;
  margin-bottom: 3%;
}
.login-form {
  background-color: #ef5554;
  color: white;
  border: none;
  border-radius: 5px;
  margin-top: 5%;
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
  color: #ef5554;
  font-weight: bold;
  font-size: 20px;
  margin-top: 5%;
}
.simplelogin {
  margin-top: 15%;
  text-align: center;
  display: flex;
  align-items: center;
  color: silver;
}
.simplelogin::before,
.simplelogin::after {
  content: '';
  flex-grow: 1;
  border-top: 1px solid silver;
  margin: 0 10px;
}
</style>
