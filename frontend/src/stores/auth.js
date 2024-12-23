import { computed, ref } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';
import { useRouter } from 'vue-router'; // router import

const initState = {
  username: '',
  Password: '',
  token: '', //접근 토큰
};

export const useAuthStore = defineStore('auth', () => {
  const state = ref({ ...initState });
  const router = useRouter();

  const isLogin = computed(() => !!state.value.username); //로그인 상태.

  const username = computed(() => state.value.username);
  const password = computed(() => state.value.Password);

  const load = () => {
    const auth = localStorage.getItem('auth');
    console.log();
    if (auth != null) {
      state.value = JSON.parse(auth);
      console.log('localStorage에서 불러온 정보:', state.value);
    }
  };

  // 카카오 로그인 처리
  const loginWithKakao = async (code) => {
    console.log('loginWithKakao 호출됨');
    // URLSearchParams 사용하여 데이터를 URL 인코딩 형식으로 변환
    const params = new URLSearchParams();
    params.append('code', code);
    try {
      const response = await axios.post(
        `http://localhost:8080/kakao/callback`,
        params, // 서버에 보내는 데이터는 인증 코드입니다.
        {
          withCredentials: true,
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
          },
        }
      );
      console.log('응답 전체:', response);
      console.log('카카오 로그인 성공:', response.data);

      // 사용자 정보를 authStore에 저장
      state.value = { ...response.data };

      // 로그인 성공 후 로컬 스토리지에 저장
      localStorage.setItem('auth', JSON.stringify(state.value));
      console.log('사용자정보:', state.value);

      // 페이지 이동
      router.push('/chat'); // 성공 시 리다이렉트
      //return response.data; // 로그인 성공 후 데이터 반환
      // 리다이렉트 URL로 페이지 이동
      // if (response.data.redirectUrl) {
      //   console.log('리다이렉트 URL:', response.data.redirectUrl); // 리다이렉트 URL 확인용
      //   window.location.href = response.data.redirectUrl; // 리다이렉트
      // } else {
      //   console.error('redirectUrl이 응답에 포함되지 않았습니다.');
      // }
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
    if (window.Kakao) {
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

  load();

  return {
    state,
    isLogin,
    username,
    password,
    login,
    logout,
    getToken,
    loginWithKakao,
  };
});
