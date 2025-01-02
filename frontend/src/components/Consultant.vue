<script setup>
import { defineProps, defineEmits } from 'vue';

// Props와 Emit 정의
const props = defineProps({
  isModalVisible: {
    type: Boolean,
    required: true,
  },
});
const emit = defineEmits(['close']);

// 모달 닫기
const closeModal = () => {
  emit('close');
};

const phoneNumber = '1588-9999';
</script>

<template>
  <div class="d-flex" id="top">
    <div v-if="isModalVisible" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <a :href="'tel:' + phoneNumber" class="phone-number">
          통화 {{ phoneNumber }}</a
        >
      </div>
      <div class="modal-content">
        <button class="close-button" @click="closeModal">취소</button>
      </div>
    </div>
  </div>
</template>

<style>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  z-index: 1000;
}

.modal-content {
  background-color: rgba(0, 0, 0, 0.5);
  padding: 5px;
  border-radius: 10px;
  text-align: center;
  width: 90%;
  max-width: 400px;
  margin-bottom: 10px; /* 아래 content와 간격 */
}

.phone-number {
  display: block;
  margin: 5px 0;
  font-size: 20px;
  color: #007bff;
  text-decoration: none;
}

.close-button {
  font-size: 20px;
  margin: 1px 0;
  background-color: rgba(0, 0, 0, 0);
  color: #007bff;
  border: none;
}
</style>
