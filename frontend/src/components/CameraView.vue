<template>
  <div>
    <video id="cameraview" width="720" height="480"></video>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';

function camInit(stream) {
  const cameraView = document.getElementById("cameraview");
  cameraView.srcObject = stream;
  cameraView.play();
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
  mainInit();
});
</script>

<style scoped>
/* 필요한 스타일을 추가할 수 있습니다 */
</style>
