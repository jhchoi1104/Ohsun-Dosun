import api from '@/api';

const BASE_URL = '/api/History'; // 기본 경로 설정
// const headers = { "Content-Type": "multipart/form-data" }; // 기본 헤더

export default {
    async applyFilters(filters, pageRequest) {
        const requestBody = {
            sortOrder: filters.sortOrder,
        };

        console.log(requestBody);

        const { data } = response;
        // console.log('data 확인:', JSON.stringify(data, null, 2)); // JSON 형식으로 출력
        return data; // 필터링된 거래 내역 및 페이지네이션 정보 반환
    },
};
