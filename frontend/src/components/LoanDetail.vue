<template>
    <div class="container">
        <div class="d-flex justify-content-between" alt="1">
            <div class="flex-item">
                <button class="btn"@click="playAudio('주택 담보 대출')">주택 담보 대출🎙️</button>
                <audio ref="audio1" src="../../src/assets/대출음성/주택담보.mp3"></audio>
            </div>
            <div class="flex-item">
                <button class="btn" @click="playAudio('전세자금 대출')">전세 자금 대출🎙️</button>
                <audio ref="audio2" src="../../src/assets/대출음성/전세자금.mp3"></audio>
            </div>
        </div>
        <div class="d-flex justify-content-between" alt="2">
            <div class="flex-item">
                <button class="btn" @click="playAudio('자동차 구입 대출')">자동차 구입 대출🎙️</button>
                <audio ref="audio3" src="../../src/assets/대출음성/자동차.mp3"></audio>
            </div>
            <div class="flex-item">
                <button class="btn" @click="playAudio('신용 대출')">신용 대출🎙️</button>
                <audio ref="audio4" src="../../src/assets/대출음성/신용.mp3"></audio>
            </div>
        </div>
        <div class="d-flex justify-content-between" alt="3">
            <div class="flex-item">
                <button class="btn" @click="playAudio('예치금 담보 대출')">예금 담보 대출🎙️</button>
                <audio ref="audio5" src="../../src/assets/대출음성/예금담보.mp3"></audio>
            </div>
            <div class="flex-item">
                <button class="btn" @click="playAudio('KB 비상금 대출')">KB 비상금 대출🎙️</button>
                <audio ref="audio6" src="../../src/assets/대출음성/비상금.mp3"></audio>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const audio1 = ref(null);
const audio2 = ref(null);
const audio3 = ref(null);
const audio4 = ref(null);
const audio5 = ref(null);
const audio6 = ref(null);

// 현재 재생 중인 오디오 추적하기
const currentAudio = ref(null);

// type과 오디오 참조를 매핑하는 객체
const audioMap = {
    '주택 담보 대출' : audio1,
    '전세자금 대출' : audio2,
    '자동차 구입 대출' : audio3,
    '신용 대출' : audio4,
    '예치금 담보 대출' : audio5,
    'KB 비상금 대출' : audio6,
};

const playAudio = (type) => {
    const audioRef = audioMap[type];
    console.log('Playing audio for type:', type, 'AudioRef:', audioRef.value);
    if (audioRef && audioRef.value) {
        // 현재 재생 중인 오디오가 있고, 새로운 오디오가 다르면 중지
        if (currentAudio.value && currentAudio.value !== audioRef.value) {
            currentAudio.value.pause();
            currentAudio.value.currentTime = 0;
        }

        // 새로운 오디오를 재생
        audioRef.value.play().then(() => {
            currentAudio.value = audioRef.value;
        }).catch(error => {
            console.error('오디오 재생 에러:', error);
        });
    } else {
        console.warn('해당 오디오 요소를 찾을 수 없습니다:', type);
    }
}

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
    background-color:rgba(244, 160, 139, 0.486);
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