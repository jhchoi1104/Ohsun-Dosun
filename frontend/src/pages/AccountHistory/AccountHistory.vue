<template>
  <div>
    <Header />
    <div class="container">
      <div class="header">
        <button @click="prevMonth">＜</button>
        <span>{{ currentMonth }}</span>
        <button @click="nextMonth">＞</button>
      </div>

      <div class="chart-section">
        <canvas id="pieChart"></canvas>
      </div>

      <div class="balance-section">
        <div class="balance-bar">
          <div class="deposit" :style="{ width: depositPercentage + '%' }"></div>
          <div class="withdrawal" :style="{ width: withdrawalPercentage + '%' }"></div>
        </div>
        <div class="balance-info">
          <span>입금 {{ depositPercentage }}%</span>
          <span>출금 {{ withdrawalPercentage }}%</span>
        </div>
      </div>

      <div class="transaction-list">
        <div class="transaction-item" v-for="(transaction, index) in transactions" :key="index">
          <div class="date">{{ transaction.date }}</div>
          <div class="details">
            <img :src="transaction.icon" alt="Icon" class="transaction-icon">
            <span>{{ transaction.description }}</span>
            <span :class="transaction.type">{{ transaction.amount }}원</span>
          </div>
          <div class="balance">잔액 {{ transaction.balance }}원</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Chart, PieController, ArcElement, Tooltip, Legend } from 'chart.js';
import Navbar from '@/components/Navbar.vue';

Chart.register(PieController, ArcElement, Tooltip, Legend);

const currentMonth = ref("2024.10");

const transactions = ref([
  { date: "2024. 10. 30", icon: "../../assets/계절밥상.png", description: "계절밥상", type: "withdraw", amount: "7,000", balance: "7,000,000" },
  { date: "2024. 10. 30", icon: "../../assets/홈플러스.png", description: "홈플러스", type: "deposit", amount: "7,000", balance: "7,000,000" },
]);

const depositPercentage = 51;
const withdrawalPercentage = 49;

function prevMonth() {
  const [year, month] = currentMonth.value.split(".");
  const newDate = new Date(year, month - 2, 1);
  currentMonth.value = `${newDate.getFullYear()}.${String(newDate.getMonth() + 1).padStart(2, "0")}`;
}

function nextMonth() {
  const [year, month] = currentMonth.value.split(".");
  const newDate = new Date(year, month, 1);
  currentMonth.value = `${newDate.getFullYear()}.${String(newDate.getMonth() + 1).padStart(2, "0")}`;
}

onMounted(() => {
  const ctx = document.getElementById("pieChart").getContext("2d");
  new Chart(ctx, {
    type: 'pie',
    data: {
      labels: ['입금', '출금'],
      datasets: [{
        data: [depositPercentage, withdrawalPercentage],
        backgroundColor: ['#a9c9e3', '#f799c8'],
      }]
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
});
</script>

<style scoped>
.container {
  height: calc(100vh - 70px);
  display: flex;
  flex-direction: column;
  align-items: stretch;
  box-sizing: border-box; /* 패딩을 너비에 포함 */
  background-color: #FFF5F2;
}

.balance-section, .transaction-list {
  width: 100%;
  padding: 20px;
  box-sizing: border-box; /* 패딩을 너비에 포함 */
  background-color: #FFFFFF;
  border-radius: 10px;
}
.header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 0;
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
  padding: 20px 0;
  width: 100%;
}

.balance-section {
  display: flex;
  flex-direction: column;
  width: 100%; /* 부모 너비에 맞춰 확장 */
  background-color: #FFFFFF;
  border-radius: 10px;
  padding: 20px;
  margin: 10px 0;
}

.balance-bar {
  display: flex;
  width: 100%; /* 부모 너비에 맞춰 확장 */
  height: 10px;
  border-radius: 5px;
  overflow: hidden;
}

.deposit {
  background-color: #a9c9e3;
}

.withdrawal {
  background-color: #f799c8;
}

.balance-info {
  display: flex;
  justify-content: space-between;
  padding-top: 5px;
  font-size: 14px;
  color: #666;
}

.transaction-list {
  background-color: #FFFFFF;
  border-radius: 10px;
  padding: 20px;
  margin: 20px 0;
  width: 100%;
  overflow-y: auto; /* 스크롤 추가 */
  max-height: 40vh; /* 리스트 최대 높이 설정 */
}

.transaction-item {
  border-bottom: 1px solid #E5E5E5;
  padding: 15px 0;
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
}

.transaction-icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.withdraw {
  color: #a9c9e3;
}

.deposit {
  color: #f799c8;
}

.balance {
  font-size: 12px;
  color: #666;
}
</style>
