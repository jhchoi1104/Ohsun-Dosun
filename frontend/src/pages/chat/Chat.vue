<script setup>
import Header from '@/components/Header.vue';
import { ref } from 'vue';
import { sendAudioToServer} from '@/api/SttApi';  // SttApi.js에서 함수 import
import axios from 'axios';
const isRecording = ref(false);
const errorMessage = ref('');
const transcription = ref('');

// 녹음기 초기화
let mediaRecorder = null;


const startRecording = () => {
  try {
    transcription.value = ''; // 녹음 시작 시 기존 텍스트 초기화
    let audioChunks = [];

    if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
      throw new Error("마이크 권한을 요청할 수 없습니다.");
    }

    navigator.mediaDevices.getUserMedia({ audio: true })
      .then((stream) => {
        mediaRecorder = new MediaRecorder(stream);

        mediaRecorder.ondataavailable = (event) => {
          audioChunks.push(event.data);
        };

        mediaRecorder.onstop = async () => {
          const audioBlob = new Blob(audioChunks, { type: 'audio/wav' });
          const formData = new FormData();
          formData.append('file', audioBlob, 'audio.wav');

          try {
            const data = await sendAudioToServer(formData);
            transcription.value = data.text || "텍스트를 인식할 수 없습니다.";
          } catch (error) {
            errorMessage.value = "서버에 전송하는 중 오류가 발생했습니다.";
          }
        };

        mediaRecorder.start();
        isRecording.value = true;
        errorMessage.value = '';
      })
      .catch((err) => {
        console.error("녹음 시작 오류:", err);
        errorMessage.value = "마이크 권한을 확인해주세요.";
      });
  } catch (error) {
    console.error("녹음기 초기화 오류:", error);
    errorMessage.value = "녹음기를 초기화할 수 없습니다.";
  }
};


// 녹음 중지 함수
const stopRecording = () => {
  if (mediaRecorder && mediaRecorder.state === 'recording') {
    mediaRecorder.stop();
    isRecording.value = false;
  } else {
    errorMessage.value = "녹음이 진행되지 않았습니다.";
  }
};
</script>

<template>
  <Header />
  <div class="main-container">
    <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
    <p v-if="transcription">인식된 텍스트: {{ transcription }}</p>
    <div class="sub-container">
      <div id="main-character">
        <img src="@/assets/images/sooni.png" alt="" />
      </div>
    </div>
    
    <div class="button-section">
      <button class="chat-button" @click="startRecording" v-if="!isRecording">말하기</button>
      <button class="chat-button" @click="stopRecording" v-if="isRecording">중지</button>
    </div>
  </div>
</template>
<style>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between; /* 요소 간의 공간을 균등하게 분배 */
  /* border: 1px solid blue; */
  height: 100vh; /* 전체 화면 높이에 맞춤 */
  height: calc(100vh - 70px); /* 화면에서 Header를 제외한 나머지 높이 */
}

.sub-container {
  padding: 20px;
  width: 100%;
  text-align: center;
  gap: 100px;
  /* border: 1px solid red; */
}

.button-section {
  margin-bottom: 30px; /* 하단에서 30px 간격 설정 */
  width: 100%; /* 버튼 섹션의 너비를 100%로 설정 */
  padding: 0 20px; /* 좌우 패딩 20px 추가 */
}
.chat-button {
  background-color: #ef5554;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  cursor: pointer;
  width: 100%;
  height: 55px;
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  display: inline-block; /* block으로 변경하여 너비 조정 */
  width: 100%; /* 부모 요소 크기를 기반으로 하므로, 100%로 설정 */
  text-decoration: none; /* 링크 기본 스타일 제거 */
}

#main-logo,
#main-character {
  justify-content: center;
  align-items: center;
}

#main-character > img {
  width: 270px;
}

#main-character {
  position: absolute;
  bottom: 190px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1;
}

#main-logo {
  justify-content: center;
  align-items: center;
  margin-top: 30px; /* 상단에서 150px 떨어지도록 추가 */
}

#main-logo > img {
  width: 250px; /* 로고 이미지의 너비 설정 */
  height: auto; /* 비율 유지 */
}
</style>