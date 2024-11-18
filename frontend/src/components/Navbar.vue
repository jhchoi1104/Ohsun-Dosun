<template>
  <nav class="navbar navbar-light bg-light">
    <div class="container-fluid">
      <span class="navbar-brand" id="logotext" @click="goToPage('/')">
        오순도순
        <img src="../assets/로고이미지.png" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
      </span>
      <button @click="goToPage('/list')" id="menu" type="button">
        조회하기
      </button>
    </div>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const goToPage = (path) => {
  router.push(path);
};
</script>

<style scoped>
#logotext {
  color: red;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 1.2rem;
}

#menu {
  background-color: #FFFFFF;
  border: 1px solid #ccc;
  color: black;
  padding: 8px 20px; /* padding을 축소하여 버튼 크기를 조정 */
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
}

.container-fluid {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px; /* 전체 nav바 padding 조정 */
}
</style>
