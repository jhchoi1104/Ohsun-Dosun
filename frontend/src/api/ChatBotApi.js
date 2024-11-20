import axios from 'axios';

// ChatBot API - 텍스트를 서버에 전송
export async function sendTextToServer(userId, input, conversationRoomNo) {
    try {
        const response = await axios.post(
            '/api/v1/conversation',
            {
                input: input,
                conversationRoomNo: conversationRoomNo,
            },
            {
                headers: {
                    userId: userId, // 사용자 ID를 헤더로 전달
                },
            }
        );
        return response.data; // 응답 데이터 반환
    } catch (error) {
        console.error('Error in sendTextToServer:', error);
        throw error; // 에러를 호출 측으로 전달
    }
}
