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
    <div class="additional-bubble" v-if="transcription">
      인식된 텍스트: {{ transcription }}
    </div>
    <div class="sub-container">
      <div id="main-character">
        <img src="@/assets/images/sooni.png" alt="" />
      </div>
    </div>
    <div class="speech-bubble">
    <!-- <div class="speech-bubble" v-if="transcription"> -->
      인식된 텍스트: {{ transcription }}
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

.additional-bubble {
  background-color: #efefef; /* 말풍선 배경색 */
  border-radius: 10px; /* 모서리 둥글게 */
  padding: 10px 15px; /* 패딩 추가 */
  position: absolute; /* 절대 위치 설정 */
  max-width: 80%; /* 최대 너비 설정 */
  text-align: center; /* 텍스트 중앙 정렬 */
  top: calc(50% - 150px); /* 이미지 바로 위로 위치 조정 */
  left: 50%; /* 수평 중앙 정렬 */
  transform: translate(-50%, -100%); /* 정확히 이미지 위에 배치 */
  z-index: 2; /* 이미지 위에 표시 */
}

.additional-bubble::after {
  content: '';
  position: absolute;
  top: 100%; /* 말풍선 아래쪽에 위치 */
  left: 50%; /* 중앙 정렬 */
  transform: translateX(-50%);
  border-width: 10px; /* 삼각형 크기 */
  border-style: solid;
  border-color: #efefef transparent transparent transparent; /* 삼각형 색상 */
}

.speech-bubble {
  width: 80%; /* 박스 너비 */
  background-color: #f9f9f9; /* 박스 배경색 */
  border: 1px solid #ddd; /* 박스 테두리 */
  border-radius: 10px; /* 박스 모서리 둥글게 */
  padding: 15px; /* 안쪽 여백 */
  text-align: center; /* 텍스트 중앙 정렬 */
  margin: 10px auto; /* 위아래 여백 및 중앙 정렬 */
  z-index: 1; /* 레이어 우선 순위 설정 */
  position: relative; /* 박스의 위치를 일반 흐름에 맞춤 */
  top: -60px; /* Y축 위치 조정 (이미지 위로 이동) */
  margin-top: 450px;
  
}
</style>