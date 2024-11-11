<template>
    <Navbar />
    <div class="container">
      <div class="d-flex" id="top">
        <div class="chatbot">
          <img src="../../assets/챗봇.png" alt="챗봇 아이콘" class="icon">
            챗봇
        </div>
        <div class="call">
          <img src="../../assets/상담사.png" alt="상담사 아이콘" class="icon">
            상담사
        </div>
      </div>
      <div class="d-flex flex-column" id="middle">
          <div class="middle-item">
            <img src="../../assets/계좌내역.png" alt="계좌 아이콘" class="icon">
            계좌 내역 조회
            <span class="arrow">></span>
          </div>
          <div class="middle-item">
            <img src="../../assets/송금내역.png" alt="송금 아이콘" class="icon">
            송금 내역 조회
            <span class="arrow">></span>
          </div>
          <div class="middle-item">
            <img src="../../assets/대출내역.png" alt="대출 아이콘" class="icon">
            대출 내역 조회
            <span class="arrow">></span>
          </div>
          <div class="middle-item">
            <img src="../../assets/챗봇.png" alt="챗봇 아이콘" class="icon">
            챗봇 내역 조회
            <span class="arrow">></span>
          </div>
          <div class="middle-item">
            <img src="../../assets/환경설정.png" alt="환경 설정 아이콘" class="icon">
            환경 설정
            <span class="arrow">></span>
          </div>
        </div>
      <div class="d-flex"id="bottom">
        <div class="ars">
          <div class="bottom-item">
          <img src="../../assets/ars.png" alt="ars 아이콘" class="icon">사고신고 전화(ARS)
          </div>
          <div class="d-flex">
            <div class="ars1">
              <img src="../../assets/전화기.png" alt="전화기 아이콘" class="bottom-icon">
                1588-9999
            </div>
            <div class="ars2">
              <img src="../../assets/전화기.png" alt="전화기 아이콘" class="bottom-icon">
                1599-9999
            </div>
          </div>
        </div>
      </div>
    </div>
    </template>
    <script setup>
    import Navbar from '../../components/Navbar.vue';
    </script>
    <style>
    .container{
      background-color: #FFF5F2;
    }
    #top{
      padding-top: 10px;
      padding-bottom: 10px;
    }
    .chatbot, .call {
      background-color: #FFFFFF;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 15px 25px; 
      width: 80%; 
      white-space: nowrap;
    }
    .chatbot {
      margin-right: 20px; 
    }
    #middle {
      border-radius: 10px;
      background-color: #FFFFFF;
      padding: 10px;
    }
    .middle-item {
      display: flex;
      align-items: center;
      padding: 15px;
      border-bottom: 1px solid #E5E5E5;
    }
    .middle-item:last-child {
      border-bottom: none;
    }
    .icon {
      width: 24px;
      height: 24px;
      margin-right: 15px;
    }
    .arrow {
      margin-left: auto;
      color: #C0C0C0;
    }
    #bottom{
      background-color: #FFFFFF;
      margin-top: 10px;
      margin-bottom:10px;
    }
    .bottom-item{
      padding-bottom: 10px;
    }
    .ars {
      background-color: #FFFFFF;
      border-radius: 10px; 
      padding: 15px; 
    }
    .ars1, .ars2 {
      background-color: #FFFFFF;
      border:groove black 1px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 8px 15px; 
      width: 60%;
      white-space: nowrap;
      font-size: 12px;
    }
    .ars1 {
      margin-right: 20px; 
    }
    .bottom-icon {
      width: 14px;
      height: 14px;
      margin-right: 10px;
    }
    </style>
    