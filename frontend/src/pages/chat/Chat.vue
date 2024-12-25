<script setup>
import Header from '@/components/Header.vue';
import { ref, onMounted, onUnmounted } from 'vue';
import { sendAudioToServer } from '@/api/SttApi';
import { bringAudioFromServer } from '@/api/TtsApi.js';
import Consultant from '@/components/Consultant.vue';
import NewIssuanceForm from '@/components/NewIssuanceForm.vue';
import NewReissuanceForm from '@/components/Reissuance.vue';
import TransferForm from '@/components/TransferForm.vue';
import LoanDetail from '@/components/LoanDetail.vue';
import { useInputStore } from '@/stores/inputStore';
import { useFontSizeStore } from '@/api/fontsize.js';

const inputStore = useInputStore();
let sttCancelRequested = false;
let chatbotCancelRequested = false;
let ttsCancelRequested = false;

const NewIssuanceFormVisible = ref(false);
const NewReissunaceFormVisible = ref(false);
const isConsultantModalVisible = ref(false);
const TransferFormVisible = ref(false);
const isRecording = ref(false);
const isAnswering = ref(false);
const isLoanDetailVisible = ref(false); //추가
const errorMessage = ref('');
const transcription = ref('');
const chatbotMessage = ref(''); // Chatbot 응답 메시지
const chatbotMessagesub = ref(''); //subTask 저장
const transferstep = ref('');
const call = ref(''); //상담원
const socket = ref(null); // WebSocket 관련 변수
const messages = ref([]); // WebSocket 관련 변수
let audio = null;
let mediaRecorder = null; // 녹음기 초기화
const audioQueue = ref([]);

const fontSizeStore = useFontSizeStore(); // store 인스턴스 생성

// 폰트 사이즈 스타일 설정
const fontSizeStyle = computed(() => {
  return { fontSize: fontSizeStore.getFontSizeValue() }; // store의 getFontSizeValue 메서드 호출
});

// 웹 서버 -> 클라이언트 (웹소켓)
const connectWebSocket = () => {
  socket.value = new WebSocket('ws://localhost:8080/ws/chat');

  socket.value.onmessage = async (event) => {
    if (typeof event.data === 'string') {
      try {
        const data = JSON.parse(event.data);

        if (chatbotCancelRequested) {
          console.log('Chatbot 요청이 취소되었습니다.');
          return;
        }

        if (data.content && data.step !== undefined) {
          // transfer 챗봇 응답 처리
          chatbotMessage.value = data.content;
          transferstep.value = data.step;
          inputStore.updateInput('step', transferstep.value);
          inputStore.updateInput('name', data.name);
          inputStore.updateInput('amount', data.amount);
        } else if (data.mainTaskNo && data.subTaskNo) {
          // 클래스 분류 처리
          chatbotMessagesub.value = data.subTaskNo;
          handleSubTask(data.mainTaskNo, data.subTaskNo);
        } else {
          // JSON이지만 처리할 유형이 정의되지 않은 경우
          console.warn('Unhandled JSON data:', data);
        }
      } catch (error) {
        // JSON 파싱 실패 -> 일반 텍스트 메시지로 처리
        if (!chatbotCancelRequested) {
          chatbotMessage.value += event.data; // 스트리밍된 일반 텍스트를 이어붙임
        }
      }
    } else if (event.data instanceof Blob) {
      if (!ttsCancelRequested) {
        audioQueue.value.push(event.data); // 새 데이터 추가
        if (!audio) {
          playAudioQueue(); // 오디오 재생이 진행 중이 아닌 경우에만 호출
        }
      }
    } else {
      console.warn('Unhandled WebSocket message type:', event.data);
    }
  };

  socket.value.onerror = (error) => {
    console.error('WebSocket Error:', error);
  };

  socket.value.onclose = () => {
    console.log('WebSocket closed.');
  };
};

// 통신 중단
const disconnectWebSocket = () => {
  if (socket.value) {
    socket.value.close();
  }
};

const handleSubTask = (mainTask, subTask) => {
  switch (subTask) {
    case '002-01':
    case '002-02':
      setTimeout(() => {
        openConsultantModal();
      }, 3000); //3초 지연
      break;
    case '003-01':
      if (transferstep.value >= 3) {
        openTransferForm();
      }
      break;
    case '003-02':
      if (transferstep.value >= 3) {
        openTransferForm();
      }
      break;
    case '003-03':
      if (transferstep.value >= 3) {
        openTransferForm();
      }
      break;
    case '004-01':
      openNewReissuanceForm();
      break;
    case '004-02':
      openNewReissuanceForm();
      break;
    case '005-01':
      openNewIssuanceForm();
      break;
    case '001-02': //새로운 case
      openLoanDetail();
      break;
    case '01-02': //새로운 case
      openLoanDetail();
      break;
    case '001.02': //새로운 case
      openLoanDetail();
      break;
    case '001-03':
      closeLoanDetail();

    default:
      break;
  }
};

// 챗봇 응답 오디오 송출(스트리밍 처리)
const playAudioQueue = () => {
  if (audio || audioQueue.value.length === 0) return; // 이미 재생 중이면 종료

  const audioBlob = audioQueue.value.shift(); // 큐에서 데이터 추출
  const audioUrl = URL.createObjectURL(audioBlob);
  audio = new Audio(audioUrl);

  audio.addEventListener('ended', () => {
    audio = null; // 현재 재생 상태 초기화
    playAudioQueue(); // 다음 오디오 재생
  });

  audio.play();
};

// 녹음 함수: 사용자 음성 -> 웹 서버 (웹소켓)
const startRecording = () => {
  try {
    // 상태 초기화
    sttCancelRequested = false;
    chatbotCancelRequested = false;
    ttsCancelRequested = false;

    chatbotMessage.value = '';
    transcription.value = '';
    let audioChunks = [];

    // 음성 출력이 진행 중이라면 멈추기
    if (audio && !audio.paused) {
      audio.pause();
      audio.currentTime = 0;
    }

    if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
      throw new Error('마이크 권한을 요청할 수 없습니다.');
    }

    navigator.mediaDevices
      .getUserMedia({ audio: true })
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
            if (sttCancelRequested) return;
            const data = await sendAudioToServer(formData);
            transcription.value = data.text || '텍스트를 인식할 수 없습니다.';

            if (transcription.value !== '텍스트를 인식할 수 없습니다') {
              const conversationRoomNo = 3;
              const userId = 1;

              // ChatBot 챗봇 API 호출
              if (chatbotCancelRequested) return;
              console.log('웹소켓 통신 전');
              socket.value.send(
                JSON.stringify({
                  userId,
                  input: transcription.value,
                  conversationRoomNo,
                })
              );
              console.log('웹소켓 통신 후');
            }
          } catch (error) {
            if (sttCancelRequested) {
              console.log('STT 요청이 취소되었습니다.');
            } else {
              console.error('Error sending audio to server:', error);
            }
          }
        };

        mediaRecorder.start();
        isRecording.value = true;
        errorMessage.value = '';
      })
      .catch((err) => {
        console.error('녹음 시작 오류:', err);
        errorMessage.value = '마이크 권한을 확인해주세요.';
      });
  } catch (error) {
    console.error('녹음기 초기화 오류:', error);
    errorMessage.value = '녹음기를 초기화할 수 없습니다.';
  }
};

// 녹음 중지 함수
const stopRecording = () => {
  if (mediaRecorder && mediaRecorder.state === 'recording') {
    mediaRecorder.stop();
    isRecording.value = false;
    isAnswering.value = true;
  } else {
    errorMessage.value = '녹음이 진행되지 않았습니다.';
  }
};

// 요청 취소 함수
const stopAnswering = () => {
  sttCancelRequested = true;
  chatbotCancelRequested = true;
  ttsCancelRequested = true;

  // 오디오 재생 중단
  if (audio && !audio.paused) {
    audio.pause();
    audio.currentTime = 0;
  }

  // 상태 초기화
  isRecording.value = false;
  isAnswering.value = false;
  transcription.value = '';
  chatbotMessage.value = '';
  chatbotMessagesub.value = '';
  transferstep.value = '';
  errorMessage.value = '';

  audioQueue.value = [];
};

// TTS 모델 통신, Audio 송출
const playAudio = async (input) => {
  try {
    // 서버에서 오디오 데이터 가져오기
    const base64Audio = await bringAudioFromServer(input);

    // Base64 디코딩 및 오디오 재생
    const byteCharacters = atob(base64Audio);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const audioBlob = new Blob([byteArray], { type: 'audio/wav' });

    // Blob URL 생성 후 오디오 재생
    const audioUrl = URL.createObjectURL(audioBlob);

    console.log(audioUrl);
    audio = new Audio(audioUrl);
    audio.play();
  } catch (error) {
    console.error('TTS 처리 중 오류:', error);
    alert('오류가 발생했습니다. 콘솔을 확인하세요.');
  }
};

// 처음 인사말 생성 함수
const createGreet = async () => {
  try {
    chatbotMessage.value = '안녕하세요. 무엇을 도와드릴까요?'; // 인사말 설정

    const response = playAudio(chatbotMessage.value); // 음성 재생
    console.log(response); // 응답 확인 (필요 시 로그)

    connectWebSocket();
  } catch (error) {
    console.error('Error during createGreet execution:', error); // 에러 로그
    errorMessage.value = '서버에 전송하는 중 오류가 발생했습니다.'; // 에러 메시지 설정
  }
};

const openConsultantModal = () => {
  isConsultantModalVisible.value = true;
};

const closeConsultantModal = () => {
  isConsultantModalVisible.value = false;
};

const openNewIssuanceForm = () => {
  NewIssuanceFormVisible.value = true;
};

const closeNewIssuanceForm = () => {
  NewIssuanceFormVisible.value = false;
};
const openNewReissuanceForm = () => {
  NewReissunaceFormVisible.value = true;
};
const closeReissunaceForm = () => {
  NewReissunaceFormVisible.value = false;
};
const openLoanDetail = () => {
  isLoanDetailVisible.value = true;
};

const closeLoanDetail = () => {
  isLoanDetailVisible.value = false;
};

const openTransferForm = () => {
  TransferFormVisible.value = true;
};
const closeTransferForm = () => {
  TransferFormVisible.value = false;
};

onMounted(() => {
  connectWebSocket();
});

onUnmounted(() => {
  disconnectWebSocket();
});
</script>

<template>
  <div :style="fontSizeStyle">
    <Header />
    <div class="main-container" :style="fontSizeStyle">
      <p v-if="errorMessage" style="color: red">{{ errorMessage }}</p>
      <p
        class="additional-bubble"
        v-if="chatbotMessage"
        :style="{ whiteSpace: 'pre-line' }"
      >
        {{ chatbotMessage }}
      </p>
      <!-- Chatbot 응답 표시 -->

      <div class="sub-container" :style="fontSizeStyle">
        <div
          id="main-character"
          v-if="!isLoanDetailVisible"
          @click="createGreet"
        >
          <img v-if="!isRecording" src="@/assets/images/sooni.png" alt="" />
          <div v-else class="listenimg">
            듣는 중...
            <img src="@/assets/images/listen.png" alt="" />
          </div>
        </div>
        <LoanDetail
          v-if="isLoanDetailVisible"
          @close="closeLoanDetail"
          class="loan-detail-component"
        />
      </div>
      <div class="speech-bubble" v-if="transcription" :style="fontSizeStyle">
        {{ transcription }}
      </div>
      <div class="button-section">
        <button
          class="chat-button"
          @click="startRecording"
          v-if="!isRecording && !isAnswering"
        >
          말하기
        </button>
        <button
          class="chat-button"
          @click="stopRecording"
          v-if="isRecording && !isAnswering"
        >
          중지
        </button>
        <div class="answer-section" v-if="isAnswering">
          <button class="chat-button answer-button" disabled>
            답변 중입니다 ...
          </button>
          <button class="chat-button close-button" @click="stopAnswering">
            X
          </button>
        </div>
      </div>

      <Consultant
        v-if="isConsultantModalVisible"
        :isModalVisible="isConsultantModalVisible"
        @close="closeConsultantModal"
      />
      <NewIssuanceForm
        v-if="NewIssuanceFormVisible"
        :show="NewIssuanceFormVisible"
        @close="closeNewIssuanceForm"
      />
      <NewReissuanceForm
        v-if="NewReissunaceFormVisible"
        :show="NewReissunaceFormVisible"
        @close="closeReissunaceForm"
      />
      <TransferForm
        v-if="TransferFormVisible"
        :show="TransferFormVisible"
        @close="closeTransferForm"
      />
    </div>
  </div>
</template>

<style scoped>
.additional-bubble {
  white-space: pre-line;
}
.main-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between; /* 요소 간의 공간을 균등하게 분배 */
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
  position: absolute;
  bottom: 1px;
  margin-bottom: 30px;
  width: 100%; /* 버튼 섹션의 너비를 100%로 설정 */
  padding: 0 20px; /* 좌우 패딩 20px 추가 */
}
.additional-bubble {
  font-size: inherit; /* 상속받도록 변경 */
  background-color: #f7c8bd; /* 말풍선 배경색 */
  border-radius: 10px; /* 모서리 둥글게 */
  padding: 10px 15px; /* 패딩 추가 */
  position: absolute; /* 절대 위치 설정 */
  max-width: 80%; /* 최대 너비 설정 */
  width: 300px;
  max-height: 150px;
  text-align: center; /* 텍스트 중앙 정렬 */
  top: calc(50% - 150px); /* 이미지 바로 위로 위치 조정 */
  left: 50%; /* 수평 중앙 정렬 */
  transform: translate(-50%, -100%); /* 정확히 이미지 위에 배치 */
  z-index: 2; /* 이미지 위에 표시 */
  overflow: scroll;
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
  top: -100px; /* Y축 위치 조정 (이미지 위로 이동) */
  margin-top: 450px;
  font-size: inherit; /* 상속받도록 변경 */
  overflow: scroll;
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
  font-size: 1.8rem;
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
.loan-detail-component {
  position: absolute;
  bottom: 180px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1;
}

#main-logo {
  justify-content: center;
  align-items: center;
  margin-top: 30px; /* 상단에서 150px 떨어지록 추가 */
}

#main-logo > img {
  width: 250px; /* 로고 이미지의 너비 설정 */
  height: auto; /* 비율 유지 */
}

.additional-bubble {
  background-color: #f7c8bd; /* 말풍선 배경색 */
  border-radius: 10px; /* 모서리 둥글게 */
  padding: 10px 15px; /* 패딩 추가 */
  position: absolute; /* 절대 위치 설정 */
  max-width: 80%; /* 최대 너비 설정 */
  width: 300px;
  max-height: 150px;
  text-align: center; /* 텍스트 중앙 정렬 */
  top: calc(50% - 150px); /* 이미지 바로 위로 위치 조정 */
  left: 50%; /* 수평 중앙 정렬 */
  transform: translate(-50%, -100%); /* 정확히 이미지 위에 배치 */
  z-index: 2; /* 이미지 위에 표시 */
  overflow: scroll;
}

/*.additional-bubble::after {
  content: '';
  position: absolute;
  top: 100%; 말풍선 아래쪽에 위치 
  left: 50%; 중앙 정렬
  transform: translateX(-50%);
  border-width: 10px; 삼각형 크기 
  border-style: solid;
  border-color: #efefef transparent transparent transparent;  삼각형 색상 
} */

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
  top: -100px; /* Y축 위치 조정 (이미지 위로 이동) */
  margin-top: 450px;
  font-size: 1.44rem;
}
.chat-button[disabled] {
  cursor: not-allowed;
  opacity: 0.6;
}
.answer-section {
  display: flex;
  justify-content: center;
  gap: 10px; /* 버튼 사이 간격 */
}

.answer-button {
  flex: 2; /* 답변 버튼이 더 넓게 차지하도록 설정 */
  max-width: 70%; /* 버튼의 최대 너비 제한 */
}

.close-button {
  flex: 1; /* X 버튼이 좁게 차지하도록 설정 */
  max-width: 20%; /* 버튼의 최대 너비 제한 */
  background-color: #ccc; /* X 버튼 배경색 */
  color: black; /* X 버튼 텍스트 색상 */
}
</style>
