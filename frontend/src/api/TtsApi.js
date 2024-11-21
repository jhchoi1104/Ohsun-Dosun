import axios from 'axios';

/**
 * 일반 텍스트를 서버로 전송해서 오디오 텍스트를 받는 함수
 * String - 전송할 데이터 형태
 * Base64 문자열 - 서버 응답 객체
 */
export const bringAudioFromServer = async (input) => {
  try {
    const response = await axios.post('/api/v1/tts', { input: input });

    return response.data; // 서버 응답 데이터 반환
  } catch (error) {
    console.error('서버 전송 오류:', error);
    throw error; // 호출한 곳에서 에러 처리
  }
};
