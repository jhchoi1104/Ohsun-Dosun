import { computed, ref } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';

const initState = {
  username: '',
  Password: '',
  token: '', //접근 토큰
};

export const useAuthStore = defineStore('auth', () => {
  const state = ref({ ...initState });

  const isLogin = computed(() => !!state.value.username); //로그인 상태.

  const username = computed(() => state.value.username);
  const password = computed(() => state.value.Password);

  const load = () => {
    const auth = localStorage.getItem('auth');
    console.log();
    if (auth != null) {
      state.value = JSON.parse(auth);
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
    localStorage.clear();
    state.value = { ...initState };
    chat.setMessages([]);
  };

  const getToken = () => state.value.token;

  load();

  return { state, isLogin, username, password, login, logout, getToken };
});
