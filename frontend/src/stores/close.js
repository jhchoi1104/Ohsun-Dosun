import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useMenuStore = defineStore('menu', () => {
  const isNavShow = ref(false);

  const toggleNavShow = () => {
    isNavShow.value = !isNavShow.value;
  };

  const closeNav = () => {
    isNavShow.value = false;
  };

  return {
    isNavShow,
    toggleNavShow,
    closeNav,
  };
});