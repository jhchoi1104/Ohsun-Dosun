<template>
  <div>
    <Header />
    <div class="main-container">
      <div class="header">
        <button @click="prevMonth">＜</button>
        <span>{{ currentMonth }}</span>
        <button @click="nextMonth">＞</button>
      </div>

      <div class="chart-section">
        <canvas id="PieChart"></canvas>
      </div>

      <div class="balance-section">
        <div class="balance-title">입출금 현황</div>
        <div class="balance-bar">
          <div
            class="depositBar"
            :style="{ width: depositPercentage + '%' }"
          ></div>
          <div
            class="withdrawal"
            :style="{ width: withdrawalPercentage + '%' }"
          ></div>
        </div>
        <div class="balance-info">
          <div class="balance-row">
            <div class="balance-item">
              <span class="dot deposit-dot"></span>
              <span>입금 {{ depositPercentage }}%</span>
            </div>
            <span class="amount">{{ depositTotal.toLocaleString() }}원</span>
          </div>
          <div class="balance-row">
            <div class="balance-item">
              <span class="dot withdrawal-dot"></span>
              <span>출금 {{ withdrawalPercentage }}%</span>
            </div>
            <span class="amount">{{ withdrawalTotal.toLocaleString() }}원</span>
          </div>
        </div>
      </div>

      <div class="transaction-list">
        <div
          class="transaction-item"
          v-for="(transaction, index) in transactions"
          :key="index"
        >
          <div class="date">{{ transaction.date }}</div>
          <div class="details">
            <div class="description-container">
              <span>{{ transaction.description }}</span>
            </div>
            <span :class="transaction.type">{{ transaction.amount }}원</span>
          </div>
          <div class="balance">{{ transaction.balance }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { Chart, PieController, ArcElement, Tooltip, Legend } from 'chart.js';
import Header from '@/components/Header.vue';
import HistoryApi from '@/api/HistoryApi';

Chart.register(PieController, ArcElement, Tooltip, Legend);

// 현재 월을 초기값으로 설정
const currentMonth = ref(
  new Date().getFullYear() +
    '.' +
    String(new Date().getMonth() + 1).padStart(2, '0')
);
const transactions = ref([]);
const depositPercentage = ref(0);
const withdrawalPercentage = ref(0);
const depositTotal = ref(0);
const withdrawalTotal = ref(0);
const allTransactions = ref([]); // 전체 거래 데이터 저장
let chart = null;

// 날짜 포맷팅 함수
function formatDate(timestamp) {
  const date = new Date(timestamp);
  return `${date.getFullYear()}. ${String(date.getMonth() + 1).padStart(
    2,
    '0'
  )}. ${String(date.getDate()).padStart(2, '0')}`;
}

// 월별 데이터 필터링 함수
function filterTransactionsByMonth(transactions, yearMonth) {
  const [year, month] = yearMonth.split('.');
  return transactions.filter((transaction) => {
    const transactionDate = new Date(transaction.transactionDate);
    return (
      transactionDate.getFullYear() === parseInt(year) &&
      transactionDate.getMonth() + 1 === parseInt(month)
    );
  });
}

// API 응답 데이터 변환 함수
function transformTransaction(transaction) {
  const isWithdrawal = transaction.senderId === 1;
  return {
    date: formatDate(transaction.transactionDate),
    description: isWithdrawal ? '출금' : '입금',
    type: isWithdrawal ? 'withdraw' : 'deposit',
    amount: transaction.amount.toLocaleString(),
    balance: transaction.senderId === 2 || transaction.receiverId === 2 ? '쵸단' :
             transaction.senderId === 3 || transaction.receiverId === 3 ? '마젠타' :
             transaction.senderId === 4 || transaction.receiverId === 4 ? '히나' :
             transaction.senderId === 5 || transaction.receiverId === 5 ? '시연' : '잔액 확인 중',
  };
}

// 입금/출금 비율 계산 함수
function calculatePercentages(transactions) {
  let depositAmount = 0;
  let withdrawalAmount = 0;

  transactions.forEach((transaction) => {
    if (transaction.senderId === 1) {
      withdrawalAmount += transaction.amount;
    } else {
      depositAmount += transaction.amount;
    }
  });

  const total = depositAmount + withdrawalAmount;
  if (total === 0) {
    depositTotal.value = 0;
    withdrawalTotal.value = 0;
    return { deposit: 50, withdrawal: 50 };
  }

  depositTotal.value = depositAmount;
  withdrawalTotal.value = withdrawalAmount;

  return {
    deposit: Math.round((depositAmount / total) * 100),
    withdrawal: Math.round((withdrawalAmount / total) * 100),
  };
}

// 월 이동 함수
function prevMonth() {
  const [year, month] = currentMonth.value.split('.');
  const newDate = new Date(year, parseInt(month) - 2, 1);
  currentMonth.value = `${newDate.getFullYear()}.${String(
    newDate.getMonth() + 1
  ).padStart(2, '0')}`;
  updateDisplayData();
}

function nextMonth() {
  const [year, month] = currentMonth.value.split('.');
  const newDate = new Date(year, parseInt(month), 1);
  currentMonth.value = `${newDate.getFullYear()}.${String(
    newDate.getMonth() + 1
  ).padStart(2, '0')}`;
  updateDisplayData();
}

// 차트 생성 함수
function createChart(deposit, withdrawal) {
  if (chart) {
    chart.destroy();
  }

  const ctx = document.getElementById('PieChart');
  if (!ctx) {
    console.error('차트 캔버스를 찾을 수 없습니다');
    return;
  }

  chart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['입금', '출금'],
      datasets: [
        {
          data: [deposit, withdrawal],
          backgroundColor: ['#a9c9e3', '#f799c8'],
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'top',
        },
      },
    },
  });
}

// 화면 데이터 업데이트 함수
async function updateDisplayData() {
  const filteredTransactions = filterTransactionsByMonth(
    allTransactions.value,
    currentMonth.value
  );
  transactions.value = filteredTransactions.map(transformTransaction);

  const percentages = calculatePercentages(filteredTransactions);
  depositPercentage.value = percentages.deposit;
  withdrawalPercentage.value = percentages.withdrawal;

  await nextTick(() => {
    createChart(depositPercentage.value, withdrawalPercentage.value);
  });
}

onMounted(async () => {
  try {
    const response = await HistoryApi.getHistory(1);
    allTransactions.value = response;
    updateDisplayData();
  } catch (error) {
    console.error('거래 내역을 불러오는데 실패했습니다:', error);
  }
});

onUnmounted(() => {
  if (chart) {
    chart.destroy();
  }
});
</script>

<style scoped>
.main-container {
  height: calc(100vh - 70px);
  display: flex;
  flex-direction: column;
  align-items: stretch;
  box-sizing: border-box;
  background-color: #fff5f2;
  padding: 0 20px;
  margin: 0 auto;
}

.balance-section,
.transaction-list {
  width: 100%;
  padding: 20px;
  box-sizing: border-box;
  background-color: #ffffff;
  border-radius: 10px;
  margin: 10px 0;
}

.header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 5px 0;
}

.header button {
  background: none;
  border: none;
  font-size: 18px;
  color: #333;
  margin: 0 10px;
  cursor: pointer;
}

.chart-section {
  display: flex;
  justify-content: center;
  padding: 0 0;
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}

#PieChart {
  width: 200px !important;
  height: 200px !important;
  margin: 0 auto;
}

.balance-title {
  font-size: 15px;
  margin-bottom: 5px;
}

.balance-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding-top: 5px;
  font-size: 14px;
  color: #666;
}

.balance-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.balance-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.amount {
  margin-left: auto;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.deposit-dot {
  background-color: #a9c9e3;
}

.withdrawal-dot {
  background-color: #f799c8;
}

.balance-bar {
  display: flex;
  width: 100%;
  height: 10px;
  border-radius: 5px;
  overflow: hidden;
}

.depositBar {
  background-color: #a9c9e3;
}

.withdrawal {
  background-color: #f799c8;
}

.transaction-list {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 20px;
  margin: 10px 0;
  width: 100%;
  overflow-y: auto;
  max-height: 40vh;
  scrollbar-width: none;
}

.transaction-item {
  border-bottom: 1px solid #e5e5e5;
  padding: 10px 0;
}

.transaction-item:last-child {
  border-bottom: none;
}

.date {
  font-size: 14px;
  color: #999;
}

.details {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.description-container {
  display: flex;
  align-items: center;
  flex: 1;
}

.balance {
  font-size: 12px;
  color: #666;
  text-align: right;
  margin-top: 4px;
}

.withdraw {
  color: #a9c9e3;
}

.deposit {
  color: #f799c8;
}
</style>
