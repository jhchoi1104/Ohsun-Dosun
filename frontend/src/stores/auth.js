import { computed, ref } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';
import { useRouter } from 'vue-router'; // router import

const initState = {
  username: '',
  Password: '',
  token: '', //접근 토큰
  userId: null,
};

export const useAuthStore = defineStore('auth', () => {
  const state = ref({ ...initState });

  const router = useRouter();

  const isLogin = computed(() => !!state.value.username); //로그인 상태.

  const username = computed(() => state.value.username);
  const password = computed(() => state.value.Password);

  const load = () => {
    const auth = localStorage.getItem('auth');
    console.log(auth);
    if (auth != null) {
      state.value = JSON.parse(auth);
      console.log('localStorage에서 불러온 정보:', state.value);
    }
  };

  // 카카오 로그인 처리
  const loginWithKakao = async () => {
    console.log('loginWithKakao 호출됨');
    // URLSearchParams 사용하여 데이터를 URL 인코딩 형식으로 변환

    try {
      const response = await axios.get('http://localhost:8080/kakao/userInfo', {
        withCredentials: true, // 세션 쿠키를 서버와 공유
      });
      console.log('응답 전체:', response);
      console.log('카카오 로그인 성공:', response.data);

      // 사용자 정보를 authStore에 저장
      state.value = { ...response.data };
      // 카카오에서 받은 데이터가 올바르게 처리되는지 확인
      console.log('사용자정보:', state.value);

      // 로그인 성공 후 로컬 스토리지에 저장
      localStorage.setItem('auth', JSON.stringify(state.value));
      console.log('로그인 상태:', isLogin.value);
      console.log(
        '로컬 스토리지에 저장된 데이터:',
        localStorage.getItem('auth')
      );
      // 페이지 이동
      router.push('/chat'); // 성공 시 리다이렉트
    } catch (error) {
      console.error('카카오 로그인 실패:', error);
      throw new Error('카카오 로그인에 실패했습니다. 다시 시도해주세요.');
    }
  };

  const login = async (member) => {
    console.log(member);

    const { data } = await axios.post('/api/auth/login', member);
    state.value = { ...data };

    localStorage.setItem('auth', JSON.stringify(state.value));

    console.log('로그인 상태:', isLogin.value);
    return data; //추가 : 로그인 결과 반환
  };

  const logout = () => {
    if (window.Kakao && window.Kakao.isInitialized()) {
      // 카카오 로그인 상태 확인
      if (window.Kakao.Auth.getAccessToken()) {
        // 카카오 로그아웃 처리
        window.Kakao.Auth.logout(() => {
          console.log('카카오 로그아웃 성공');
          // 2. 일반 로그아웃 처리
          localStorage.clear();
          state.value = { ...initState }; // 상태 초기화
        });
      } else {
        console.log('카카오 로그인 상태가 아닙니다.');
        // 로그인 상태가 아니면 일반 로그아웃만 처리
        localStorage.clear();
        state.value = { ...initState }; // 상태 초기화
      }
    } else {
      console.error('Kakao API가 로드되지 않았습니다.');
      // 카카오 API가 로드되지 않은 경우 일반 로그아웃 처리
      localStorage.clear();
      state.value = { ...initState }; // 상태 초기화
    }
  };

  const getToken = () => state.value.token;

  const setUserId = (id) => {
    state.value.userId = id; //동적으로 userId 설정
    localStorage.setItem('auth', JSON.stringify(state.value)); // 업데이트된 userid 저장
  };

  load();

  return {
    state,
    isLogin,
    username,
    password,
    login,
    logout,
    getToken,
    setUserId,
  };
});
