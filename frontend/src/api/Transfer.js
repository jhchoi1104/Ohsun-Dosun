import axios from 'axios';

const BASE_URL = '/api/transfer';

export const bringTransfer = async (input) => {
  try {
    const response = await axios.post(`${BASE_URL}/send`, { input }); // send로 요청한다고 가정
    return response.data; // 서버 응답 데이터 반환
  } catch (error) {
    console.error('송금 요청 실패: ', error); // 에러 로깅 추가
    throw error.response?.data || '송금 요청 중 문제가 발생했습니다.'; // 호출 측에서 에러 처리할 수 있도록 다시 던지기
  }
};
