<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

const member = reactive({
  username: '',
  password: '',
  password2: '',
  name: '',
  gender: '',
  ssnFirst: '', // 주민등록번호 앞자리
  ssnSecond: '', // 주민등록번호 뒷자리
  ssn: '',
});

const usernameValid = ref(true);

const checkUsername = async () => {
  // 이메일 형식 확인
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!emailPattern.test(member.username)) {
    alert('유효한 이메일 주소를 입력해주세요.');
    return; // 이메일 형식이 맞지 않으면 함수 종료
  }
  try {
    const response = await axios.get(`/api/member/${member.username}`);
    console.log(response.data);
    if (response.data.exists) {
      usernameValid.value = true; // 사용 가능한 아이디
      alert('사용 가능한 아이디입니다.');
    } else {
      usernameValid.value = false; // 사용 중인 아이디
      alert('이미 사용 중인 아이디입니다.');
    }
  } catch (error) {
    console.error(error);
    // 백엔드에서 NoSuchElementException 발생 시 사용 가능한 아이디 처리
    usernameValid.value = true; // 사용 가능한 아이디
    alert('사용 가능한 아이디입니다.');
  }
  console.log(usernameValid.value);
};

const join = async () => {
  if (!usernameValid.value) {
    return alert('아이디 확인을 먼저 해주세요.'); // 아이디 확인을 하지 않은 경우 경고
  }

  if (member.password != member.password2) {
    return alert('비밀번호가 다릅니다.');
  }

  console.log('회원가입 정보:', member);
  try {
    //회원가입 정보 전송
    const { data } = await axios.post('/api/member', member);
    localStorage.setItem('join', JSON.stringify(data));

    router.push({ name: 'Chat' });
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

//나이 목록
const ages = ref([]); // 나이 리스트

// 3~150까지 나이 초기화
const initializeAges = () => {
  for (let i = 3; i <= 150; i++) {
    ages.value.push(i);
  }
};
initializeAges();

//주민등록번호 규칙
const ssnWarning = ref('');

const formatSsn = () => {
  // 한글이 포함되어 있는지 확인
  if (
    /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/.test(member.ssnFirst) ||
    /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/.test(member.ssnSecond)
  ) {
    ssnWarning.value = '숫자만 입력 가능합니다!';
  } else {
    ssnWarning.value = ''; //경고 메시지 초기화
  }
  // 숫자만 남기고 최대 길이 제한
  member.ssnFirst = member.ssnFirst.replace(/[^0-9]/g, '').slice(0, 6);
  member.ssnSecond = member.ssnSecond.replace(/[^0-9]/g, '').slice(0, 7);
  member.ssn = `${member.ssnFirst}-${member.ssnSecond}`;
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
      <div class="titlejoin">회원가입</div>
      <div class="login mt-2">
        <form @submit.prevent="join">
          <!-- 아이디 입력 -->
          <div class="mb-2">
            <label class="form-label">아이디</label>
            <div style="display: flex; align-items: center">
              <input
                class="form-input"
                type="email"
                v-model="member.username"
                placeholder=" 이메일을 입력하세요."
              />
              <button
                type="button"
                @click="checkUsername"
                :disabled="!member.username"
                style="margin-left: 10px"
              >
                아이디 확인
              </button>
            </div>
            <div v-if="!usernameValid" class="warning-message">
              아이디가 이미 존재합니다.
            </div>
          </div>
          <!-- 비밀번호 입력 -->
          <div class="password-container mb-2">
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
          <div class="password-container mt-2">
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
          <!--이름입력-->
          <div class="name mt-2">
            <label class="form-label">이름</label>
            <input
              class="form-input"
              type="text"
              v-model="member.name"
              maxlength="100"
              placeholder="이름을 입력해주세요."
            />
          </div>
          <!-- 나이 입력 -->
          <div class="age mt-2">
            <label class="form-label">나이</label>
            <div class="form-select-wrapper">
              <select
                class="form-select"
                v-model="member.age"
                aria-label="나이 선택"
              >
                <option value="" disabled>나이를 선택해주세요</option>
                <option v-for="age in ages" :key="age" :value="age">
                  {{ age }}
                </option>
              </select>
            </div>
          </div>
          <!-- 성별 입력 -->
          <div class="gender mt-2">
            <label class="form-label">성별</label>
            <div class="form-radio-group">
              <label
                class="form-radio"
                style="margin-right: 5%; margin-left: 2%"
              >
                <input type="radio" v-model="member.gender" value="M" /> 남성
              </label>
              <label class="form-radio" style="margin-right: 5%">
                <input type="radio" v-model="member.gender" value="F" />
                여성
              </label>
              <label class="form-radio">
                <input type="radio" v-model="member.gender" value="O" />
                기타
              </label>
            </div>
          </div>
          <!-- 주민등록번호 입력 -->
          <div class="ssn mt-2">
            <label class="form-label">주민등록번호</label>
            <div class="d-flex">
              <!-- 6자리 입력 폼 -->
              <input
                class="ssn-form-input"
                type="text"
                v-model="member.ssnFirst"
                placeholder="앞자리 6자리"
                @input="formatSsn"
              />
              <span class="hyphen">-</span>
              <!-- 하이픈 표시 -->
              <!-- 7자리 입력 폼 -->
              <input
                class="ssn-form-input"
                type="text"
                v-model="member.ssnSecond"
                placeholder="뒷자리 7자리"
                @input="formatSsn"
              />
            </div>
            <div v-if="ssnWarning" class="warning-message">
              {{ ssnWarning }}
            </div>
          </div>

          <button class="login-form mb-4">회원가입</button>
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
.sub-container {
  width: 100%;
  max-width: 400px;
  padding: 0 10px;
}
.titlejoin {
  text-align: center;
  font-weight: bold;
  font-size: 35px;
}
.form-label {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  font-weight: bold;
  font-size: 20px;
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
.ssn-form-input {
  border-radius: 8px; /*모서리 둥글게*/
  width: 40%;
  height: 50px;
  font-size: 30px;
  padding-bottom: 2%;
}
.ssn-form-input::placeholder {
  font-size: 20px; /* 원하는 크기로 변경 */
  text-indent: 2%;
}
.hyphen {
  display: inline-block;
  width: 4%; /* 하이픈의 너비 */
  line-height: 50px; /* 세로 가운데 정렬 */
  font-size: 30px; /* 하이픈 크기 */
  margin: 0 5px; /* 양쪽 간격 */
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
.warning-message {
  background-color: #f8d7da; /* 연한 빨간 배경 */
  color: #721c24; /* 짙은 빨간 텍스트 */
  border: 1px solid #f5c6cb; /* 빨간색 테두리 */
  border-radius: 5px; /* 둥근 모서리 */
  padding: 10px; /* 내부 여백 */
  margin-top: 8px; /* 위쪽 여백 */
  font-size: 0.9rem; /* 글자 크기 */
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 약간의 그림자 */
}
</style>
