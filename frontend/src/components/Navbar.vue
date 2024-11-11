<template>
    <nav class="navbar navbar-light bg-light">
      <div class="container-fluid">
        <span class="navbar-brand" href="#" id="logotext" @click="goToPage('/')">
          오순도순
          <img src="../assets/로고이미지.png" alt="" width="30" height="24" class="d-inline-block align-text-top">
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
  
  
  <style>
  #logotext{
      color:red;
      cursor:pointer;
  } 
  #menu{
    background-color: #FFFFFF;
    border-style:groove;
    color: black;
    padding:8px 20px;
  }
  </style>