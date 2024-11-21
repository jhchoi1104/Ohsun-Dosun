<template>
    <div :class="['camera-modal', { hidden: !show }]">
      <div class="camera-modal-content">
        <span class="camera-close" @click="close">&times;</span>
        <p class="camera-instruction">신분증과 얼굴이 모두 나오게 해주세요.</p>
        <div class="camera-container">
          <video ref="cameraView" autoplay></video>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { onMounted, watch, onBeforeUnmount, defineProps, ref } from 'vue';
  
  const props = defineProps({
    show: {
      type: Boolean,
      required: true,
    },
  });
  
  const emit = defineEmits(['close']);
  const cameraView = ref(null);
  let mediaStream = null;
  
  function camInit(stream) {
    mediaStream = stream;
    if (cameraView.value) {
      cameraView.value.srcObject = mediaStream;
    } else {
      console.error("cameraView is not ready.");
    }
  }
  
  function camInitFailed(error) {
    console.log("get camera permission failed: ", error);
  }
  
  function mainInit() {
    if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
      alert("Media Device not supported");
      return;
    }
    navigator.mediaDevices.getUserMedia({ video: true })
      .then(camInit)
      .catch(camInitFailed);
  }
  
  onMounted(() => {
    if (props.show) {
      mainInit();
    }
  });
  
  watch(() => props.show, (newValue) => {
    if (newValue) {
      mainInit();
    } else {
      if (mediaStream) {
        mediaStream.getTracks().forEach((track) => track.stop());
        mediaStream = null;
      }
    }
  });
  
  onBeforeUnmount(() => {
    if (mediaStream) {
      mediaStream.getTracks().forEach((track) => track.stop());
    }
  });
  
  const close = () => {
    emit('close');
  };
  </script>
  
  <style scoped>
  .camera-modal {
    position: fixed;
    width: 100%;
    top: 50%; /* 화면의 수직 중앙 */
    left: 50%; /* 화면의 수평 중앙 */
    transform: translate(-50%, -50%); /* 중앙 정렬 */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .camera-modal.hidden {
    display: none; /* 모달이 숨겨질 때 */
  }
  
  .camera-modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    width: 90%; /* 가로 폭 유지 */
    max-width: 500px; /* 최대 가로 폭 유지 */
    max-height: 600px; /* 세로 폭 확대 */
  }
  
  .camera-close {
    cursor: pointer;
    float: right;
    font-size: 20px;
  }
  
  .camera-container {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .camera-container video {
    width: 100%; /* 가로 크기 유지 */
    height: auto; /* 비율 유지 */
    max-height: 500px; /* 세로 크기 확대 */
    object-fit: cover; /* 비율을 유지하며 컨테이너를 채움 */
    border-radius: 10px; /* 모서리를 둥글게 */
  }
  
  .camera-instruction {
    font-size: 1rem; /* 적당한 글씨 크기 */
    color: #333; /* 텍스트 색상 */
    text-align: center; /* 중앙 정렬 */
    margin-bottom: 15px; /* 비디오와의 간격 */
    font-weight: bold; /* 글씨 강조 */
  }
  </style>
  