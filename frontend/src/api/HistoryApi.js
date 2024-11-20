import api from '@/api'; // API 설정 파일

const BASE_URL = '/api/history'; // 기본 경로 설정

export default {
    // userId에 해당하는 데이터 가져오기
    async getHistory(userId) {
        try {
            const { data } = await api.get(`${BASE_URL}/get`, {
                params: { userId }, 
            });
            console.log('response:', data);
            return data;
        } catch (error) {
            console.error('요청 중 오류 발생:', error);
            throw error; 
        }
    },
};
