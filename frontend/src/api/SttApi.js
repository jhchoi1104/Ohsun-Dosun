import axios from 'axios';

/**
 * 오디오 파일을 서버로 전송하는 함수
 * @param {FormData} formData - 전송할 폼 데이터
 * @returns {Promise<Object>} - 서버 응답 객체
 */
export const sendAudioToServer = async (formData) => {
  try {
    const response = await axios.post('/api/v1/speech', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });

    return response.data; // 서버 응답 데이터 반환
  } catch (error) {
    console.error('서버 전송 오류:', error);
    throw error; // 호출한 곳에서 에러 처리
  }
};
