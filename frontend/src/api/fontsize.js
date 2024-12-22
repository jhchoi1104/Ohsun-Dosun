import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useFontSizeStore = defineStore('fontSize', () => {
  const fontSize = ref(localStorage.getItem('fontSize') || 'medium');

  const setFontSize = (size) => {
    fontSize.value = size;
    localStorage.setItem('fontSize', size);
  };

  // 폰트 크기 매핑
  const getFontSizeValue = () => {
    switch (fontSize.value) {
      case 'small':
        return '1.2rem';
      case 'medium':
        return '1.5rem';
      case 'large':
        return '1.8rem';
      default:
        return '1.5rem'; // 기본값
    }
  };

  return { fontSize, setFontSize, getFontSizeValue };
});