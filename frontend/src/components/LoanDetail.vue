<template>
  <div class="container">
    <p>아래 버튼을 누르시면 대출에 대한 설명이 들립니다.</p>
    <div class="d-flex justify-content-between" alt="1">
      <div class="flex-item">
        <button class="btn" @click="playAudio('주택을 담보로 신청 가능한 대출 상품입니다.')">
          주택 담보 대출🔊
        </button>
      </div>
      <div class="flex-item">
        <button class="btn" @click="playAudio('고객님의 전세금을 지원하는 대출입니다.')">
          전세 자금 대출🔊
        </button>
      </div>
    </div>
    <div class="d-flex justify-content-between" alt="2">
      <div class="flex-item">
        <button class="btn" @click="playAudio('신차 및 중고차 구매를 위한 제품입니다.')">
          자동차 구입 대출🔊
        </button>
       </div>
      <div class="flex-item">
        <button class="btn" @click="playAudio('신용도에 기반한 대출입니다.')">신용 대출🔊</button>
       </div>
    </div>
    <div class="d-flex justify-content-between" alt="3">
      <div class="flex-item">
        <button class="btn" @click="playAudio('중도 해지의 불이익이 없는 예금 담보 대출입니다.')">
          예금 담보 대출🔊
        </button>
      </div>
      <div class="flex-item">
        <button class="btn" @click="playAudio('비대면으로 간편하게 신청하고 바로 사용하는 kb 비상금 대출입니다.')">
          KB 비상금 대출🔊
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { bringAudioFromServer } from '@/api/TtsApi';

let audio = null;

const playAudio = async (message) => {
  try {
    // 기존 재생 중인 오디오 멈추기
    if (audio && !audio.paused) {
      audio.pause();
      audio.currentTime = 0;
    }

    // TTS API를 통해 음성 데이터를 가져오기
    const base64Audio = await bringAudioFromServer(message);

    // Base64 디코딩 및 Blob 생성
    const byteCharacters = atob(base64Audio);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: 'audio/mp3' });
    const audioUrl = URL.createObjectURL(blob);

    // 오디오 객체 생성 및 재생
    audio = new Audio(audioUrl);
    audio.play();
  } catch (error) {
    console.error('음성 재생 오류:', error);
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
  width: 100%;
  height: 250px;
  text-align: center;
  gap: 15px; /* 행 간 너비 줄이기 */
  background-color: white;
}
.btn {
  border: none;
  background-color: rgba(244, 160, 139, 0.486);
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}
.btn:hover {
  background-color: rgba(244, 160, 139, 0.7);
}
/* 한 행 */
.d-flex {
  display: flex;
  gap: 15px;
  justify-content: center;
  align-items: center;
}

.flex-item {
  flex: 1; /* 각 아이템이 같은 비율로 공간을 차지하도록 설정 */
  margin: 5px; /* 아이템 간의 간격을 추가 */
}

button {
  font-size: 14px;
  width: 135px; /* 버튼이 div의 너비를 가득 채우도록 설정 */
  height: 35px; /* 버튼의 높이를 고정 */
}
</style>
